package com.zzw.smartframework.config;

import com.zzw.smartframework.util.ClassUtil;
import com.zzw.smartframework.util.CommonContextUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Configuration
public class PlugLoadConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${spring.filePath}")
    String filePath;

    Map<String,HashMap<String,Object>> classListMap;

    @Bean
    public Map<String,HashMap<String,Object>>  plugLogicBean(){
        classListMap = new HashMap<>();
        return classListMap;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        File file = new File(filePath);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jar");
            }
        });

        for (File outFile : files) {
            ApplicationContext applicationContext = CommonContextUtils.getApplicationContext();
            ConfigurableApplicationContext context = (ConfigurableApplicationContext)applicationContext;
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
            ClassLoader beanClassLoader = beanFactory.getBeanClassLoader();

            Map<String, Object> stringObjectMap = ClassUtil.LoadJar(outFile, beanClassLoader);
            Class aClass = ((List<Class>) stringObjectMap.get("class")).get(0);
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
            beanFactory.registerBeanDefinition(aClass.getName(),beanDefinitionBuilder.getRawBeanDefinition());

            Object unit = stringObjectMap.get("unit");
            Object method = stringObjectMap.get("method");
            Object describe = stringObjectMap.get("describe");
            HashMap<String,Object> classHashMap  = new HashMap<>();
            classHashMap.put("unit",unit);
            classHashMap.put("method",method);
            classHashMap.put("describe",describe);
            classHashMap.put("class",aClass);
            classListMap.put(unit + "!!::!!" + method,classHashMap);
        }
    }
}
