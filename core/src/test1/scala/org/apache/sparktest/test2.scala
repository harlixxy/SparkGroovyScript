package org.apache.sparktest
import java.util.concurrent.TimeUnit;
import java.util

import com.google.common.base.Charsets
import com.google.common.io.Resources
import io.unimatic.scripting.groovy.Executor
import org.apache.spark.util.GroovyScriptHashMap
import com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;

/**
  * Created by Administrator on 2017/3/30.
  */
object test2 {

  def main(args: Array[String]): Unit = {
    var resIterator1:Iterator[String]= null
    var results: util.Map[String, _]=new util.HashMap[String,Object]()
    val url = Resources.getResource("ScriptForFatigue.groovy")
    val scriptTexts = Resources.toString(url, Charsets.UTF_8)
    val executor = new Executor(scriptTexts);
    executor.prepare();

    val runner = new Runnable {
      val idx = 0
      override def run() = {
        var now = 1300000000000L
        now = System.currentTimeMillis()
        var ctx1 = executor.newContext();
        var s:util.Map[String,Object] = new util.HashMap[String,Object]()
        s.put("bjName", "省煤器集箱");//这里以后通过参数传进来,bjName
        //s.put("bjName", "高过集箱");
        s.put("material", "SA-335P91");//这里以后通过参数传进来,material

        ctx1.restore(s)
        s = ctx1.getStatusValue();
        val threadnum = 1l to 100l
        threadnum.foreach{num =>
          println(num);
        //start compute
        val start = System.currentTimeMillis();
        if (null == s){

          s = ctx1.getStatusValue();
        }
        ctx1.restore(s)
        val timestamp = now

        val count = num
        val groovyHashMap = new GroovyScriptHashMap(count,timestamp,executor)
        val  hashmapres = groovyHashMap.getHashMap
        results = groovyHashMap.executorStartRun(executor,hashmapres,ctx1)

        val lines = results
        val keys = lines.keySet()
        val values = lines.values()
        val keysiterator: util.Iterator[String] = keys.iterator();
        val valuesIterator:util.Iterator[_] = values.iterator();


        var resIterator:Iterator[String] = new Iterator[String] {
          def next(): String = {
            if (!hasNext()) {
              throw new NoSuchElementException()
            }
            lines.values().toString
          }

          def hasNext(): Boolean = {
            val result = if (keys.iterator().hasNext) {
              true
            } else {

              false
            }

            result
          }


        }

        resIterator1 = resIterator
        println("5555555")

        println(resIterator1)

          val elapsed = System.currentTimeMillis() - start;
          if (elapsed < 1000L) {
            sleepUninterruptibly(1000L - elapsed, TimeUnit.MILLISECONDS);
            now += 1000L;
          } else {
            now += elapsed;
          }
          s = null;
      }//foreach end

      }//run end
    }//线程Runnable runner end


    new Thread(runner, "Thread-1").start();

    println("4444444444444444444")

    println(resIterator1)
  }

}
