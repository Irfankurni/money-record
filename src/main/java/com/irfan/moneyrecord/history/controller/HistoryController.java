package com.irfan.moneyrecord.history.controller;

import com.irfan.moneyrecord.constant.CommonConstant;
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

    private final HistoryService historyService;
    private final HistoryRepository repository;

    @PostMapping
    public ResponseEntity<MessageResponse> add(@RequestBody HistoryRequest request) throws Exception {
        try {
            return ResponseEntity.ok(historyService.add(request));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(CommonConstant.FAILED));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable String id) throws Exception {
        try {
            return ResponseEntity.ok(historyService.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(CommonConstant.FAILED));
        }
    }

    @GetMapping("/analysis")
    public ResponseEntity<?> getPerWeek(String userId) throws Exception {
        return ResponseEntity.ok(historyService.getPerWeek(userId));
    }

    @GetMapping()
    public ResponseEntity<?> findByUser() throws Exception {
        var result = historyService.getByUser();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getDetail(@PathVariable String id) throws Exception {
        var result = historyService.getDetails(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
