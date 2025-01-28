package com.irfan.moneyrecord.history.model;

import com.irfan.moneyrecord.factory.LocalDateFactory;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import java.time.LocalDate;

class HistoryTest {

    @Test
    public void getterAndSetterCorrectness() throws Exception {
        final BeanTester beanTester = new BeanTester();
        beanTester.getFactoryCollection().addFactory(LocalDate.class, new LocalDateFactory());
        beanTester.testBean(History.class);
    }

}
