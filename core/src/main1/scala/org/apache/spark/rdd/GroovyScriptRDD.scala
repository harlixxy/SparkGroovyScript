package org.apache.spark.rdd
import com.google.common.base.Charsets
import com.google.common.io.Resources
import io.unimatic.scripting.groovy.Context
import io.unimatic.scripting.groovy.DataPoint
import io.unimatic.scripting.groovy.Executor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.URL
import java.util.Random
import java.util.concurrent.TimeUnit
import com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly
import java.util.HashMap
import org.apache.spark.util.GroovyScriptHashMap





import java.io.BufferedWriter
import java.io.File
import java.io.FilenameFilter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.util
import java.util.StringTokenizer
import java.util.concurrent.atomic.AtomicReference
import scala.collection.JavaConverters._
import scala.collection.Map
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.reflect.ClassTag
import org.apache.spark.{Partition, SparkEnv, TaskContext}
import org.apache.spark.util.Utils

/**
  * 要注意这里的map，一定要注意这里的map是java.util包下的，还是scala.collection包下面的
  * 该类重写了RDD的俩个方法，一个是getPartitons方法
  * 另一个是compute方法
  *
  */
private[spark]class GroovyScriptRDD[T: ClassTag](prev: RDD[T],
                                                 scriptText: Seq[String],
                                                 envVars: Map[String, String],
                                                 printPipeContext: (String => Unit) => Unit,
                                                 printRDDElement: (T, String => Unit) => Unit,
                                                 separateWorkingDir: Boolean,
                                                 bufferSize: Int,
                                                 encoding: String) extends RDD[String](prev) {

  override def getPartitions: Array[Partition] = firstParent[T].partitions//从父RDD那里获得分区数，继承父RDD



  var resIterator1:Iterator[String]= null

   def compute(split: Partition, context: TaskContext): Iterator[String] = {

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
         //start compute
         val start = System.currentTimeMillis();
         if (null == s){

           s = ctx1.getStatusValue();
         }
         ctx1.restore(s)
         val timestamp = now

         val count = 5l
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
         resIterator1

       }
     }//线程Runnable runner end


     new Thread(runner, "Thread-1").start();



     return resIterator1
  }//compute 结束了



}


object GroovyScriptRDD{
  // Split a string into words using a standard StringTokenizer将代码按照标准standard StringTokenizer分成单个单个的单词。
  def tokenize(scriptText: String): Seq[String] = {
    val buf = new ArrayBuffer[String]
    val tok = new StringTokenizer(scriptText)
    while(tok.hasMoreElements) {
      buf += tok.nextToken()
    }
    buf
  }



}
