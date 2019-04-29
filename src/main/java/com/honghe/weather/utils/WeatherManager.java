package com.honghe.weather.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.annotation.Configuration;
import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 北京鸿合智能系统股份有限公司</p>
 *
 * @author hthwx
 * @date 2019/3/20
 */
@Configuration
public class WeatherManager {

    private static WeatherManager INSTANCE;

    public static WeatherManager getInstance(){
        if(null== INSTANCE){
            synchronized (WeatherManager.class){
                if(null==INSTANCE){
                    INSTANCE=new WeatherManager();
                }
            }
        }
        return INSTANCE;
    }


    private String CITY_LIST_URL=ConfigUtil.getInstance().getPropertyValue("cityListUrl");

    private String CITY_WEATHER_URL=ConfigUtil.getInstance().getPropertyValue("cityWeatherUrl");

    public String getCityWeatherByCode(String cityCode)
    {
        String cityWeatherUrl = CITY_WEATHER_URL + cityCode.trim();
        String responseStr = httpGet(cityWeatherUrl);
        if(responseStr == null || responseStr.trim().equals(""))
        {
            return "";
        }
        JSONObject jsonObject = JSON.parseObject(responseStr);
        if(jsonObject == null || jsonObject.get("data") ==null)
        {
            return "";
        }
        JSONObject weatherInfoJO = (JSONObject) jsonObject.get("data");
//        System.out.println(weatherInfoJO);
        //转换天气xml
        String hhtXML = "";
        Date now = new Date();
        hhtXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
        hhtXML += "<weather>\n";
        hhtXML += "<cityCode>" + cityCode + "</cityCode>\n";
        hhtXML += "<city><![CDATA[" + weatherInfoJO.get("city") + "]]></city>\n";
        hhtXML += "<dateCn>" + DateUtil.formatDate(now) + "</dateCn>\n";
        JSONObject yesterdayJson = (JSONObject) weatherInfoJO.get("yesterday");
        JSONArray weatherArray = (JSONArray) weatherInfoJO.get("forecast");
        String yesterdayXML = "<date number=\"" + (0) + "\" day=\"" + yesterdayJson.get("date") + "\">\n";
        yesterdayXML += "<low>" + yesterdayJson.get("low").toString().replace("低温 ","") + "</low>\n";
        yesterdayXML += "<high>" + yesterdayJson.get("high").toString().replace("高温 ","") + "</high>\n";
        int typeIndex = getIconIndex(yesterdayJson.getString("type"));
        yesterdayXML += "<code>" + typeIndex + "</code>\n";
        yesterdayXML += "<icon>" +typeIndex + "</icon>\n";
        yesterdayXML += "<icon2>" + typeIndex+ "</icon2>\n";
        yesterdayXML += "<condition><![CDATA[" + typeIndex + "]]></condition>\n";
        yesterdayXML += "<conditionCn><![CDATA[" + (yesterdayJson.get("type")) + "]]></conditionCn>\n";
        yesterdayXML += "</date>\n";
        for(int j = 0;j<weatherArray.size();j++)
        {
            JSONObject tmpJO = (JSONObject) weatherArray.get(j);
            int typeIndexCurr = getIconIndex(tmpJO.getString("type"));
            String dayDate = tmpJO.getString("date");
            if(j == 0)
            {
                hhtXML =hhtXML + "<current_conditions time=\"" + dayDate + "\">\n"
                + "<temp> </temp>\n"
                + "<low>" + tmpJO.get("low").toString().replace("低温 ","")  + "</low>\n"
                + "<high>" + tmpJO.get("high").toString().replace("高温 ","")  + "</high>\n"
                + "<code>" + typeIndexCurr + "</code>\n"
                + "<icon>" + typeIndexCurr + "</icon>\n"
                + "<icon2>" + typeIndexCurr + "</icon2>\n"
                + "<condition><![CDATA[" + typeIndexCurr + "]]></condition>\n"
                + "<conditionCn><![CDATA[" + tmpJO.get("type") + "]]></conditionCn>\n"
                + "</current_conditions>\n";
            }

            hhtXML += "<date number=\"" + (j + 1) + "\" day=\"" + dayDate + "\">\n";
            hhtXML += "<low>" + tmpJO.get("low").toString().replace("低温 ","")  + "</low>\n";
            hhtXML += "<high>" + tmpJO.get("high").toString().replace("高温 ","")  + "</high>\n";
            hhtXML += "<code>" + typeIndexCurr + "</code>\n";
            hhtXML += "<icon>" + typeIndexCurr + "</icon>\n";
            hhtXML += "<icon2>" + typeIndexCurr + "</icon2>\n";
            hhtXML += "<condition><![CDATA[" + typeIndexCurr + "]]></condition>\n";
            hhtXML += "<conditionCn><![CDATA[" + tmpJO.get("type") + "]]></conditionCn>\n";
            hhtXML += "</date>\n";
        }
        hhtXML += yesterdayXML + "</weather>\n";
        return hhtXML;
    }


    public void generateCityList()
    {
        String cityListUrl = CITY_LIST_URL;
        httpGet(cityListUrl);
    }
    public String xmlCityListParse(String ret_xml)
    {
        String cityList = "";
        try {
            Document document = DocumentHelper.parseText(ret_xml);
            Element root = document.getRootElement();
            if(root != null && !root.isTextOnly() && root.getName().equals("xml"))
            {
                Element typeC = root.element("c");
              //  System.out.println(typeC.getName() + "$$$$$$$$$");
                List<Element> listD = typeC.elements("d");
                int i =0;
                File file = new File("/filename.txt");

                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                for(Element elementD : listD)
                {
                    if(elementD != null)
                    {
                        bw.write(elementD.attributeValue("d2") + " " + elementD.attributeValue("d1"));
                        bw.newLine();
                    }
                }
                bw.close();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        return cityList;
    }

    public String httpGet(String requestUrl)
    {
        String resultStr = "";
        //1.使用默认的配置的httpclient
        CloseableHttpClient client = HttpClients.createDefault();
        //2.使用get方法
        HttpGet httpGet = new HttpGet(requestUrl);
        InputStream inputStream = null;
        CloseableHttpResponse response = null;

        try {
            //3.执行请求，获取响应
            response = client.execute(httpGet);

            //看请求是否成功，这儿打印的是http状态码
//            System.out.println(response.getStatusLine().getStatusCode());
            //4.获取响应的实体内容，就是我们所要抓取得网页内容
            HttpEntity entity = response.getEntity();
            //5.将其打印到控制台上面
            if (entity != null) {
               // System.out.println(entity.getContentEncoding().getValue() + "---------------------");
                inputStream = entity.getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    //System.out.println(line);
                    resultStr += line;
                }
            }

        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
//        System.out.println(resultStr);
        return resultStr;
    }
    public int getIconIndex(String weatherType)
    {
        int code = 33;
        if(weatherType != null && !"".equals(weatherType))
        {
            for(int i = 0;i<ConfigConstants.dictionaryStrings.length;i++)
            {
                if(weatherType.equals(ConfigConstants.dictionaryStrings[i]))
                {
                    code = i;
                }
            }
        }

        return code;
    }
}
