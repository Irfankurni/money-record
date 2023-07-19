package com.irfan.moneyrecord.history.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeRes {

    private Number today;
    private Number yesterday;
    private List<Double> week;
    private MonthRes month;
}
