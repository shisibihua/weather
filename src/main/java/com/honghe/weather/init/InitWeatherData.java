package com.honghe.weather.init;

import com.honghe.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * 初始化天气信息
 * @author caoqian
 * @date 20190329
 */

@Component
public class InitWeatherData implements CommandLineRunner{

    @Autowired
    private WeatherService weatherService;
    @Override
    public void run(String... args) throws Exception {
        //初始化天气城市代码
        weatherService.getWeatherCode();
        //初始化城市
        weatherService.getCities();
    }
}
