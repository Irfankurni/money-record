package com.irfan.moneyrecord.user.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class UserTest {

    @Test
    public void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(User.class);
    }

}
