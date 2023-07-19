package com.irfan.moneyrecord.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRequest {

    private String type;
    private LocalDate date;
    private Double total;
    private List<HistoryDetailsRequest> details;
}
