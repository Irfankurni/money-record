package com.irfan.moneyrecord.history.service;

import com.irfan.moneyrecord.dto.MessageResponse;
import com.irfan.moneyrecord.history.dto.HistoryRequest;
import com.irfan.moneyrecord.history.dto.HistoryRes;
import com.irfan.moneyrecord.history.dto.HomeRes;

import java.util.List;

public interface HistoryService {

    MessageResponse add(HistoryRequest request) throws Exception;
    MessageResponse delete(String id) throws Exception;
    List<HistoryRes> getByUser(String type);
    HomeRes getPerWeek() throws Exception;
    HistoryRes getDetails(String id);
}
