package com.honghe.weather.controller;

import com.honghe.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author caoqian
 * @date 20190325
 */
@CrossOrigin
@RestController("weatherController")
@RequestMapping("weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    /**
     * 查询城市天气情况
     * @param city   城市名称
     * @param u      钥匙串
     * @return
     */
    @RequestMapping(value = "CityWeather",method = RequestMethod.GET)
    public String cityWeather(String city,String u){
        return weatherService.cityWeather(city,u);
    }
}
