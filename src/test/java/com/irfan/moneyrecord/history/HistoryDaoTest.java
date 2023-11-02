package com.irfan.moneyrecord.history;

import com.irfan.moneyrecord.history.dao.HistoryDao;
import com.irfan.moneyrecord.history.model.History;
import com.irfan.moneyrecord.user.model.Role;
import com.irfan.moneyrecord.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class HistoryDaoTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private HistoryDao historyDao;

    private User user;

    @TestConfiguration
    static class HistoryDaoTestContextConfiguration {
        @Bean
        public HistoryDao historyDao() {
            return new HistoryDao();
        }
    }

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .fullName("john")
                .email("john@mail.com")
                .password("123456")
                .role(Role.USER)
                .build();
        em.persist(user);

        History history1 = History.builder()
                .type("Pengeluaran")
                .date(LocalDate.now())
                .total(10000D)
                .user(user)
                .build();
        History history2 = History.builder()
                .type("Pemasukkan")
                .date(LocalDate.now().minusDays(1))
                .total(110000D)
                .user(user)
                .build();
        em.persist(history1);
        em.persist(history2);
        em.flush();
    }

    @Test
    public void getPerWeek() {
        var histories = historyDao.getPerWeek(user.getId());
        assertThat(10000D)
                .isEqualTo(histories.getToday().doubleValue());
    }

    @Test
    public void getIncomePerMonth() {
        var histories = historyDao.getIncomePerMonth(user.getId());
        assertThat(110000D)
                .isEqualTo(histories.doubleValue());
    }

    @Test
    public void getOutcomePerMonth() {
        var histories = historyDao.getOutcomePerMonth(user.getId());
        assertThat(10000D)
                .isEqualTo(histories.doubleValue());
    }

}
