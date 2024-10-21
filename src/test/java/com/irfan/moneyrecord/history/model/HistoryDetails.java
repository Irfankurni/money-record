package com.irfan.moneyrecord.history.model;

import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

class HistoryDetailsTest {

    @Test
    public void getterAndSetterCorrectness() throws Exception {
        new BeanTester().testBean(HistoryDetails.class);
    }

}
