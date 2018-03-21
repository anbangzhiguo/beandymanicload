package com.zzw.smartframework.logic;

import org.springframework.context.ApplicationContext;

public abstract class BaseLogic implements Logic {

    public ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
