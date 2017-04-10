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
//      s.put("GOOD_RUNNING(200)", "正常运行");
//      s.put("STANDBY(201)", "待机");
//      s.put("TEST_STOP(202)", "测试停机");
//      s.put("MAINTAINING_STOP(203)", "维护停机");
//      s.put("WEATHER_STOP(204)", "天气原因停机");
//      s.put("REMOTE_STOP(205)", "远程停机");
//      s.put("alarm_RUNNING(400)", "报警运行");
//      s.put("alarm_RUNNING_STANDBY(401)", "带报警待机");
//      s.put("LIMIT_LOAD_POWER(402)", "限负荷发电");
//      s.put("TROUBLE_STOP(500)", "故障停机");
//      s.put("NETWORK_STOP(501)", "电网原因停机");
//      s.put("COMMUNICATE_STOP(502)", "通讯中断");
//      s.put("UNKNOWN_STATUS(601)", "未知状态");

      //require 输入字段名称
      val requireNames = new util.HashMap[String,Object]()
      /**
        * 要注意，这里的顺序要一一对应
        */
//      requireNames.put("devStatus","devStatus")
//      requireNames.put("yggl","yggl")
//      requireNames.put("alm1","alm1")
//      requireNames.put("alm2","alm2")
//      requireNames.put("alm3","alm3")
//      requireNames.put("alm4","alm4")
//      requireNames.put("alm5","alm5")
//      requireNames.put("alm6","alm6")
//      requireNames.put("alm7","alm7")
//      requireNames.put("alm8","alm8")
//      requireNames.put("alm9","alm9")
//      requireNames.put("fengsu","fengsu")
//      requireNames.put("fengxang","fengxang")
//      requireNames.put("cangwWd","cangwWd")
//      requireNames.put("yeYali","yeYali")



      val rddres:RDD[String] = lines.groovyCompute(scriptTexts,s,requireNames)
      val resarray = rddres.collect()

      assert(1==1)

  }
  test("simple test"){
    //textScript
    val url = Resources.getResource("test.groovy")
    val scriptTexts = Resources.toString(url, Charsets.UTF_8)
    val lines = sc.makeRDD(Array(":2,v0:4,t:4"))
    //compute
    val res = lines.groovyCompute(scriptTexts)
    val resarray = res.collect()
    resarray.foreach(println)
    assert("a"==="a")
  }



}
