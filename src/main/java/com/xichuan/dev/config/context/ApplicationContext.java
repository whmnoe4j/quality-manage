package com.xichuan.dev.config.context;

import com.xichuan.dev.audit.AbstractStrategy;
import com.xichuan.dev.config.annotation.BeforeExit;
import com.xichuan.dev.config.annotation.RegisterBean;
import com.xichuan.dev.config.annotation.StrategyAnnotation;
import com.xichuan.dev.util.ClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @Author Xichuan
 * @Date 2022/4/13 10:48
 * @Description
 */

/**
 * ApplicationContext
 */
public class ApplicationContext {
    //Application的package
    private String PackageName;

    //bean容器
    private ConcurrentHashMap<String, Object> beanMap = null;

    /**
     * 初始化ApplicationContext
     * @param PackageName 最外面的package
     * @throws Exception
     */
    public ApplicationContext(String PackageName) throws Exception {
        if (PackageName == null || PackageName.equals("")) {
            throw new Exception("package name err");
        }
        this.PackageName = PackageName;
        initBean();
    }


    /**
     * 根据bean名词获取bean
     * @param beanid bean名称
     * @return
     * @throws Exception
     */
    public Object getBean(String beanid) throws Exception {
        if (beanid==null||beanid.equals("")){
            throw new Exception("beanid not null");
        }
        Object o = beanMap.get(beanid);
        if (o==null){
            throw new Exception("class not find");
        }
        return o;
    }

    /**
     * 初始化bean
     * @throws Exception
     */
    private void initBean() throws Exception {
        //获取所有给bean设置注解的类
        beanMap = getAllAnnotationClass();
        //将对象赋值给该属性
        AttriAssign();
    }

    /**
     * 将对象赋值给该属性
     * @throws IllegalAccessException
     */
    private void AttriAssign() throws IllegalAccessException {
        if (beanMap!=null&&beanMap.size()>0){
            for (Object o:beanMap.values()){
                // 获取类的属性是否存在 获取bean注解
                Class<? extends Object> classInfo = o.getClass();
                Field[] declaredFields = classInfo.getDeclaredFields();
                for (Field field : declaredFields) {
                    // 属性名称
                    String name = field.getName();
                    // 2.使用属性名称查找bean容器赋值
                    Object bean = beanMap.get(name);
                    if (bean != null) {
                        // 私有访问允许访问
                        field.setAccessible(true);
                        // 给属性赋值
                        field.set(o, bean);
                    }
                }
            }
        }
    }

    /**
     * 获取所有注解的类
     * @return
     * @throws Exception
     */
    private ConcurrentHashMap<String,Object> getAllAnnotationClass() throws Exception {
        ConcurrentHashMap<String,Object> concurrentHashMap = null;
        List<Class<?>> allClasses = ClassUtil.getClasses(PackageName);
        if (allClasses==null||allClasses.size()==0){
            throw new Exception("package not class");
        }
        concurrentHashMap = new ConcurrentHashMap<String, Object>();
        for (Class<?> c : allClasses) {
            //如果含有策略注解，将策略bean注册到 StrategyContext
            StrategyAnnotation strategyAnnotation = c.getDeclaredAnnotation(StrategyAnnotation.class);
            Object newInstance = null;
            if (strategyAnnotation != null){
                String strategyName = toLowerCaseFirstOne(c.getSimpleName());
                if (strategyAnnotation.name() != null && !"".equals(strategyAnnotation)){
                    strategyName = strategyAnnotation.name();
                }
                newInstance = c.newInstance();
                ((AbstractStrategy)newInstance).register(strategyName);

            }

            //如果含有RegisterBean注解，将bean注册到applicationContext
            RegisterBean registerAnnotation = c.getDeclaredAnnotation(RegisterBean.class);
            if (registerAnnotation != null && registerAnnotation.isLoad()){
                if(newInstance == null){
                    newInstance = c.newInstance();
                }
                String beanId = registerAnnotation.name();
                if (beanId == null || "".equals(beanId)){
                    beanId = toLowerCaseFirstOne(c.getSimpleName());
                }
                concurrentHashMap.put(beanId, newInstance);
            }
        }
        return concurrentHashMap;
    }

    /**
     * ApplicationContext#close
     */
    public void close()throws Exception{
        //处理BeforeExit
        beforeExist();

        //cleanBean
        cleanBean();
    }

    //clean bean
    private void cleanBean(){
        beanMap.clear();
    }

    //deal BeforeExit
    private void beforeExist() throws InvocationTargetException, IllegalAccessException {
        if (beanMap != null){
            Object object = null;
            for (Map.Entry<String,Object> entry: beanMap.entrySet()){
                object = entry.getValue();
                Method[] methods = object.getClass().getMethods();
                for (Method method : methods){
                    //调用含有BeforeExit注解的方法
                    BeforeExit beforeExit = method.getAnnotation(BeforeExit.class);
                    if (beforeExit != null){
                        method.invoke(object);
                    }
                }

            }
        }
    }


    /**
     * 将首字母小写
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))){
            return s;
        }
        else{
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

}
