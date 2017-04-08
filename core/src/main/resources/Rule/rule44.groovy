import io.unimatic.scripting.groovy.DataPoint

import java.text.SimpleDateFormat

//适用于rule4大庆肇源新龙顺德
enum Status {
    GOOD_RUNNING(200),//正常运行
    STANDBY(201),//待机
    TEST_STOP(202),//测试停机
    MAINTAINING_STOP(203),//维护停机
    WEATHER_STOP(204),//天气原因停机
    REMOTE_STOP(205),//远程停机
    ALARM_RUNNING(400),//报警运行
    ALARM_RUNNING_STANDBY(401),//带报警待机
    LIMIT_LOAD_POWER(402),//限负荷发电
    LIMIT_LOAD_STOP(206),//限负荷停机
    TROUBLE_STOP(500),//故障停机
    NETWORK_STOP(501),//电网原因停机
    COMMUNICATE_STOP(502),//通讯中断
    UNKNOWN_STATUS(601),//状态非法
    STOP(602)//停机
    int code

    Status(int code) {
        this.code = code;
    }
}

require {
    stop is "停机"
    running is "发电"
    start is "启动"
    commErr is "通信故障"

    mStop is "手动停机"
    fail is "故障停机"
    qStop is "快速停机"
    eStop is "紧急停机"
    weaStop is "气象条件停机"
    gridErr is "电网故障"
    whStop is "维护"
    ppmStop is "限负荷停机"
    alarm is "报警"
    ppmFd is "限负荷发电"

    fengsu is "风速";
    fengxang is "风向";
    cangwWd is "舱外温度";
    yeYali is "液压压力";
}

status {
    lastday is "上一天"
    yxsj_dsum is "日运行时间sum"
    gzsj_dsum is "日故障时间sum"
    gztjcs_dsum is "日故障停机次数sum"

    lastmonth is "上一月"
    yxsj_msum is "月运行时间sum"
    gzsj_msum is "月故障时间sum"
    gztjcs_msum is "月故障停机次数sum"

    lastyear is "上一年"
    yxsj_ysum is "年运行时间sum"
    gzsj_ysum is "年故障时间sum"
    gztjcs_ysum is "年故障停机次数sum"

    lastStatus is "上一次状态"
    lastgzStatus is "上一次故障状态"
    lastTime is "上一次时间戳"
}

onInit {
    lastday = null
    yxsj_dsum = 0D
    gzsj_dsum = 0D
    gztjcs_dsum = 0D

    lastmonth = null
    yxsj_msum = 0D
    gzsj_msum = 0D
    gztjcs_msum = 0D

    lastyear = null
    yxsj_ysum = 0D
    gzsj_ysum = 0D
    gztjcs_ysum = 0D

    lastStatus = null
    lastgzStatus = null
    lastTime = 0D
}

time_align { nullable }

output {
    virtualStatus is "抽象状态"
    mergeVirStatus is "合并抽象状态"
    ryxsj is "日运行时间"
    yyxsj is "月运行时间"
    nyxsj is "年运行时间"
    rgztjcs is "日故障停机次数"
    ygztjcs is "月故障停机次数"
    ngztjcs is "年故障停机次数"
    rgztjsj is "日故障停机时间"
    ygztjsj is "月故障停机时间"
    ngztjsj is "年故障停机时间"
}

def rule1 = { statusList, condition, metrics, range ->
    def result
    if (condition) {
        for (item in metrics) {
            if (item != null && item.value == 1D) result = statusList.findAll { range.contains(it.key) }
        }
    }
    return result == null || result.isEmpty() ? statusList : result
}

def rule2 = { statusList, necessary, nonobligatory, range1, range2 ->
    def result
    if (necessary) {
        if (nonobligatory) result = statusList.findAll { range1.contains(it.key) }
        else result = statusList.findAll { range2.contains(it.key) }
    }
    return result == null || result.isEmpty() ? statusList : result
}

def hasValue = { metric ->
    return metric != null && metric.value == 1D
}

