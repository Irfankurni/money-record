package com.irfan.moneyrecord.history.controller;

import com.irfan.moneyrecord.dto.MessageResponse;
import com.irfan.moneyrecord.exception.HistoryServiceException;
import com.irfan.moneyrecord.history.dto.HistoryRequest;
import com.irfan.moneyrecord.history.dto.HistoryRes;
import com.irfan.moneyrecord.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyServiceImpl;

    @PostMapping
    public ResponseEntity<MessageResponse> add(@RequestBody HistoryRequest request) {
        try {
            return ResponseEntity.ok(historyServiceImpl.add(request));
        } catch (HistoryServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable String id) {
        try {
            return ResponseEntity.ok(historyServiceImpl.delete(id));
        } catch (HistoryServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/analysis")
    public ResponseEntity<?> getPerWeek() {
        try {
            return ResponseEntity.ok(historyServiceImpl.getPerWeek());
        } catch (HistoryServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping()
    public ResponseEntity<List<HistoryRes>> findByUser(@RequestParam String type) {
        try {
            var result = historyServiceImpl.getByUser(type);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (HistoryServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<HistoryRes> getDetail(@PathVariable String id) {
        try {
            var result = historyServiceImpl.getDetails(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (HistoryServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
