package com.irfan.moneyrecord.history.service;

import com.irfan.moneyrecord.dto.MessageResponse;
import com.irfan.moneyrecord.exception.HistoryServiceException;
import com.irfan.moneyrecord.history.dto.HistoryRequest;
import com.irfan.moneyrecord.history.dto.HistoryRes;
import com.irfan.moneyrecord.history.dto.HomeRes;

import java.util.List;

public interface HistoryService {

    MessageResponse add(HistoryRequest request) throws HistoryServiceException;

    MessageResponse delete(String id) throws HistoryServiceException;
    List<HistoryRes> getByUser(String type);

    HomeRes getPerWeek() throws HistoryServiceException;
    HistoryRes getDetails(String id);
}
