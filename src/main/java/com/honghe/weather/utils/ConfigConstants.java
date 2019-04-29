package com.honghe.weather.utils;

import org.springframework.beans.factory.annotation.Value;
import java.util.Hashtable;

/**
 * 读取配置文件
 * @author caoqian
 * @date 20190325
 */
public class ConfigConstants {
	
	public static Hashtable<String, String> CHINA_WEATHER_CITY_CODE_TABLE = new Hashtable<String, String>();
	
	public static Hashtable<String, String> CITYS_TABLE = new Hashtable<String, String>();

	public final static String[] dictionaryStrings = { "龙卷风", "热带风暴", "飓风", "强雷阵雨",
            "雷阵雨", "小雨加雪", "雨加冰雹", "雪加冰雹", "冰雨", "毛毛雨", "冻雨", "小雨", "阵雨", "小雪",
            "零星小雪", "高吹雪", "雪", "冰雹", "雨夹雪", "尘", "雾", "薄雾", "多烟的", "大风", "有风",
            "寒冷", "阴天", "阴", "白天阴天", "夜间多云", "白天多云", "夜间清亮", "晴朗", "晴",
            "转晴", "雨夹冰雹", "热", "雷阵雨", "雷阵雨", "雷阵雨", "雷阵雨", "大雪", "阵雪", "中雪",
            "多云", "雷", "阵雪", "雷雨" };

}
