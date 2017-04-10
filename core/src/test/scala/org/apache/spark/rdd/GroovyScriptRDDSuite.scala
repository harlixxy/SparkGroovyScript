package org.apache.spark.rdd

import java.io.File
import java.util

import com.google.common.base.Charsets
import com.google.common.io.Resources
import org.apache.spark._
import org.apache.spark.util.Utils


/**
  * Created by Administrator on 2017/3/31.
  */
class GroovyScriptRDDSuite extends SparkFunSuite with SharedSparkContext {

  /**
    * 临时文件目录
    */
  var tempDir: File = _

  override def beforeAll(): Unit = {
    super.beforeAll()
    // 测试 前  用工具类创建临时文件，前缀是spark
    tempDir = Utils.createTempDir()
  }

  override def afterAll(): Unit = {
    try {
      //测试 后  用工具类删除临时文件
      Utils.deleteRecursively(tempDir)
    } finally {
      super.afterAll()
    }
  }

  test("rule8"){
      //textScript
      val url = Resources.getResource("Rule/rule8.groovy")
      val scriptTexts = Resources.toString(url, Charsets.UTF_8)
      //require
      val lines = sc.makeRDD(Array("devStatus:2342,alm1:4232,alm1:434,alm2:5434,alm3:2342,alm4:2342,alm5:434,alm6:5434,alm7:2342,alm8:434,alm9:2342,fengxang:434,yeYali:5434,yeYali:444,cangwWd:4434"))
      //enumStatus环境变量
      var s:util.Map[String,Object] = new util.HashMap[String,Object]()
      //require 输入字段名称
      val requireNames = new util.HashMap[String,Object]()
      val rddres:RDD[String] = lines.groovyCompute(scriptTexts,s,requireNames)
      val resarray = rddres.collect()
      assert(1==1)
  }
  test("simple test"){
    //textScript
    val url = Resources.getResource("test.groovy")
    val scriptTexts = Resources.toString(url, Charsets.UTF_8)
    val lines = sc.makeRDD(Array("a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4"
    ,"a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4","a:2,v0:4,t:4"),2)
    //compute
    val res = lines.groovyCompute(scriptTexts)
    val resarray = res.collect()
    resarray.foreach(println)
    assert("a"==="a")
  }



}
