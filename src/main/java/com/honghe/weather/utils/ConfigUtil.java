package com.honghe.weather.utils;


import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 读取配置文件properties文件
 * @author caoqian
 * @date 20180823
 */
public class ConfigUtil {
    private Logger log= LoggerFactory.getLogger(ConfigUtil.class);
    private static ConfigUtil INSTANCE=null;
    public static ConfigUtil getInstance(){
        if(null==INSTANCE){
            synchronized (ConfigUtil.class){
                if(null==INSTANCE){
                    INSTANCE=new ConfigUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 读取配置文件
     * @param key   key值
     * @return
     */
    public String getPropertyValue(String key){
        String value="";
        Properties properties=new Properties();
        String filePath=getWebRootFilePath()+"config/application.properties";
        try {
            properties.load(new FileInputStream(filePath));
            if(properties.get(key)!=null && StringUtil.isNotBlank(properties.get(key).toString())){
                value=properties.get(key).toString();
            }
        } catch (IOException e) {
            value="";
            log.error("读取配置文件异常,key="+key,e);
        }
        return value;
    }

    /**
     * 获取文件根目录路径
     * @return
     */
    public String getWebRootFilePath(){
        try {
            return (new File("")).getCanonicalPath() + File.separator;
        } catch (Exception var1) {
            return "";
        }
    }
}
