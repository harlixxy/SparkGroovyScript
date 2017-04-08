import io.unimatic.scripting.groovy.DataPoint
import java.text.SimpleDateFormat

//适用于 rule3 山西偏关
//（状态码解析规则修改为alarm1~8 31位）
//（增加合并抽象状态，调整各种时间的规则）
//（增加 相邻两点位大于等于5分钟，则不累加时长 的控制）
require {
    devStatus is "风机主状态"
    scLevel is "刹车等级"

    zAlarm1 is "zAlarm1"
    zAlarm2 is "zAlarm2"
    zAlarm3 is "zAlarm3"
    zAlarm4 is "zAlarm4"
    zAlarm5 is "zAlarm5"
    zAlarm6 is "zAlarm6"
    zAlarm7 is "zAlarm7"
    zAlarm8 is "zAlarm8"

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

def scLeveRule = { statusList, condition ->
    switch (condition) {
        case 0:
            findStatus(statusList, [1, 8, 3, 4, 10, 11, 12])
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

task {
    def mp = [1 : Status.COMMUNICATE_STOP, 2: Status.WEATHER_STOP, 3: Status.NETWORK_STOP, 4: Status.MAINTAINING_STOP,
              5 : Status.REMOTE_STOP, 6: Status.LIMIT_LOAD_STOP, 7: Status.TEST_STOP,
              8 : Status.TROUBLE_STOP, 9: Status.MAINTAINING_STOP, 10: Status.GOOD_RUNNING,
              11: Status.ALARM_RUNNING, 12: Status.STANDBY]

    def alarms = [zAlarm1, zAlarm2, zAlarm3, zAlarm4, zAlarm5, zAlarm6, zAlarm7, zAlarm8]

    //对比状态码（若符合条件，则返回true,不符合返回false）
    def compareCodeInAlarm = { items ->
        for (item in items) {
            def res = ((int) item).intdiv(31)
            def remr = ((int) item).mod(31)  //31
            Long temp = 1L << remr
            if (res < alarms.size()) {
                def alarm = alarms[res]
                if (alarm != null && (alarm & temp) == temp) return true
            }
        }
        return false
    }

    def ignStatusCode = [1, 8, 9, 10, 11, 12]  //不用判断状态码的抽象状态序号

    def alarmFilter = { metrics, range ->
        if (compareCodeInAlarm(metrics)) {
            range + ignStatusCode
        } else {
            ignStatusCode
        }
    }

    //电网停机 故障码
    def network_alarms = [27, 28, 29, 30, 31, 32, 33, 36, 38, 76]

    //天气原因停机 故障码
    def weather_alarms = [12, 45, 74, 77, 78, 81, 83, 86, 132, 148,
                          149, 162, 173, 174, 194, 208, 209, 210]

    def statusCodeAry = [[network_alarms, [3]],
                         [weather_alarms, [2]],
                         [[64], [4]],
                         [[72], [5]],
                         [[195], [6]],
                         [[26], [7]]]

    def statusCodeRule = { statusList, rule2 ->
        def set = [] as Set
        for (f in rule2) {
            set.addAll(alarmFilter(f.get(0), f.get(1)))
        }
        findStatus(mp, set);
    }

    //开始执行
    mp = devStatusRule(mp, devStatus)       //过滤 devStatus
    mp = statusCodeRule(mp, statusCodeAry)    //过滤 状态码
    mp = scLeveRule(mp, scLevel)            //过滤 scLevel

    //判断通讯中断
    def commErr_metric = [fengsu, fengxang, cangwWd, yeYali]
    def isCommunicateStop = commErr_metric.any { entry -> entry != null && entry.value != 0D }

    if (!mp.isEmpty()) {
        virtualStatus = mp.get(mp.keySet().min()).code
    } else {
        if (!isCommunicateStop) {
            virtualStatus = Status.COMMUNICATE_STOP.code
        } else {
            virtualStatus = Status.UNKNOWN_STATUS.code
        }
    }

    //找到对应的时间戳，获取timestamp
    def time_items = [devStatus, scLevel, fengsu, fengxang, cangwWd, yeYali, zAlarm1, zAlarm2, zAlarm3, zAlarm4, zAlarm5, zAlarm6, zAlarm7, zAlarm8]
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