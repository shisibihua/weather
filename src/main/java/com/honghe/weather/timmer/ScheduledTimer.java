package com.honghe.weather.timmer;


import com.honghe.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器
 *
 * @author caoqiaan
 * @date: 20190325
 */
@Component
@Configuration
public  class ScheduledTimer {

    @Autowired
    private WeatherService weatherService;
    //分钟
    @Value("${refreshInterval}")
    public String REFRESH_INTERVAL;

    /**
     * 每天23:59:00删除原有的城市记录
     * @return
     */
    @Scheduled(cron="0 59 23  * * ?")
    private void clearWeatherDb() {
        weatherService.delAllWeather();
    }

    /**
     * 天气刷新定时器
     * 默认2小时刷新一次
     */
    @Scheduled(initialDelayString = "${delay.time}",fixedDelayString = "${period.time}")
    private void weatherForeCase(){
        //天气刷新定时器
        if (Integer.parseInt(REFRESH_INTERVAL) > 0) {
            weatherService.weatherForeCase();
        }
    }

}
