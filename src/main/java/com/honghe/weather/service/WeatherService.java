package com.honghe.weather.service;

import java.util.Map;

/**
 * @author caoqian
 * @date 20190325
 */
public interface WeatherService {
    /**
     * 获取天气
     * @param city 城市名
     * @param u
     * @return
     */
    String cityWeather(String city,String u);

    /**
     * 保存天气
     * @param city     城市名
     * @param weather  天气信息
     * @param time     时间
     * @return
     */
    boolean addWeather(String city,String weather,String time);

    /**
     * 更新天气
     * @param city     城市名
     * @param weather  天气信息
     * @param time     时间
     * @return
     */
    boolean updateWeather(String city,String weather,String time);

    /**
     * 清空天气数据
     * @return
     */
    boolean delAllWeather();

    /**
     * 将城市code编码放到缓存中
     */
    void getWeatherCode();

    /**
     * 将城市名放到缓存中
     */
    void getCities();

    /**
     * 天气刷新
     */
    void weatherForeCase();


}
