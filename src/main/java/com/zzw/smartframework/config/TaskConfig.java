package com.zzw.smartframework.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.servlet.ServletContext;


public class TaskConfig implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private ServletContext servletContext;

    public void onApplicationEvent(ContextRefreshedEvent event){
        Thread a =new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        a.start();


    }
}
