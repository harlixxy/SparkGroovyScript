package org.apache.spark.util;

import io.unimatic.scripting.Exceptions.ScriptRTException;
import io.unimatic.scripting.groovy.Context;
import io.unimatic.scripting.groovy.DataPoint;
import io.unimatic.scripting.groovy.Executor;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Administrator on 2017/3/30.
 * 返回一个HashMap(String,DataPoint)
 */
public class ComputeExecutor {

    private long timestamp;
    private Executor executor;

    public ComputeExecutor(){
        super();
    }

    public ComputeExecutor(long timestamp,Executor executor){
        super();
        this.timestamp = timestamp;
        this.executor = executor;

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor executor) {
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
            System.out.println(res+"kkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
          return res;
        }catch (ScriptRTException e){
            e.printStackTrace();
        }
        return res;
    }
}
