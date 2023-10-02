package com.irfan.moneyrecord.user;

import com.irfan.moneyrecord.user.model.Role;
import com.irfan.moneyrecord.user.model.User;
import com.irfan.moneyrecord.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserByEmail() {
        User user = User.builder()
                .fullName("john doe")
                .email("john@mail.com")
                .password("123456")
                .role(Role.USER)
                .build();
        em.persist(user);
        em.flush();
        var userFound = userRepository.findByEmail(user.getEmail()).orElse(new User());
        assertThat(user.getEmail())
                .isEqualTo(userFound.getEmail());
    }

    @Test
    public void whenInvalidEmail_thenReturnNull() {
        User fromDb = userRepository.findByEmail("doesNotExist").orElse(null);
        assertThat(fromDb).isNull();
    }

}
