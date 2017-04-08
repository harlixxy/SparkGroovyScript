import io.unimatic.scripting.groovy.DataPoint

import java.text.SimpleDateFormat

//适用于rule3 国华内蒙达来东
require {
    devStatus is "风机主状态"
    scLevel is "刹车等级"
    alarm157 is "远程停机"
    alarm119 is "变桨电池检测"
    alarm153 is "风机维护"

    // 电网停机故障码
    alarm120 is "电网故障过频"
    alarm121 is "电压缺相"
    alarm122 is "三相相位偏差过大"
    alarm124 is "相电压低"
    alarm125 is "电网频率过高"
    alarm126 is "电网频率过低"
    alarm127 is "三相相位角偏离(120°)"
    alarm130 is "三相电流不对称"
    alarm132 is "变频器报电网故障"
    alarm161 is "相电压高"

    //天气停机状态码
    alarm25 is "瞬时功率过高紧停"
    alarm38 is "轮毂温度过热"
    alarm54 is "变频器并网超时"
    alarm55 is "变频器入口温度过热"
    alarm69 is "变频器温度过低"
    alarm81 is "箱式变压器过热停机"
    alarm82 is "600s平均功率过高"
    alarm91 is "空转时间过长"
    alarm92 is "塔筒共振时间过长"
    alarm93 is "功率过低等风"
    alarm94 is "并网时发电机转速过低"
    alarm140 is "箱变温度过高停机"
    alarm159 is "机舱温度过高停机"
    alarm162 is "户外温度过低停机"
    alarm163 is "户外温度过高停机"
    alarm169 is "风向偏差大于45度"
    alarm172 is "风速过高停机"

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

enum Status {
    GOOD_RUNNING(200), //	正常运行
    STANDBY(201),//	待机
    TEST_STOP(202),//	测试停机
    MAINTAINING_STOP(203),//	维护停机
    WEATHER_STOP(204),//	天气原因停机
    REMOTE_STOP(205),//	远程停机
    ALARM_RUNNING(400),//	报警运行
    ALARM_RUNNING_STANDBY(401),//	带报警待机
    LIMIT_LOAD_POWER(402),//	限负荷发电
    LIMIT_LOAD_STOP(206),//	限负荷停机
    TROUBLE_STOP(500),//	故障停机
    NETWORK_STOP(501),//	电网原因停机
    COMMUNICATE_STOP(502),//通讯中断
    UNKNOWN_STATUS(601)//未知状态

    int code

    Status(int code) {
        this.code = code
    }
}

def findStatus = { statusList, range ->
    if (!statusList.isEmpty()) {
        statusList = statusList.findAll { range.contains(it.key) }
    }
    statusList
}

def devStatusRule = { statusList, condition ->
    switch (condition) {
        case 0:
            findStatus(statusList, [1, 4])
            break
        case 1:
            findStatus(statusList, [2, 3, 7, 8, 4])
            break
        case 2:
            findStatus(statusList, [5, 6, 9, 4])
            break
        case 3:
            findStatus(statusList, [12, 4])
            break
        case 4:
            findStatus(statusList, [11, 4])
            break
        case 5:
            findStatus(statusList, [10, 4])
            break
        default:
            findStatus(statusList, [4])
    }
}

def scLevelRule = { statusList, condition ->
    switch (condition) {
        case 0:
            findStatus(statusList, [3, 4, 1, 8, 10, 11, 12])
            break
        case 1:
            findStatus(statusList, [1, 8, 3, 4, 2, 7])
            break
        case 2:
            findStatus(statusList, [1, 8, 3, 4, 5, 6])
            break
        case 3:
            findStatus(statusList, [1, 8, 3, 4, 9])
            break
        default:
            findStatus(statusList, [1, 8, 3, 4])
    }
}

def rule3ShareState = [1, 6, 8, 9, 10, 11, 12]
def rule3 = {metrics, range ->
    if (metrics.any { entry -> entry != null && entry.value == 1D }) range+rule3ShareState else rule3ShareState
}

task {
    def mp = [1 : Status.COMMUNICATE_STOP, 2: Status.WEATHER_STOP, 3: Status.NETWORK_STOP, 4: Status.MAINTAINING_STOP,
              5 : Status.REMOTE_STOP, 6: Status.LIMIT_LOAD_STOP, 7: Status.TEST_STOP,
              8 : Status.TROUBLE_STOP, 9: Status.MAINTAINING_STOP, 10: Status.GOOD_RUNNING,
              11: Status.ALARM_RUNNING, 12: Status.STANDBY]

    def network_alarms = [alarm120, alarm121, alarm122, alarm124, alarm125, alarm126, alarm127, alarm130, alarm132,
                          alarm161]
    def weather_alarms = [alarm25, alarm38, alarm54, alarm55, alarm69, alarm81, alarm82, alarm91, alarm92, alarm93,
                          alarm94, alarm140, alarm159, alarm162, alarm163, alarm169, alarm172]

    def filters = [[devStatus, devStatusRule], [scLevel, scLevelRule]]
    def filters2 = [[network_alarms, rule3, [3]],
                    [weather_alarms, rule3, [2]],
                    [[alarm153], rule3, [4]],
                    [[alarm157], rule3, [5]],
                    [[alarm119], rule3, [7]]]

    def commErr_metric = [fengsu, fengxang, cangwWd, yeYali]
    def isCommunicateStop = commErr_metric.any { entry -> entry != null && entry.value != 0D }

    //计算抽象状态
    for (f in filters)
        mp = f.get(1)(mp, f.get(0))

    def set = [] as Set
    for (f in filters2)
        set.addAll(f.get(1)(f.get(0), f.get(2)))
    mp = findStatus(mp, set);

    if (!mp.isEmpty())
    {
        virtualStatus = mp.get(mp.keySet().min()).code
    }
    else
    {
        if (!isCommunicateStop) {
            virtualStatus = Status.COMMUNICATE_STOP.code
        } else {
            virtualStatus = Status.UNKNOWN_STATUS.code
        }
    }

    //找到对应的时间戳，获取timestamp
    def time_items = [ fengsu, fengxang, cangwWd, yeYali, devStatus, scLevel,alarm157, alarm119,alarm153 ]
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
