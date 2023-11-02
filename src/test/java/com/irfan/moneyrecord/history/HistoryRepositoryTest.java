package com.irfan.moneyrecord.history;

import com.irfan.moneyrecord.history.model.History;
import com.irfan.moneyrecord.history.repository.HistoryRepository;
import com.irfan.moneyrecord.user.model.Role;
import com.irfan.moneyrecord.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class HistoryRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private HistoryRepository historyRepository;

    private User user;

    private History history;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .fullName("john")
                .email("john@mail.com")
                .password("123456")
                .role(Role.USER)
                .build();
        em.persist(user);

        history = History.builder()
                .type("Pemasukkan")
                .date(LocalDate.now())
                .total(10000D)
                .user(user)
                .build();
        em.persist(history);
        em.flush();
    }

    @Test
    public void findHistoryByUser() {
        var histories = historyRepository.findByUserAndType(user, history.getType());
        assertThat(1)
                .isEqualTo(histories.size());
    }

    @Test
    public void findHistoryByUserNull() {
        var histories = historyRepository.findByUserAndType(user, "Tipe");
        assertThat(0)
                .isEqualTo(histories.size());
    }

}
