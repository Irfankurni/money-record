package com.irfan.moneyrecord.history.dao;

import com.irfan.moneyrecord.config.BaseEntityManager;
import com.irfan.moneyrecord.history.dto.HomeRes;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Repository
public class HistoryDao extends BaseEntityManager {

    @SuppressWarnings("unchecked")
    public HomeRes getPerWeek(String userId) {
        String sql = "SELECT date, trim(to_char(date, 'Day')) AS \"day\"," +
                "sum(total) FROM history  \n" +
                "WHERE " +
                "     user_id = :userId " +
                "     AND date >= :dateEnd " +
                "AND lower(TYPE) = lower('Pengeluaran')  " +
                "GROUP BY date " +
                "ORDER BY date DESC";

        Query query = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("dateEnd", LocalDate.now().minusDays(6));
        List<Object[]> list = query.setMaxResults(7).getResultList();
        HomeRes homeRes = new HomeRes();
        List<LinkedHashMap<String, Object>> weeks = new ArrayList<>();
        for (Object[] date : list) {
            if (LocalDate.parse(date[0].toString()).isEqual(LocalDate.now())) {
                homeRes.setToday((Number) date[2]);
            }
            if (LocalDate.parse(date[0].toString()).isEqual(LocalDate.now().minusDays(1))) {
                homeRes.setYesterday((Number) date[2]);
            }
            LinkedHashMap<String, Object> week = new LinkedHashMap<>();
            week.put("day", date[1]);
            week.put("total", date[2]);
            weeks.add(week);
        }
        homeRes.setWeek(weeks);

        return homeRes;
    }

    public Double getIncomePerMonth(String userId) {
        String sql = "SELECT cast(sum(total) as double precision) FROM history \n" +
                "WHERE\n" +
                "\tuser_id = :userId\n" +
                "\tAND EXTRACT(MONTH FROM date) = :month\n" +
                " AND lower(type) = lower('Pemasukkan')";

        Query query = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("month", LocalDate.now().getMonthValue());

        return (Double) query.getSingleResult();
    }

    public Double getOutcomePerMonth(String userId) {
        String sql = "SELECT cast(sum(total) as double precision) FROM history \n" +
                "WHERE\n" +
                "\tuser_id = :userId\n" +
                "\tAND EXTRACT(MONTH FROM date) = :month\n" +
                " AND lower(type) = lower('Pengeluaran')";

        Query query = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("month", LocalDate.now().getMonthValue());

        return (Double) query.getSingleResult();
    }


}
