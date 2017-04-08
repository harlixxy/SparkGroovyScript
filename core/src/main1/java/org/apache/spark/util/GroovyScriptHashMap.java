package org.apache.spark.util;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.unimatic.scripting.Exceptions.ScriptRTException;
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
 * Created by Administrator on 2017/3/30.
 * 返回一个HashMap(String,DataPoint)
 */
public class GroovyScriptHashMap {
    private long count;
    private long timestamp;
    private Executor executor;

    public GroovyScriptHashMap(){

    }

    public GroovyScriptHashMap(long count,long timestamp,Executor executor){
        this.count = count;
        this.timestamp = timestamp;
        this.executor = executor;

    }

    /**
     * 返回计算的结果
     * @param executor
     * @param hashMap
     * @param context
     * @return
     */
    public Map<String,?> executorStartRun(Executor executor,HashMap<String,DataPoint> hashMap,Context context){
        Map<String,?> res = null;
        try {
            res = executor.run(context,hashMap);

          return res;

        }catch (ScriptRTException e){
            e.printStackTrace();
        }
        return res;
    }


    public HashMap<String,DataPoint> getHashMap(){
        HashMap<String,DataPoint> resHashMap = new HashMap<String, DataPoint>() {
            {
                for (String s : executor.getIndefine().keySet()) {

                    if(0 < count && count <= 10){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 8.974120221842469));

                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 521.5876961512264));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 353.29892862945206));
                        }
                    }else  if(10 < count && count <= 20){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 513));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 21 * Math.random() + 350));
                        }
                    }else if(20 < count && count <= 30){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 513));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp ,21 * Math.random() + 350));
                        }
                    }else if(30 < count && count <= 40){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 513));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 21 * Math.random() + 350));
                        }
                    }
                    else if(40 < count && count <= 50){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 480));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 21 * Math.random() + 350));
                        }
                    }else if(50 < count && count <= 60){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 457));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 21 * Math.random() + 350));
                        }
                    }else if(60 < count && count <= 70){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 445));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 21 * Math.random() + 350));
                        }
                    }else if(70 < count && count <= 80){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 465));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 21 * Math.random() + 350));
                        }
                    }else if(80 < count && count <= 90){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 439));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 21 * Math.random() + 350));
                        }
                    }else if(90 < count && count <= 100){
                        if(s.equals("dqpress")){
                            put(s, new DataPoint(timestamp , 41 * Math.random() + 3));
                        }
                        if(s.equals("dqinnerT")){
                            put(s, new DataPoint(timestamp, 11 * Math.random() + 440));
                        }
                        if(s.equals("dqoutT")){
                            put(s, new DataPoint(timestamp , 21 * Math.random() + 350));
                        }
                    }



                }
            }
        };

        return resHashMap;
    }

}
