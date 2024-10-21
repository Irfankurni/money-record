package com.irfan.moneyrecord.token.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class TokenTest {

    @Test
    public void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(Token.class);
    }

}
