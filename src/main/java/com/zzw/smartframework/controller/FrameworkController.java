package com.zzw.smartframework.controller;

import com.alibaba.fastjson.JSON;
import com.zzw.smartframework.util.ClassUtil;
import com.zzw.smartframework.util.CommonContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FrameworkController {

    @Autowired
    public ServletContext servletContext;

    @Value("${spring.filePath}")
    String filePath;

    /**
     * 访问页面
     * @param page
     * @return
     */
    @RequestMapping("/page/{page}")
    public String helloword(@PathVariable("page") String page){
        return page;
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password ){
        if("admin".equals(username) && "123456".equals(password)){
            return "main";
        }else{
            return "errorPage";
        }
    }


    /**
     * 上传插件
     * @param file
     * @param model
     * @return
     */
    @RequestMapping(value = "/uploadPlug", method = RequestMethod.POST)
    @ResponseBody
    public String uploadPlug(
            @RequestParam("file") MultipartFile file,
            Map<String, Object> model
    ) {
        ApplicationContext applicationContext = CommonContextUtils.getApplicationContext();
        ConfigurableApplicationContext context = (ConfigurableApplicationContext)applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
        ClassLoader beanClassLoader = beanFactory.getBeanClassLoader();
        if (!file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                File outFile = new File(filePath+originalFilename);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
                out.write(file.getBytes());
                out.flush();
                out.close();

                Map<String, Object> stringObjectMap = ClassUtil.LoadJar(outFile, beanClassLoader);

                Class aClass = ((List<Class>) stringObjectMap.get("class")).get(0);
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
                beanFactory.registerBeanDefinition(aClass.getName(),beanDefinitionBuilder.getRawBeanDefinition());

                Map<String,HashMap<String,Object>> classListMap = (Map<String,HashMap<String,Object>>) applicationContext.getBean("plugLogicBean");
                Object unit = stringObjectMap.get("unit");
                Object method = stringObjectMap.get("method");
                Object describe = stringObjectMap.get("describe");


                HashMap<String,Object> classHashMap  = new HashMap<>();
                classHashMap.put("unit",unit);
                classHashMap.put("method",method);
                classHashMap.put("describe",describe);
                classHashMap.put("class",aClass);

                classListMap.put(unit + "!!::!!" + method,classHashMap);

                model.put("unit",unit);
                model.put("method",method);
                model.put("describe",describe);
            } catch (FileNotFoundException e) {
                model.put("error",e.getMessage());
            } catch (IOException e) {
                model.put("error",e.getMessage());
            }
        } else {
            model.put("error","上传失败，因为文件是空的.");
        }
        return JSON.toJSONString(model);
    }

    @RequestMapping(value = "/uploadJs", method = RequestMethod.POST)
    public String uploadJs(
            @RequestParam("file") MultipartFile file,
            Map<String, Object> model){
        try{
            String originalFilename = file.getOriginalFilename();

            File outFile = new File(servletContext.getRealPath("/")+"/WEB-INF/js/"+originalFilename);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
            out.write(file.getBytes());
            out.flush();
            out.close();
            model.put("jsFile",originalFilename);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "testJs";
    }


    /**
     * 执行逻辑
     * @param unit
     * @param method
     * @return
     */
    @RequestMapping(value = { "/excute/{unit}/{method}" })
    @ResponseBody
    public String beanLoad(
            @PathVariable("unit") String unit,
            @PathVariable("method") String method,
            @RequestBody(required=false) HashMap<String,Object> param
    ){

        ApplicationContext applicationContext = CommonContextUtils.getApplicationContext();
        Map<String,HashMap<String,Object>> classListMap = (Map<String,HashMap<String,Object>>) applicationContext.getBean("plugLogicBean");
        HashMap<String, Object> stringObjectHashMap = classListMap.get(unit + "!!::!!" + method);
        Class aClass = (Class)stringObjectHashMap.get("class");
        Object logic = (applicationContext.getBean(aClass));
        Method setApplicationContext = null;
        Method setParam = null;
        Method excute = null;
        try {
            setApplicationContext = aClass.getMethod("setApplicationContext", ApplicationContext.class);
            setParam = aClass.getMethod("setParam", HashMap.class);
            excute = aClass.getMethod("excute", null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object ret = null;
        try {
            setApplicationContext.invoke(logic,applicationContext);
            setParam.invoke(logic,param);
            ret = excute.invoke(logic, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    /**
     * 获得插件列表
     * @return
     */
    @RequestMapping(value = { "/getPlugs" },produces="application/json;charset=UTF-8")
    @ResponseBody
    public String getPlugs(){
        ApplicationContext applicationContext = CommonContextUtils.getApplicationContext();
        Map<String,HashMap<String,Object>> classListMap = (Map<String,HashMap<String,Object>>) applicationContext.getBean("plugLogicBean");
        String s = JSON.toJSONString(classListMap.values());
        return s;
    }


    /**
     * 获取菜单
     * @return
     */

    @RequestMapping(value = { "/getMenus" },produces="application/json;charset=UTF-8")
    @ResponseBody
    public String getMenus(){
        ApplicationContext applicationContext = CommonContextUtils.getApplicationContext();
        String s = JSON.toJSONString(applicationContext.getBean("menuBean"));
        return s;
    }


}
