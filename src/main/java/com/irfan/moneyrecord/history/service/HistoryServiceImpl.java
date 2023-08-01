package com.irfan.moneyrecord.history.service;

import com.irfan.moneyrecord.constant.CommonConstant;
import com.irfan.moneyrecord.dto.MessageResponse;
import com.irfan.moneyrecord.history.dao.HistoryDao;
import com.irfan.moneyrecord.history.dto.*;
import com.irfan.moneyrecord.history.model.History;
import com.irfan.moneyrecord.history.model.HistoryDetails;
import com.irfan.moneyrecord.history.repository.HistoryDetailsRepository;
import com.irfan.moneyrecord.history.repository.HistoryRepository;
import com.irfan.moneyrecord.user.model.User;
import com.irfan.moneyrecord.user.principal.PrincipalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository repository;
    private final HistoryDetailsRepository detailsRepository;
    private final PrincipalService principalService;
    private final HistoryDao historyDao;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public MessageResponse add(HistoryRequest request) throws Exception {
        var user = principalService.getAuthPrincipal();

        Double total = request.getTotal();
        if(!request.getDetails().isEmpty()) {
            total = request.getDetails().stream()
                    .map(HistoryDetailsRequest::getPrice)
                    .reduce(0D, Double::sum);
        }

        var history = History.builder()
                .user((User) user)
                .date(request.getDate())
                .type(request.getType())
                .total(total)
                .build();
        var historySaved = repository.save(history);

        for (var details : request.getDetails()) {
            var detail = HistoryDetails.builder()
                    .history(historySaved)
                    .name(details.getName())
                    .price(details.getPrice())
                    .build();
            detailsRepository.save(detail);
        }

        return MessageResponse.builder()
                .message(CommonConstant.SUCCESS)
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public MessageResponse delete(String id) throws Exception {
        var details = detailsRepository.deleteByHistoryId(id);
        if (details) {
            repository.deleteById(id);
        }
        return MessageResponse.builder()
                .message(CommonConstant.SUCCESS)
                .build();
    }

    @Override
    public HomeRes getPerWeek() throws Exception {
        User user = (User) principalService.getAuthPrincipal();
        HomeRes homeRes = historyDao.getPerWeek(user.getId());
        MonthRes monthRes = new MonthRes();
        monthRes.setIncome(historyDao.getIncomePerMonth(user.getId()));
        monthRes.setOutcome(historyDao.getOutcomePerMonth(user.getId()));
        homeRes.setMonth(monthRes);
        return homeRes;
    }

    @Override
    public List<HistoryRes> getByUser() {
        var user = (User) principalService.getAuthPrincipal();

        List<History> histories = repository.findByUser(user);

        List<HistoryRes> result = new ArrayList<>();
        for (History res: histories) {
            var details = detailsRepository.findAllByHistory(res);
            var history = HistoryRes.builder()
                    .id(res.getId())
                    .type(res.getType())
                    .date(res.getDate())
                    .total(res.getTotal())
                    .details(details.stream()
                            .map(detail -> new HistoryDetailRes(detail.getName(), detail.getPrice()))
                            .collect(Collectors.toList()))
                    .build();
            result.add(history);
        }

        return result;
    }

    @Override
    public HistoryRes getDetails(String id) {
        var history = repository.findById(id).orElse(null);
        var details = detailsRepository.findAllByHistory(history);
        return HistoryRes.builder()
                .id(history.getId())
                .type(history.getType())
                .date(history.getDate())
                .total(history.getTotal())
                .details(details.stream()
                        .map(detail -> new HistoryDetailRes(detail.getName(), detail.getPrice()))
                        .collect(Collectors.toList()))
                .build();
    }
}
