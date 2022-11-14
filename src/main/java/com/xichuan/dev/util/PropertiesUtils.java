package com.xichuan.dev.util;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * @Author Xichuan
 * @Date 2022/4/14 13:43
 * @Description
 */
public class PropertiesUtils {

    public static Properties loadProperties(String propertiesPath)throws Exception{
        if (StringUtils.isBlank(propertiesPath)){
            propertiesPath = "default.properties";
        }

        Properties properties = new Properties();
        InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(propertiesPath);
        properties.load(inputStream);
        return properties;
    }

}
