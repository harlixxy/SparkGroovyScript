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

         val executor = new Executor(scriptTexts);
         executor.prepare();
         var ctx1 = executor.newContext();
          //enumStatus是传进来的参数
         ctx1.restore(enumStatus)
         //start compute
         val start = System.currentTimeMillis();
         if (null == enumStatus){
           enumStatus = ctx1.getStatusValue();
         }
         ctx1.restore(enumStatus)
         var arrayList = new util.ArrayList[util.Map[String,_]]()

         for(elem <- iterator){
           var now = 1300000000000L
           now = System.currentTimeMillis()
           val timestamp = now
           val computeExecutor = new ComputeExecutor(timestamp,executor)
           val  hashmapres = getDataPointHashMap(requireName,elem,executor,timestamp)
           val result:java.util.Map[String,_] = computeExecutor.executorStartRun(executor,hashmapres,ctx1)
           arrayList.add(result)

         }

         val javaIterator = arrayList.iterator()

         new Iterator[String] {
           def next(): String = {
             if (!hasNext()) {
               throw new NoSuchElementException()
             }
             javaIterator.next().toString
           }
           def hasNext(): Boolean = {
             val result = if (javaIterator.hasNext) {
               true
             } else {
               false
             }
             result
           }
         }

         //返回结

  }//compute 结束了
  def getDataPointHashMap(requireName:util.HashMap[String,Object], line: Any,executor: Executor,timestamp:Long) : util.HashMap[String,DataPoint] ={
      val resHashMap = new util.HashMap[String, DataPoint]() {
      val requireKeys:util.Set[String] = executor.getIndefine.keySet
      val mapAndVlaues = line.toString.split(",")
      for(mapAndValue<-mapAndVlaues){
        val key = mapAndValue.split(":")(0)
        val value = mapAndValue.split(":")(1)
        if(requireKeys.contains(key)){
          put(key,new DataPoint(timestamp,java.lang.Double.parseDouble(value)))
        }else{
          System.err.println("no this require key , please check the require ")
        }
      }
    }
    return resHashMap
  }
}

