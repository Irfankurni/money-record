package com.irfan.moneyrecord.history.controller;

import com.irfan.moneyrecord.dto.MessageResponse;
import com.irfan.moneyrecord.exception.HistoryServiceException;
import com.irfan.moneyrecord.history.dto.HistoryRequest;
import com.irfan.moneyrecord.history.dto.HistoryRes;
import com.irfan.moneyrecord.history.dto.HomeRes;
import com.irfan.moneyrecord.history.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class HistoryControllerTest {

    @Mock
    private HistoryService historyServiceImpl;

    @InjectMocks
    private HistoryController historyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAdd_Success() {
        HistoryRequest request = new HistoryRequest();
        MessageResponse expectedResponse = new MessageResponse("History added successfully");

        when(historyServiceImpl.add(request)).thenReturn(expectedResponse);

        ResponseEntity<MessageResponse> response = historyController.add(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testAdd_Failure() {
        HistoryRequest request = new HistoryRequest();
        HistoryServiceException exception = new HistoryServiceException("Failed to add history");

        when(historyServiceImpl.add(request)).thenThrow(exception);

        ResponseEntity<MessageResponse> response = historyController.add(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new MessageResponse(exception.getMessage()), response.getBody());
    }

    @Test
    void testDelete_Success() {
        String id = "1";
        MessageResponse expectedResponse = new MessageResponse("History deleted successfully");

        when(historyServiceImpl.delete(id)).thenReturn(expectedResponse);

        ResponseEntity<MessageResponse> response = historyController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testDelete_Failure() {
        String id = "1";
        HistoryServiceException exception = new HistoryServiceException("Failed to delete history");

        when(historyServiceImpl.delete(id)).thenThrow(exception);

        ResponseEntity<MessageResponse> response = historyController.delete(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new MessageResponse(exception.getMessage()), response.getBody());
    }

    @Test
    void testGetPerWeek_Success() {
        HomeRes expectedResponse = new HomeRes();

        when(historyServiceImpl.getPerWeek()).thenReturn(expectedResponse);

        ResponseEntity<?> response = historyController.getPerWeek();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetPerWeek_Failure() {
        HistoryServiceException exception = new HistoryServiceException("Failed to get history per week");

        when(historyServiceImpl.getPerWeek()).thenThrow(exception);

        ResponseEntity<?> response = historyController.getPerWeek();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(new MessageResponse(exception.getMessage()), response.getBody());
    }

    @Test
    void testFindByUser_Success() {
        String type = "income";
        List<HistoryRes> expectedResponse = Arrays.asList(new HistoryRes(), new HistoryRes());

        when(historyServiceImpl.getByUser(type)).thenReturn(expectedResponse);

        ResponseEntity<List<HistoryRes>> response = historyController.findByUser(type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testFindByUser_Failure() {
        String type = "income";
        HistoryServiceException exception = new HistoryServiceException("Failed to get history by user");

        when(historyServiceImpl.getByUser(type)).thenThrow(exception);

        ResponseEntity<List<HistoryRes>> response = historyController.findByUser(type);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetDetail_Success() {
        String id = "1";
        HistoryRes expectedResponse = new HistoryRes();

        when(historyServiceImpl.getDetails(id)).thenReturn(expectedResponse);

        ResponseEntity<HistoryRes> response = historyController.getDetail(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetDetail_Failure() {
        String id = "1";
        HistoryServiceException exception = new HistoryServiceException("Failed to get history details");

        when(historyServiceImpl.getDetails(id)).thenThrow(exception);

        ResponseEntity<HistoryRes> response = historyController.getDetail(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}
