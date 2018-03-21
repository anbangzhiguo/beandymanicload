package com.zzw.smartframework.logic;

import org.springframework.context.ApplicationContext;

import java.util.Map;

public abstract class BaseLogic implements Logic {

    public ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
    
    
    public Map<String,Object> param;
}
