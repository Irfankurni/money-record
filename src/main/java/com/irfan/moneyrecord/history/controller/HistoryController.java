package com.irfan.moneyrecord.history.controller;

import com.irfan.moneyrecord.dto.MessageResponse;
import com.irfan.moneyrecord.history.dto.HistoryRequest;
import com.irfan.moneyrecord.history.repository.HistoryRepository;
import com.irfan.moneyrecord.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyServiceImpl;
    private final HistoryRepository repository;

    @PostMapping
    public ResponseEntity<MessageResponse> add(@RequestBody HistoryRequest request) throws Exception {
        try {
            return ResponseEntity.ok(historyServiceImpl.add(request));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable String id) throws Exception {
        try {
            return ResponseEntity.ok(historyServiceImpl.delete(id));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @GetMapping("/analysis")
    public ResponseEntity<?> getPerWeek() throws Exception {
        return ResponseEntity.ok(historyServiceImpl.getPerWeek());
    }

    @GetMapping()
    public ResponseEntity<?> findByUser() throws Exception {
        var result = historyServiceImpl.getByUser();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) throws Exception {
        var result = historyServiceImpl.getDetails(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
