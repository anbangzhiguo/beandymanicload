package com.zzw.smartframework.util;
import com.alibaba.fastjson.JSON;
import com.sun.xml.internal.ws.org.objectweb.asm.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {

    public static List<Class> LoadClass(String filePath, ClassLoader beanClassLoader) {
        List<Class> classNameList = new ArrayList<>();
        File clazzPath = new File(filePath);
        int clazzCount = 0;
        if (clazzPath.exists() && clazzPath.isDirectory()) {
            int clazzPathLen = clazzPath.getAbsolutePath().length() + 1;
            Stack<File> stack = new Stack<>();
            stack.push(clazzPath);
            while (stack.isEmpty() == false) {
                File path = stack.pop();
                File[] classFiles = path.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        return pathname.isDirectory() || pathname.getName().endsWith(".class");
                    }
                });
                for (File subFile : classFiles) {
                    if (subFile.isDirectory()) {
                        stack.push(subFile);
                    } else {
                        if (clazzCount++ == 0) {

                            Method method = null;
                            try {
                                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                            boolean accessible = method.isAccessible();
                            try {
                                if (accessible == false) {
                                    method.setAccessible(true);
                                }
                                URLClassLoader classLoader = (URLClassLoader) beanClassLoader;
                                try {
                                    URL url = clazzPath.toURI().toURL();
                                    method.invoke(classLoader, url);
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            } finally {
                                method.setAccessible(accessible);
                            }
                        }
                        String className = subFile.getAbsolutePath();
                        className = className.substring(clazzPathLen, className.length() - 6);
                        className = className.replace(File.separatorChar, '.');
                        try {
                            classNameList.add(Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return classNameList;
    }

    public static Class LoadClass2(String filePath, ClassLoader beanClassLoader) {
        if(!filePath.endsWith(".class")){
            return null;
        }
        File clazzPath = new File(filePath);
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        boolean accessible = method.isAccessible();
        try {
            if (accessible == false) {
                method.setAccessible(true);
            }
            URLClassLoader classLoader = (URLClassLoader) beanClassLoader;
            try {
                URL url = clazzPath.toURI().toURL();
                method.invoke(classLoader, url);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } finally {
            method.setAccessible(accessible);
        }

        String className = upClassName(filePath);
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Map<String,Object> LoadJar(String filePath, ClassLoader beanClassLoader) {
        Map<String,Object> returnMap = null;
        File libPath = new File(filePath);
        File[] jarFiles = libPath.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") || name.endsWith(".zip");
            }
        });

        if (jarFiles != null) {
            Method method = null;
            try {
                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            boolean accessible = method.isAccessible();
            try {
                if (accessible == false) {
                    method.setAccessible(true);
                }

                URLClassLoader classLoader = (URLClassLoader) beanClassLoader;
                for (File file : jarFiles) {
                    URL url = null;
                    try {
                        url = file.toURI().toURL();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        method.invoke(classLoader, url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    returnMap = getClasssFromJarFile(file.getAbsolutePath(), beanClassLoader);
                }
            } finally {
                method.setAccessible(accessible);
            }
        }
        return returnMap;
    }



    public static Map<String,Object> LoadJar(File file, ClassLoader beanClassLoader) {
        Map<String,Object> returnMap = null;

        if (file != null) {
            Method method = null;
            try {
                method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            boolean accessible = method.isAccessible();     // 获取方法的访问权限
            try {
                if (accessible == false) {
                    method.setAccessible(true);     // 设置方法的访问权限
                }
                // 获取系统类加载器
                URLClassLoader classLoader = (URLClassLoader) beanClassLoader;
                URL url = null;
                try {
                    url = file.toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    method.invoke(classLoader, url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                returnMap = getClasssFromJarFile(file.getAbsolutePath(), beanClassLoader);
            } finally {
                method.setAccessible(accessible);
            }
        }
        return returnMap;
    }


    public static Map<String,Object> getClasssFromJarFile(String jarPaht, ClassLoader beanClassLoader) {
        Map<String,Object> returnMap = new HashMap<>();
        List<Class> clazzs = new ArrayList<Class>();
        returnMap.put("class",clazzs);
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(jarPaht);
            List<JarEntry> jarEntryList = new ArrayList<JarEntry>();
            Enumeration<JarEntry> ee = jarFile.entries();
            while (ee.hasMoreElements()) {
                JarEntry entry = (JarEntry) ee.nextElement();
                if (entry.getName().endsWith(".class")) {
                    jarEntryList.add(entry);
                }

                if(entry.getName().endsWith(".txt")){
                    InputStream inputStream = jarFile.getInputStream(entry);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String readLine = null;
                    StringBuffer sb = new StringBuffer();
                    while ((readLine = bufferedReader.readLine()) != null)
                    {
                        sb.append(readLine);
                    }
                    HashMap hashMap = JSON.parseObject(sb.toString(), HashMap.class);
                    returnMap.put("unit",hashMap.get("unit"));
                    returnMap.put("method",hashMap.get("method"));
                    Object describe = hashMap.get("describe");
                    returnMap.put("describe", describe);
                }
            }
            for (JarEntry entry : jarEntryList) {
                String className = entry.getName().replace('/', '.');
                className = className.substring(0, className.length() - 6);
                try {
                    clazzs.add(beanClassLoader.loadClass(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (null != jarFile) {
                try {
                    jarFile.close();
                } catch (Exception e) {
                }

            }
        }
        return returnMap;
    }


    public static String upClassName(String classFile) {
        FileInputStream is = null ;
        ClassReader cr = null;
        try{
            is = new FileInputStream(classFile);
            cr = new ClassReader(is);
        }catch (Exception e){
            e.printStackTrace();
        }
        String className = cr.getClassName();
        return className;
    }



}