task {
    def mp = [1 : Status.GOOD_RUNNING, 2: Status.ALARM_RUNNING, 3: Status.COMMUNICATE_STOP, 4: Status.COMMUNICATE_STOP,
              5 : Status.LIMIT_LOAD_POWER, 6: Status.STANDBY, 7: Status.ALARM_RUNNING_STANDBY,
              8 : Status.MAINTAINING_STOP, 9: Status.WEATHER_STOP, 10: Status.TROUBLE_STOP,
              11: Status.TROUBLE_STOP, 12: Status.TROUBLE_STOP, 13: Status.NETWORK_STOP,
              14: Status.LIMIT_LOAD_STOP, 15: Status.LIMIT_LOAD_STOP, 16: Status.MAINTAINING_STOP,
              17: Status.MAINTAINING_STOP, 18: Status.MAINTAINING_STOP, 19: Status.MAINTAINING_STOP,
              20: Status.MAINTAINING_STOP, 21: Status.WEATHER_STOP, 22: Status.STOP, 23: Status.UNKNOWN_STATUS]

    def trouble_stop_metrics = [fail, qStop, eStop]
    def commErr_metric = [fengsu, fengxang, cangwWd, yeYali]

    def mptemp = [:]
    if (hasValue(running)) {
        if (hasValue(alarm))
            mptemp.putAll([2: Status.ALARM_RUNNING])
        else if (hasValue(ppmFd))
            mptemp.putAll([5: Status.LIMIT_LOAD_POWER])
        else
            mptemp.putAll([1: Status.GOOD_RUNNING])
    }
    if (hasValue(commErr) || !commErr_metric.any { entry -> entry != null && entry.value != 0 }) {
        mptemp.putAll([3: Status.COMMUNICATE_STOP, 4: Status.COMMUNICATE_STOP])
    }
    mp = !mptemp.isEmpty() ? mptemp : mp
    mp = rule2(mp, start != null && start.value == 1D, alarm != null && alarm.value == 1D, [7], [6])

    mp = rule1(mp, stop != null && stop.value == 1D, [mStop], [8])
    mp = rule1(mp, stop != null && stop.value == 1D, [weaStop], [9])
    mp = rule1(mp, stop != null && stop.value == 1D, trouble_stop_metrics, [10, 11, 12])
    mp = rule1(mp, stop != null && stop.value == 1D, [gridErr], [13])
    mp = rule1(mp, stop != null && stop.value == 1D, [ppmStop], [14])
    mp = rule1(mp, stop != null && stop.value == 1D, [whStop], [16])
    mp = rule2(mp, stop != null && stop.value == 1D, fengsu != null && (fengsu.value <= 4D || fengsu.value >= 20D), [21], [22])

    if (!mp.isEmpty())
        virtualStatus = mp.get(mp.keySet().min()).code

    //找到对应的时间戳，获取timestamp
    def time_items = [ fengsu , fengxang ,  cangwWd ,  yeYali ,stop ,  running , start , commErr , mStop , fail ,
                       qStop , eStop, gridErr ]
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    //判断不出状态,使用上一次的状态
    if (virtualStatus == null) {
        virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    }

    //计算合并抽象状态
    def CalMergeVirStatus = { virtualStatus ->
        if (lastStatus == null || virtualStatus.value != lastStatus.value)
            mergeVirStatus = virtualStatus
    }(virtualStatus)

    //计算各类时间、次数
    def CalAllKindsTimes = { virtualStatus, timeStamp ->
        def normalStatus = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态

        //需要计算
        def isNeedAdd = {
            if ((timestamp - lastTime) / 60 < 5) {
                return true
            } else {
                return false
            }
        }()

        //计算运行时间
        if (lastStatus != null) {
            if (lastStatus.value in normalStatus && lastTime != null
                    && isNeedAdd) {  //相邻两点位大于等于5分钟，则不累加时长
                yxsj_dsum += (timestamp - lastStatus.timestamp)
            }
        }

        //计算故障停机时间和次数
        if (lastgzStatus != null && lastgzStatus.value == 500D) {
            if (lastStatus.value == 500D && lastTime != null
                    && isNeedAdd) {  //若上一次的状态也是500，才进行累加时间。若是其他的，则不累加
                //相邻两点位大于等于5分钟，则不累加时长
                gzsj_dsum += (timestamp - lastStatus.timestamp)
            }
            if (virtualStatus in normalStatus) { //如果这次正常，则纳入统计次数，并且清除上次故障状态
                gztjcs_dsum = gztjcs_dsum + 1
                lastgzStatus = null
            }
        }

        def today = null
        def month = null
        def year = null

        if (virtualStatus != null && timestamp > 0) {
            def date = new Date(timestamp * 1000)
            def dayformat = new SimpleDateFormat("yyyy-MM-dd");
            def monthformat = new SimpleDateFormat("yyyy-MM");
            def yearformat = new SimpleDateFormat("yyyy");
            today = dayformat.format(date)
            month = monthformat.format(date)
            year = yearformat.format(date)
        }


        if (lastday == null && today != null) {
            lastday = today
            lastmonth = month
            lastyear = year
        }

        def curdatestamp = null;  //当日时间正点

        //日统计输出
        if (today != lastday && lastday != null) {
            //用于计算日故障总时间 当前越过的时间戳 (当前日总时间 -当天越过0点后的时间)
            def date = new Date(timestamp * 1000)
            def dayformat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cal = Calendar.getInstance();
            cal.setTime(dayformat.parse(today));
            curdatestamp = cal.getTimeInMillis() / 1000

            //println "跨天输出"

            //计算运行时间
            if (isNeedAdd && lastStatus.value in normalStatus) {
                ryxsj = new DataPoint((Long) lastTime,
                        (double) ((yxsj_dsum - (timestamp - curdatestamp)) / 3600))
                yxsj_msum += yxsj_dsum - (timestamp - curdatestamp)   //减去超时的
                yxsj_dsum = timestamp - curdatestamp
            } else {
                ryxsj = new DataPoint((Long) lastTime,
                        (double) ((yxsj_dsum) / 3600))
                yxsj_msum += yxsj_dsum
                yxsj_dsum = 0D
            }

            //println(yxsj_msum)  //打印输出

            //计算故障时间
            if (isNeedAdd && lastStatus.value == 500D) {
                rgztjsj = new DataPoint((Long) lastTime,
                        (double) ((gzsj_dsum - (timestamp - curdatestamp)) / 3600))
                gzsj_msum += gzsj_dsum - (timestamp - curdatestamp)   //减去超时的
                gzsj_dsum = timestamp - curdatestamp   //供计算下次的
            } else {
                rgztjsj = new DataPoint((Long) lastTime,
                        (double) ((gzsj_dsum) / 3600))
                gzsj_msum += gzsj_dsum
                gzsj_dsum = 0D
            }

            //输出停机次数
            rgztjcs = new DataPoint((Long) lastTime, (double) gztjcs_dsum)
            gztjcs_msum += gztjcs_dsum

            lastday = today
            gztjcs_dsum = 0D
        }

        //月统计输出
        if (lastmonth != month && lastmonth != null) {
            yyxsj = new DataPoint((Long) lastTime, (Double) (yxsj_msum / 3600))
            yxsj_ysum += yxsj_msum
            yxsj_msum = 0D

            ygztjsj = new DataPoint((Long) lastTime, (Double) (gzsj_msum / 3600))
            gzsj_ysum += gzsj_msum
            gzsj_msum = 0D

            ygztjcs = new DataPoint((Long) lastTime, (Double) gztjcs_msum)
            gztjcs_ysum += gztjcs_msum;
            gztjcs_msum = 0D

            lastmonth = month
        }

        //年统计输出
        if (lastyear != year && lastyear != null) {
            nyxsj = new DataPoint((Long) lastTime, (Double) (yxsj_ysum / 3600))
            yxsj_ysum = 0D

            ngztjsj = new DataPoint((Long) lastTime, (Double) (gzsj_ysum / 3600))
            gzsj_ysum = 0D

            ngztjcs = new DataPoint((Long) lastTime, (Double) gztjcs_ysum)
            gztjcs_ysum = 0D

            lastyear = year
        }
        lastTime = timestamp;
    }(virtualStatus, timestamp)

    //重置相关状态
    def ResetStates = { virtualStatus ->
        if (virtualStatus != null) {
            lastStatus = new DataPoint((Long) timestamp, (Double) virtualStatus)
            if (virtualStatus == 500D) {
                lastgzStatus = new DataPoint((Long) timestamp, (Double) virtualStatus)
            }
        }
    }(virtualStatus)
}