import io.unimatic.scripting.groovy.DataPoint
import java.text.SimpleDateFormat

//适用于rule2华能干河口
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
        this.code = code;
    }

}

require {
    devStatus is "运行状态";
    stateAvaile is "可利用率状态";
    failcode is "实际停机条件"
    yggl is "有功功率"

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

def alarmCode = [4107D, 4110D, 4111D, 5004D, 7007D, 7010D, 7011D, 8008D, 8009D, 9004D, 9031D, 9032D, 9033D, 9037D, 9038D,
                 9039D, 10013D, 10015D, 11004D, 12012D, 12013D, 12014D, 12015D, 13009D, 13010D, 13011D, 13012D, 14009D,
                 14010D, 14011D, 14012D, 19001D]
def troubleStopCode = [6D, 7D, 8D, 9D, 10D, 11D, 12D, 120D, 121D, 122D, 123D, 130D, 131D, 132D, 133D, 140D, 141D, 142D,
                       143D, 132D, 150D, 151D, 152D, 160D, 161D, 162D, 163D, 180D, 181D, 182D, 183D, 184D, 1005D, 1006D,
                       1007D, 1008D, 1009D, 1010D, 5001D, 5002D, 5003D, 5005D, 5006D, 5007D, 6001D, 6002D, 6003D, 6004D,
                       6005D, 6006D, 6007D, 6008D, 6009D, 6010D, 6011D, 6012D, 6013D, 6014D, 6015D, 6016D, 6101D, 6102D,
                       6103D, 6104D, 6105D, 6106D, 6107D, 6108D, 6109D, 6110D, 7001D, 7002D, 7003D, 7004D, 7005D, 7006D,
                       7008D, 7009D, 7012D, 8001D, 8002D, 8003D, 8004D, 8005D, 8006D, 8007D, 8010D, 8011D, 9001D, 9002D,
                       9003D, 9005D, 9006D, 9007D, 9008D, 9009D, 9010D, 9011D, 9012D, 9013D, 9014D, 9015D, 9016D, 9018D,
                       9019D, 9020D, 9021D, 9022D, 9023D, 9024D, 9025D, 9026D, 9027D, 9028D, 9029D, 9030D, 9034D, 9035D,
                       9036D, 9040D, 9041D, 9042D, 10001D, 10002D, 10003D, 10004D, 10005D, 10006D, 10007D, 10008D, 10009D,
                       10010D, 10011D, 10012D, 10014D, 10016D, 10017D, 11001D, 11002D, 11003D, 12001D, 12002D, 12003D,
                       12004D, 12005D, 12006D, 12007D, 12008D, 12009D, 12010D, 12011D, 12016D, 13001D, 13002D, 13003D,
                       13004D, 13005D, 13006D, 13007D, 13008D, 13013D, 13014D, 13015D, 13016D, 14001D, 14002D, 14003D,
                       14004D, 14005D, 14006D, 14007D, 14008D, 14013D, 14014D, 14015D, 17001D, 17002D, 19005D]
def networkStopCode = [1D, 2D, 4D, 5D, 4002D, 4003D, 4004D, 4005D, 4006D, 4007D, 4008D, 4009D, 4010D, 4011D, 4014D,
                       4015D, 4020D, 4099D, 4101D, 4102D, 4103D, 4104D, 4105D, 4106D, 4108D, 4109D, 4112D, 4113D, 4119D]
def testMaintainingCode = 15003D
def maintainingStopCode = [1001D, 1002D, 1003D, 1004D, 1011D, 1012D, 1013D, 1014D, 3001D, 3002D, 3003D, 3100D, 15004D,
                           2101D, 2111D, 2121D, 2131D, 2141D, 2151D, 2161D, 2171D, 2181D, 2191D, 2201D, 2211D, 2221D,
                           2231D, 18002D, 18003D]
def remoteStopCode = [3100D, 18001D]
def weatherStopCode = [15001D, 15002D, 16001D, 16002D, 16003D, 16004D, 19002D, 19003D, 19004D, 20001D]
def limitLoadPowerCode = 18004D

def remove = { statusList, rang ->
    for (item in rang) {
        statusList.remove(item)
    }
    statusList
}

def rule1 = { statusList, condition ->
    switch (condition) {
        case 0: remove(statusList, [1, 2, 10, 11, 12])
            break
        case 1:
            remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 12])
            break
        case 2: remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 10, 11])
            break
        case 3: remove(statusList, [3, 4, 5, 6, 7, 8, 12])
            break
        default: remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12])
    }
    statusList
}

