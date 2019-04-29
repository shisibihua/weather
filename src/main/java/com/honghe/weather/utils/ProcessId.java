package com.honghe.weather.utils;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 读取当前程序pid，用于关闭
 *
 * @auther Libing
 * @Time 2017/11/20 16:36
 */
public class ProcessId {


    public static final void setProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Integer pid = Integer.valueOf(runtimeMXBean.getName().split("@")[0])
                .intValue();
        try {
            File file = new File("pid.txt");
            if(!file.exists()){
                file.createNewFile();
            }
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            // 往文件里写入字符串
            ps.println(pid);
            ps.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
            System.out.print("创建文件失败");
        }

    }
}
