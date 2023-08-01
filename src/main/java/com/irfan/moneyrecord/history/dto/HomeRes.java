package com.irfan.moneyrecord.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomeRes {

    private Number today;
    private Number yesterday;
    private List<LinkedHashMap<String, Object>> week;
    private MonthRes month;
}
