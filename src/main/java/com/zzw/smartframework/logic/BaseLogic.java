package com.zzw.smartframework.logic;

import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseLogic implements Logic {

    public ApplicationContext applicationContext;
    public Map<String,Object> param;

    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
    public void setParam(HashMap param) {
        this.param = param;
    }
}
