package com.honghe.weather.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author caoqian
 * @date 20190325
 */
public interface WeatherDao {

    /**
     * 根据城市名称查询城市天气信息
     * @param cityName 城市名称
     * @return
     */
    String searchWeatherContent(@Param("cityName") String cityName);

    /**
     * 插入天气数据
     * @param city      城市名称
     * @param weather   天气信息
     * @param time      时间e
     * @return
     */
    boolean addWeather(@Param("city") String city,@Param("weather") String weather,
                       @Param("time") String time);
    /**
     * 更新天气数据
     * @param city      城市名称
     * @param weather   天气信息
     * @param time      时间
     * @return
     */
    boolean updWeather(@Param("city") String city,@Param("weather") String weather,
                       @Param("time") String time);

    /**
     * 清空天气
     */
    boolean clearWeather();

    /**
     * 查询城市名称列表
     * @return
     */
    List<String> searchCityName();


}
