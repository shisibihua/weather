package com.honghe.weather;

import com.honghe.weather.utils.ProcessId;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.TimeZone;


/**
 * @author caoqian
 * @date 20190325
 */
@SpringBootApplication(scanBasePackages = "com.honghe.weather")
@MapperScan("com.honghe.weather.dao")
@ServletComponentScan
@EnableScheduling
public class WeatherServerApplication {


    @Value("${poolSize}")
    private int poolSize;

    public static void main(String[] args){

        //写入PId 用于shutDown
        ProcessId.setProcessID();
        SpringApplication.run(WeatherServerApplication.class, args);
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println(">>>>>>>>>>>天气预报服务启动完成.........");
    }


    //设置定时器
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler=new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(poolSize);
        taskScheduler.setThreadNamePrefix("springboot-task");
        return taskScheduler;
    }

}