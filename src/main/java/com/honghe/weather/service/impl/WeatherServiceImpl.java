package com.honghe.weather.service.impl;

import com.honghe.weather.utils.DateUtil;
import com.honghe.weather.dao.WeatherDao;
import com.honghe.weather.service.WeatherService;
import com.honghe.weather.utils.ConfigConstants;
import com.honghe.weather.utils.MD5;
import com.honghe.weather.utils.WeatherManager;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Configuration
public class WeatherServiceImpl implements WeatherService {
    Logger logger= LoggerFactory.getLogger(WeatherService.class);
    @Value("${lock}")
    private String lock;
    @Autowired
    private WeatherDao weatherDao;

    @Override
    public String cityWeather(String cityName,String u) {
        if(StringUtil.isNotBlank(cityName)){
            try {
                cityName= URLDecoder.decode(cityName, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("城市名转换成urlDecoder异常",e);
            }
        }else{
            return "";
        }
        if (!"0".equals(lock)) {
            //上锁了
            if(StringUtil.isBlank(u)){
                return "";
            }
            SimpleDateFormat keydf = new SimpleDateFormat("'KANG-LIN'yyyy'*5AA7F8F71A707262AC6659CDDC876E46*'MM'*#@$%!*'dd",
                    Locale.CHINA);
            Date today = new Date();
            Date yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000);
            Date tomorrow = new Date(today.getTime() + 24 * 60 * 60 * 1000);
            MD5 md5 = new MD5();
            String key1 = md5.computeMd5Value(keydf.format(yesterday));
            String key2 = md5.computeMd5Value(keydf.format(today));
            String key3 = md5.computeMd5Value(keydf.format(tomorrow));
            if (!u.equalsIgnoreCase(key1) && !u.equalsIgnoreCase(key2) && !u.equalsIgnoreCase(key3)) {
                //钥匙不对
                return "";
            }
        }
        String weather = ConfigConstants.CITYS_TABLE.get(cityName);
        if (StringUtil.isBlank(weather)) {
            //内存没有天气数据
            try {
                weather = weatherDao.searchWeatherContent(cityName);
                if (StringUtil.isBlank(weather)) {
                    //数据库没有天气数据
                    String cityCode = ConfigConstants.CHINA_WEATHER_CITY_CODE_TABLE.get(cityName);
                    if (StringUtil.isNotBlank(cityCode)) {
                        String hhtWeather = WeatherManager.getInstance().getCityWeatherByCode(cityCode);
                        if (StringUtil.isNotBlank(hhtWeather)) {
                            Date now = new Date();
                            ConfigConstants.CITYS_TABLE.put(cityName, hhtWeather);
                            //保存城市天气预报信息
                            weatherDao.addWeather(cityName,hhtWeather,DateUtil.formatDatetime(now));
                            //Ajax回传数据
                            return hhtWeather;
                        }else{
                            return "";
                        }

                    }else {
                        //Ajax回传数据
                        return "";
                    }
                } else {
                    ConfigConstants.CITYS_TABLE.put(cityName, weather);
                    //数据库有天气数据,Ajax回传数据
                    return weather;
                }
            } catch (Exception e) {
                logger.error("根据城市名称查询城市天气信息异常",e);
            }
        } else {
            return weather;
        }
        return null;
    }

    @Override
    public boolean addWeather(String city, String weather, String time) {
        if(StringUtil.isBlank(city) || StringUtil.isBlank(weather)){
            return false;
        }else {
            return weatherDao.addWeather(city,weather,time);
        }
    }

    @Override
    public boolean updateWeather(String city, String weather, String time) {
        if(StringUtil.isBlank(city)){
            return false;
        }else {
            return weatherDao.updWeather(city,weather,time);
        }
    }


    @Override
    public boolean delAllWeather() {
        ConfigConstants.CITYS_TABLE.clear();
        return weatherDao.clearWeather();
    }

    @Override
    public void getWeatherCode() {
        //资源文件所在目录
        String tempPath = System.getProperty("user.dir")+File.separator+"config"+File.separator+"cityCodes.properties";
        File codeFile = new File(tempPath);
        if (codeFile.exists()) {
            //读取资源文件
            FileInputStream fis = null;
            InputStreamReader isr = null;
            BufferedReader reader = null;
            try {
                ConfigConstants.CHINA_WEATHER_CITY_CODE_TABLE = new Hashtable<String, String>();
                fis = new FileInputStream(codeFile);
                isr = new InputStreamReader(fis, "UTF-8");
                reader = new BufferedReader(isr);
                String tempString;
                while ((tempString = reader.readLine()) != null) {
                    //数据行
                    String[] rArray = tempString.trim().split(" ");
                    for (int i = 1; i < rArray.length; i++) {
                        ConfigConstants.CHINA_WEATHER_CITY_CODE_TABLE.put(rArray[0].trim().replace("\uFEFF",""), rArray[1].trim());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception ex) {
                    }
                }
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (Exception ex) {
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }
    }

    @Override
    public void getCities() {
        try {
            List<String> cityNameList=weatherDao.searchCityName();
            if(cityNameList!=null && !cityNameList.isEmpty()){
                for(String cityName:cityNameList){
                    ConfigConstants.CITYS_TABLE.put(cityName, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void weatherForeCase() {
        try {
            Date now = new Date();
            Set<String> citySet = ConfigConstants.CITYS_TABLE.keySet();
            for (String aCity : citySet) {
                String cityCode = ConfigConstants.CHINA_WEATHER_CITY_CODE_TABLE.get(aCity);
                if (StringUtil.isNotBlank(cityCode)) {
                    String hhtWeather = "";
                    try {
                        hhtWeather = WeatherManager.getInstance().getCityWeatherByCode(cityCode);
                        Thread.sleep(15*1000);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    if (StringUtil.isNotBlank(hhtWeather)) {
                        ConfigConstants.CITYS_TABLE.put(aCity, hhtWeather);
                        //保存城市天气预报信息
                        boolean updateCnt=weatherDao.updWeather(aCity,hhtWeather, DateUtil.formatDatetime(now));
                        if (!updateCnt) {
                            //增加记录
                            weatherDao.addWeather(aCity,hhtWeather,DateUtil.formatDatetime(now));
                        }
                    }
                }
            }
            //强制内存回收
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
