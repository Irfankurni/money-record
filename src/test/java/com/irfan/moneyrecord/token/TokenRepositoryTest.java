package com.irfan.moneyrecord.token;

import com.irfan.moneyrecord.token.model.Token;
import com.irfan.moneyrecord.token.model.TokenType;
import com.irfan.moneyrecord.token.repository.TokenRepository;
import com.irfan.moneyrecord.user.model.Role;
import com.irfan.moneyrecord.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class TokenRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TokenRepository tokenRepository;

    private User user;

    private Token token;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .fullName("john")
                .email("john@mail.com")
                .password("123456")
                .role(Role.USER)
                .build();
        em.persist(user);

        token = Token.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();
        em.persist(token);
        em.flush();
    }

    @Test
    public void findValidToken() {
        var tokenValid = tokenRepository.findAllValidTokenByUser(user.getId());
        assertThat(1)
                .isEqualTo(tokenValid.size());
    }

    @Test
    public void findInvalidToken() {
        var revokedToken = tokenRepository.findByToken(token.getToken()).orElseThrow(() -> new NullPointerException("Data Null"));
        revokedToken.setExpired(true);
        revokedToken.setRevoked(true);
        tokenRepository.save(revokedToken);

        var tokenInvalid = tokenRepository.findAllValidTokenByUser(user.getId());
        assertThat(0)
                .isEqualTo(tokenInvalid.size());
    }

    @Test
    public void findByToken() {
        var tokenValid = tokenRepository.findByToken(token.getToken()).orElse(new Token());
        assertThat(token.getToken())
                .isEqualTo(tokenValid.getToken());
    }
}
