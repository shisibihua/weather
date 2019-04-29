package com.honghe.weather.controller;

import com.honghe.weather.WeatherServerApplication;
import com.honghe.weather.service.WeatherService;
import com.honghe.weather.utils.ConfigConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherServerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class WeatherControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired
    private WeatherService weatherService;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void CityWeather() throws Exception {
        Hashtable<String, String> cityMap=ConfigConstants.CHINA_WEATHER_CITY_CODE_TABLE;
        Iterator<Map.Entry<String, String>> iterator = cityMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry=iterator.next();
            String cityName=entry.getKey();
            ResultActions result= mockMvc.perform(get("/weather/CityWeather?city="+cityName)
                    .accept(MediaType.APPLICATION_JSON))
                    .andDo(print()) //打印内容
                    .andExpect(status().isOk());////使用jsonPath解析返回值，判断具体的内容
            if(!result.andReturn().getResponse().getContentAsString().contains("<weather>")){
                System.out.println("查不到此城市的天气信息，cityName="+cityName);
            };
        }

    }

}