def rule2 = { statusList, condition ->
    switch (condition) {
        case 0: remove(statusList, [3, 4, 5, 6, 7, 9])
            break
        case 1: remove(statusList, [1, 2, 3, 5, 6, 7, 9, 10, 11])
            break
        case 2: remove(statusList, [1, 2, 3, 4, 5, 6, 9, 10, 11])
            break
        case 3: remove(statusList, [1, 2, 3, 4, 7, 10, 11])
            break
        case 4: remove(statusList, [1, 2, 4, 5, 6, 7, 9, 10, 11])
            break
        default: remove(statusList, [1, 2, 3, 4, 5, 6, 7, 9, 10, 11])
    }
    statusList
}

def rule3 = { statusList, condition ->
    def con = (Double) condition
    if (con != null) {
        switch (con.value) {
            case 0D:
                if (statusList.containsKey(1))
                    remove(statusList, [2, 3, 4, 5, 6, 7, 8, 9, 10, 11])
                else
                    remove(statusList, [2, 3, 4, 5, 6, 7, 8, 9, 11])
                break
            case alarmCode: remove(statusList, [1, 3, 4, 5, 6, 7, 8, 9, 10])
                break
            case troubleStopCode: remove(statusList, [1, 2, 4, 5, 6, 7, 8, 9, 11])
                break
            case networkStopCode: remove(statusList, [1, 2, 3, 5, 6, 7, 8, 9, 11])
                break
            case testMaintainingCode: remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 11])
                break
            case maintainingStopCode: remove(statusList, [1, 2, 3, 4, 6, 7, 8, 9, 11])
                break
            case remoteStopCode: remove(statusList, [1, 2, 3, 4, 5, 7, 8, 9, 11])
                break
            case weatherStopCode: remove(statusList, [1, 2, 3, 4, 5, 6, 8, 9, 11])
                break
            case limitLoadPowerCode: remove(statusList, [1, 2, 3, 4, 5, 6, 7, 9, 11])
                break
            default:
                remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 9, 11])
        }
    } else {
        remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 9, 11])
    }
    statusList
}

def rule4 = { statusList, condition ->
    if (condition != null && condition.value <= 0 && (statusList.containsKey(10) || statusList.containsKey(11))) {
        remove(statusList, [1, 2])
    } else if (condition != null && condition.value > 0) {
        remove(statusList, [10, 11])
    }
    statusList
}

//1	正常运行,2	报警运行,3	故障停机,4	电网原因停机,5	维护停机,6	远程停机,7	天气原因停机,8	限负荷停机,
// 9	测试停机,10	待机,11	带报警待机,12	正常运行,13	通讯中断
task {
    def mp = [1 : Status.GOOD_RUNNING, 2: Status.ALARM_RUNNING, 3: Status.TROUBLE_STOP, 4: Status.NETWORK_STOP,
              5 : Status.MAINTAINING_STOP, 6: Status.REMOTE_STOP, 7: Status.WEATHER_STOP,
              8 : Status.LIMIT_LOAD_STOP, 9: Status.TEST_STOP, 10: Status.STANDBY,
              11: Status.ALARM_RUNNING_STANDBY, 12: Status.GOOD_RUNNING]

    def commErr_metric = [fengsu, fengxang, cangwWd, yeYali]
    def isCommunicateStop = commErr_metric.any { entry -> entry != null && entry.value != 0D }

    def time_items = [devStatus, stateAvaile, failcode, yggl, fengsu, fengxang, cangwWd, yeYali]
    //获取timestamp
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    def filters = [[devStatus, rule1], [stateAvaile, rule2], [failcode, rule3], [yggl, rule4]]

    //计算抽象状态
    for (f in filters) {
        def temp = [:]
        temp.putAll(mp)
        mp = f.get(1)(mp, f.get(0))
        if (mp.size() > 0) {
            continue
        } else {
            mp = temp
            break
        }
    }

    if (mp.containsKey(9) && (stateAvaile == null || stateAvaile.value != 3D
            || failcode == null || failcode.value != 15003D)) {
        mp.remove(9)
    }
    if (mp.size() > 0) {
        virtualStatus = mp.get(mp.keySet().min()).code
    } else {
        if (!isCommunicateStop) {
            virtualStatus = Status.COMMUNICATE_STOP.code
        } else if (devStatus != null || stateAvaile != null || failcode != null) {
            virtualStatus = Status.UNKNOWN_STATUS.code
        }
    }

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