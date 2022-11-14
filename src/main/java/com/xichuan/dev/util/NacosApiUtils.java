package com.xichuan.dev.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.text.MessageFormat;
import java.util.Properties;

/**
 * @Author Xichuan
 * @Date 2022/4/14 13:41
 * @Description
 */
public class NacosApiUtils {

    public static JSONObject getJsonProperties(String propertiesFile) throws Exception{
        Properties properties = PropertiesUtils.loadProperties(propertiesFile);
        String nacosUrl = properties.getProperty("NACOS_URL");
        String nacosDataId = properties.getProperty("NACOS_DATA_ID");
        String nacosGroup = properties.getProperty("NACOS_GROUP");
        String nacosNameSpace = properties.getProperty("NACOS_NAMESPACE");
        String totalUrl = MessageFormat.format(properties.getProperty("NACOS_GETPROPERTIES_URL"),nacosUrl,nacosDataId,nacosGroup,nacosNameSpace);
        String resultMessage = HttpURLConnectionUtil.doGet(totalUrl);
        return JSON.parseObject(resultMessage);
    }

}
