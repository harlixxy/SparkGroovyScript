package org.apache.spark.rdd


import io.unimatic.scripting.groovy.{DataPoint, Executor}
import org.apache.spark.util.ComputeExecutor
import java.util

import scala.reflect.ClassTag
import org.apache.spark.{Partition, TaskContext}

import scala.collection.Iterator


/**
  * 要注意这里的map，一定要注意这里的map是java.util包下的，还是scala.collection包下面的
  * 该类重写了RDD的俩个方法，一个是getPartitons方法
  * 另一个是compute方法
  *
  */
private[spark]class GroovyScriptRDD[T: ClassTag](prev: RDD[T],
                                                 scriptTexts: String,
                                                 var enumStatus: util.Map[String, Object],requireName:util.HashMap[String,Object]) extends RDD[String](prev) {

  override def getPartitions: Array[Partition] = firstParent[T].partitions//从父RDD那里获得分区数，继承父RDD


   def compute(split: Partition, context: TaskContext): Iterator[String] = {


         /**
           * 将上一个rdd的集合传入下一个groovy算法的输入参数中去
           *
           */
         TaskContext.setTaskContext(context)

         val iterator: Iterator[Any] = firstParent[T].iterator(split,context)


         var results: util.Map[String, _]=new util.HashMap[String,Object]()

         val executor = new Executor(scriptTexts);
         executor.prepare();
         var now = 1300000000000L
         now = System.currentTimeMillis()
         var ctx1 = executor.newContext();
          //enumStatus是传进来的参数
         ctx1.restore(enumStatus)
         //start compute
         val start = System.currentTimeMillis();
         if (null == enumStatus){
           enumStatus = ctx1.getStatusValue();
         }
         ctx1.restore(enumStatus)
         val timestamp = now
         val computeExecutor = new ComputeExecutor(timestamp,executor)
         println("first"+iterator)
         val  hashmapres = getDataPointHashMap(requireName,iterator,executor,timestamp)
         results = computeExecutor.executorStartRun(executor,hashmapres,ctx1)

         val lines = results
         val keys = lines.keySet()
         val values = lines.values()
         val keysiterator: util.Iterator[String] = keys.iterator();
         val valuesIterator:util.Iterator[_] = values.iterator();

     //返回结
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
     //返回结
         resIterator
  }//compute 结束了
  def getDataPointHashMap(requireName:util.HashMap[String,Object], iterator: Iterator[Any],executor: Executor,timestamp:Long) : util.HashMap[String,DataPoint] ={
    val preRddMap = new util.HashMap[String,java.lang.Long]()

    for(elem <- iterator){
      println("elem"+elem)
      val splits = elem.toString.split(",")
      for(keyAndValue<- splits){
        val value = java.lang.Long.parseLong(keyAndValue.split(":")(1))
        val key = keyAndValue.split(":")(0)
        println("in is value"+value)
        println("in is key"+key)
        preRddMap.put(key,value)
      }

    }
    println("preRddMap"+preRddMap)
    val resHashMap = new util.HashMap[String, DataPoint]() {
      val sets:util.Set[String] = executor.getIndefine.keySet
      val keysiterator = sets.iterator()

      while (keysiterator.hasNext){
        val next = keysiterator.next()
              println("next is"+next)
              println("get next"+preRddMap.get(next))
              put(next,new DataPoint(timestamp,preRddMap.get(next)))
      }
    }
    return resHashMap
  }
}
import org.apache.spark.internal.Logging
object GroovyScriptRDD extends Logging{
}
