package com.zzw.smartframework.config;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
public class MenuConfig implements ApplicationListener<ContextRefreshedEvent> {

    List<HashMap<String,Object>> classListMap;
    @Bean
    public List<HashMap<String,Object>> menuBean(){
        classListMap = new ArrayList<>();
        return classListMap;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        HashMap<String,Object> classHashMap  = new HashMap<>();
        classHashMap.put("code","plugList");
        classHashMap.put("js","pluglist");
        classHashMap.put("describe","插件列表");
        classHashMap.put("subList",null);
        classListMap.add(classHashMap);
    }
}
