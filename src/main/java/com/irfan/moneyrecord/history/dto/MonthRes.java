package com.irfan.moneyrecord.history.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthRes {

    private Double income;
    private Double outcome;
}
