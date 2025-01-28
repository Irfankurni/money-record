package com.irfan.moneyrecord.history;

import com.irfan.moneyrecord.history.dao.HistoryDao;
import com.irfan.moneyrecord.history.dto.HomeRes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class HistoryDaoTest {

    @Mock
    private EntityManager em;

    @Mock
    private Query query;

    @InjectMocks
    private HistoryDao historyDao;

    @BeforeEach
    public void setUp() {
        when(em.createNativeQuery(anyString())).thenReturn(query);
    }

    @Test
    public void testGetPerWeek() {
        String userId = "testUserId";
        LocalDate now = LocalDate.now();
        LocalDate yesterday = now.minusDays(1);
        LocalDate dayBeforeYesterday = now.minusDays(2);

        List<Object[]> resultList = Arrays.asList(
                new Object[]{now.toString(), "Today", 100.0},
                new Object[]{yesterday.toString(), "Yesterday", 50.0},
                new Object[]{dayBeforeYesterday.toString(), "DayBeforeYesterday", 25.0}
        );

        when(query.setParameter(eq("userId"), eq(userId))).thenReturn(query);
        when(query.setParameter(eq("dateEnd"), eq(now.minusDays(6)))).thenReturn(query);
        when(query.setMaxResults(7)).thenReturn(query);
        when(query.getResultList()).thenReturn(resultList);

        HomeRes homeRes = historyDao.getPerWeek(userId);

        assertEquals(100.0, homeRes.getToday());
        assertEquals(50.0, homeRes.getYesterday());
        assertEquals(3, homeRes.getWeek().size());
    }

    @Test
    public void testGetIncomePerMonth() {
        String userId = "testUserId";
        int currentMonth = LocalDate.now().getMonthValue();
        Double expectedIncome = 1500.0;

        when(query.setParameter(eq("userId"), eq(userId))).thenReturn(query);
        when(query.setParameter(eq("month"), eq(currentMonth))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedIncome);

        Double income = historyDao.getIncomePerMonth(userId);

        assertEquals(expectedIncome, income);
    }

    @Test
    public void testGetOutcomePerMonth() {
        String userId = "testUserId";
        int currentMonth = LocalDate.now().getMonthValue();
        Double expectedOutcome = 1000.0;

        when(query.setParameter(eq("userId"), eq(userId))).thenReturn(query);
        when(query.setParameter(eq("month"), eq(currentMonth))).thenReturn(query);
        when(query.getSingleResult()).thenReturn(expectedOutcome);

        Double outcome = historyDao.getOutcomePerMonth(userId);

        assertEquals(expectedOutcome, outcome);
    }
}
