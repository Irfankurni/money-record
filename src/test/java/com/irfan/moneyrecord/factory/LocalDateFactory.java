package com.irfan.moneyrecord.factory;

import org.meanbean.lang.Factory;

import java.time.LocalDate;

public class LocalDateFactory implements Factory<LocalDate> {

    @Override
    public LocalDate create() {
        return LocalDate.now();
    }

}
