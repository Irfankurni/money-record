package com.irfan.moneyrecord.history.service;

import com.irfan.moneyrecord.constant.CommonConstant;
import com.irfan.moneyrecord.dto.MessageResponse;
import com.irfan.moneyrecord.exception.HistoryServiceException;
import com.irfan.moneyrecord.history.dao.HistoryDao;
import com.irfan.moneyrecord.history.dto.*;
import com.irfan.moneyrecord.history.model.History;
import com.irfan.moneyrecord.history.model.HistoryDetails;
import com.irfan.moneyrecord.history.repository.HistoryDetailsRepository;
import com.irfan.moneyrecord.history.repository.HistoryRepository;
import com.irfan.moneyrecord.user.model.User;
import com.irfan.moneyrecord.user.principal.PrincipalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryServiceImplTest {

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private HistoryDetailsRepository historyDetailsRepository;

    @Mock
    private PrincipalService principalService;

    @Mock
    private HistoryDao historyDao;

    @InjectMocks
    private HistoryServiceImpl historyServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdd_Success() throws HistoryServiceException {
        HistoryRequest request = new HistoryRequest();
        request.setDate(LocalDate.now());
        request.setType("income");
        request.setDetails(Arrays.asList(
                new HistoryDetailsRequest("Item1", 100.0),
                new HistoryDetailsRequest("Item2", 200.0)
        ));

        User user = new User();
        when(principalService.getAuthPrincipal()).thenReturn(user);

        History savedHistory = new History();
        when(historyRepository.save(any(History.class))).thenReturn(savedHistory);

        MessageResponse response = historyServiceImpl.add(request);

        assertEquals(CommonConstant.SUCCESS, response.getMessage());
        verify(historyRepository, times(1)).save(any(History.class));
        verify(historyDetailsRepository, times(2)).save(any(HistoryDetails.class));
    }

    @Test
    void testAdd_Failure() {
        HistoryRequest request = new HistoryRequest();
        request.setDate(LocalDate.now());
        request.setType("income");
        request.setDetails(Arrays.asList(
                new HistoryDetailsRequest("Item1", 100.0),
                new HistoryDetailsRequest("Item2", 200.0)
        ));

        when(principalService.getAuthPrincipal()).thenThrow(new HistoryServiceException("Principal service error"));

        assertThrows(HistoryServiceException.class, () -> historyServiceImpl.add(request));
    }

    @Test
    void testDelete_Success() throws HistoryServiceException {
        String id = "1";
        when(historyDetailsRepository.deleteByHistoryId(id)).thenReturn(1);

        MessageResponse response = historyServiceImpl.delete(id);

        assertEquals(CommonConstant.SUCCESS, response.getMessage());
        verify(historyDetailsRepository, times(1)).deleteByHistoryId(id);
        verify(historyRepository, times(1)).deleteById(id);
    }

    @Test
    void testDelete_Failure() {
        String id = "1";
        when(historyDetailsRepository.deleteByHistoryId(id)).thenThrow(new HistoryServiceException("Principal service error"));

        assertThrows(HistoryServiceException.class, () -> historyServiceImpl.delete(id));
    }

    @Test
    void testGetPerWeek_Success() throws HistoryServiceException {
        User user = new User();
        user.setId("user1");
        when(principalService.getAuthPrincipal()).thenReturn(user);

        HomeRes homeRes = new HomeRes();
        when(historyDao.getPerWeek(user.getId())).thenReturn(homeRes);

        MonthRes monthRes = new MonthRes();
        when(historyDao.getIncomePerMonth(user.getId())).thenReturn(1000.0);
        when(historyDao.getOutcomePerMonth(user.getId())).thenReturn(500.0);

        HomeRes response = historyServiceImpl.getPerWeek();

        assertNotNull(response);
        assertEquals(1000.0, response.getMonth().getIncome());
        assertEquals(500.0, response.getMonth().getOutcome());
    }

    @Test
    void testGetPerWeek_Failure() {
        when(principalService.getAuthPrincipal()).thenThrow(new HistoryServiceException("Principal service error"));

        assertThrows(HistoryServiceException.class, () -> historyServiceImpl.getPerWeek());
    }

    @Test
    void testGetByUser_Success() {
        User user = new User();
        when(principalService.getAuthPrincipal()).thenReturn(user);

        List<History> histories = Arrays.asList(
                new History(),
                new History()
        );
        when(historyRepository.findByUserAndType(user, "income")).thenReturn(histories);

        List<HistoryDetails> details = Arrays.asList(
                new HistoryDetails(),
                new HistoryDetails()
        );
        when(historyDetailsRepository.findAllByHistory(any(History.class))).thenReturn(details);

        List<HistoryRes> response = historyServiceImpl.getByUser("income");

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void testGetDetails_Success() {
        String id = "1";
        History history = new History();
        history.setId(id);
        history.setType("income");
        history.setDate(LocalDate.now());
        history.setTotal(300.0);

        when(historyRepository.findById(id)).thenReturn(Optional.of(history));

        List<HistoryDetails> details = Arrays.asList(
                new HistoryDetails(),
                new HistoryDetails()
        );
        when(historyDetailsRepository.findAllByHistory(history)).thenReturn(details);

        HistoryRes response = historyServiceImpl.getDetails(id);

        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals("income", response.getType());
        assertEquals(LocalDate.now(), response.getDate());
        assertEquals(300.0, response.getTotal());
        assertEquals(2, response.getDetails().size());
    }

    @Test
    void testGetDetails_Failure() {
        String id = "1";
        when(historyRepository.findById(id)).thenThrow(new HistoryServiceException("Principal service error"));

        assertThrows(HistoryServiceException.class, () -> historyServiceImpl.getDetails(id));
    }
}
