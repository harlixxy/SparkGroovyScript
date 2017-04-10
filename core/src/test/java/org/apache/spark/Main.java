package org.apache.spark;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.unimatic.scripting.groovy.Context;
import io.unimatic.scripting.groovy.DataPoint;
import io.unimatic.scripting.groovy.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
/**
 * Author: Ryan Jiang <jiangrun@riant-cn.com> Date: 2014-05-09 16:04
 */
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.unimatic.scripting.groovy.Context;
import io.unimatic.scripting.groovy.DataPoint;
import io.unimatic.scripting.groovy.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
/**
 * Author: Ryan Jiang <jiangrun@riant-cn.com> Date: 2014-05-09 16:04
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    static Map<String, ?> results = null;

    static Map<String, Object> states = new HashMap<>();

    static final Random random = new Random(System.currentTimeMillis());

    static double nextDouble() {
        return (double) random.nextInt(20);
    }

    static public void main(String[] args) throws IOException {
        //获取Script1.groovy文件路径
        URL url = Resources.getResource("Rule/rule8.groovy");
//        URL url = Resources.getResource("ScriptForCreep.groovy");
        //加载Script1.groovy文件
        String scriptText = Resources.toString(url, Charsets.UTF_8);

        //    LOGGER.info("Execution phase...");
        //通过scriptText创建Executor对象
        final Executor executor = new Executor(scriptText);
        executor.prepare();
        //executor.getIndefine():获取require中的变量
        //    LOGGER.info("> field define  >> " + executor.getIndefine());
        Runnable r = new Runnable() {
            int idx = 0;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                long now = 1300000000000L;
                now = System.currentTimeMillis();
                //Context封装require，status，output,DataPoint等数据信息
                Context ctx1 = null;
                ctx1 = executor.newContext();
                //设置环境变量
                Map<String, Object> s = new HashMap<>();

                s.put("GOOD_RUNNING(200)", "正常运行");

                s.put("STANDBY(201)", "待机");
                s.put("TEST_STOP(202)", "测试停机");
                s.put("MAINTAINING_STOP(203)", "维护停机");
                s.put("WEATHER_STOP(204)", "天气原因停机");
                s.put("REMOTE_STOP(205)", "远程停机");
                s.put("alarm_RUNNING(400)", "报警运行");
                s.put("alarm_RUNNING_STANDBY(401)", "带报警待机");
                s.put("LIMIT_LOAD_POWER(402)", "限负荷发电");
                s.put("TROUBLE_STOP(500)", "故障停机");

                s.put("NETWORK_STOP(501)", "电网原因停机");

                s.put("COMMUNICATE_STOP(502)", "通讯中断");

                s.put("UNKNOWN_STATUS(601)", "未知状态");


                ctx1.restore(s);

                for (int i = 1; i <= 100; i++) {

                    long start = System.currentTimeMillis();
                    //ctx1.getStatusValue(),获取status的数据。
                    if (null == s){
                        s = ctx1.getStatusValue();
                    }

                    ctx1.restore(s);

                    final long timestamp = now;
                    //    LOGGER.info("> now >> " + now);
                    final long count = i;
                    LOGGER.info("############  START [" + count + "]  ############");
                    try {
                        results = executor.run(ctx1, new HashMap<String, DataPoint>() {
                            {
                                for (String s : executor.getIndefine().keySet()) {



                                    if(0 < count && count <= 10){

                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 4443));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 34));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 434));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 43));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 344));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 343));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 343));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 3434));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 43));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 434));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 3434));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 3434));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 4343));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 434));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 434));
                                        }



                                    }else  if(10 < count && count <= 20){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 8.974120221842469));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 521.5876961512264));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }else if(20 < count && count <= 30){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 8.974120221842469));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 521.5876961512264));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }else if(30 < count && count <= 40){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 0));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 0));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 0));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 0));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 0));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 0));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }
                                    else if(40 < count && count <= 50){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 8.974120221842469));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 521.5876961512264));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }else if(50 < count && count <= 60){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 8.974120221842469));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 521.5876961512264));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }else if(60 < count && count <= 70){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 8.974120221842469));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 521.5876961512264));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }else if(70 < count && count <= 80){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 8.974120221842469));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 521.5876961512264));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }else if(80 < count && count <= 90){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 8.974120221842469));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 521.5876961512264));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }else if(90 < count && count <= 100){
                                        if(s.equals("devStatus")){
                                            DataPoint dataPoint = put(s, new DataPoint(timestamp , 8.974120221842469));
                                        }
                                        if(s.equals("yggl")){
                                            put(s, new DataPoint(timestamp, 521.5876961512264));
                                        }
                                        if(s.equals("alm1")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm2")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm3")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm4")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm5")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm6")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm7")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm8")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("alm9")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("fengsu")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }

                                        if(s.equals("fengxang")){
                                            put(s, new DataPoint(timestamp , 24234.44));
                                        }
                                        if(s.equals("cangwWd")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                        if(s.equals("yeYali")){
                                            put(s, new DataPoint(timestamp , 353.29892862945206));
                                        }
                                    }



                                }
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
System.out.println("resultsresultsresultsresultsresultsresultsresultsresultsresults"+results);

                    LOGGER.info("############  END [" + count + "]  ############");
                    System.out.println();

                    long elapsed = System.currentTimeMillis() - start;
                    if (elapsed < 1000L) {
                        sleepUninterruptibly(1000L - elapsed, TimeUnit.MILLISECONDS);
                        now += 1000L;
                    } else {
                        now += elapsed;
                    }
                    s = null;

                }
            }
        };

        new Thread(r, "Thread-1").start();
    }
}
