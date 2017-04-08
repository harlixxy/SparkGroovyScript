import io.unimatic.scripting.groovy.DataPoint
import java.text.SimpleDateFormat

//适用于rule2左云一三期
//(增加性能分析 温度功率部分)
enum Status {
    GOOD_RUNNING(200), //	正常运行
    STANDBY(201),//	待机
    TEST_STOP(202),//	测试停机
    MAINTAINING_STOP(203),  //	维护停机
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
    devStatus3 is "用户给定状态";
    yggl is "有功功率"

    fengsu is "风速";
    fengxang is "风向";
    cangwWd is "舱外温度";
    yeYali is "液压压力";

    clxYouWd is "齿轮箱油温"  //供性能分析
}

status {
    lastDevStatus is "上一次运行状态";
    lastStateAvaile is "上一次可利用率状态";
    lastFailcode is "上一次实际停机条件"
    lastDevStatus3 is "上一次用户给定状态";
    lastYggl is "上一次有功功率"

    lastStatus is "上一次状态"
    lastTime is "上一次时间戳"

    firstCommonStateTimestamp is "第一次正常状态时间戳"
    firstAlarmStateTimestamp is "第一次报警状态时间戳"
    firstCommonStateOrAlarmStateTimestamp is "第一次正常或报警状态时间戳"
}

onInit {
    lastDevStatus = null
    lastStateAvaile = null
    lastFailcode = null
    lastDevStatus3 = null
    lastYggl = null

    lastStatus = null
    lastTime = null

    firstCommonStateTimestamp = null
    firstAlarmStateTimestamp = null
    firstCommonStateOrAlarmStateTimestamp = null
}

time_align { nullable }

output {
    outYgglForCommonState is "YG_GLForCommonState"
    outCangwWdForCommonState is "CANGW_WDForCommonState"
    outClxYouWdForCommonState is "CLX_YOU_WDForCommonState"

    outYgglForAlarmState is "YG_GLForAlarmState"
    outCangwWdForAlarmState is "CANGW_WDForAlarmState"
    outClxYouWdForAlarmState is "CLX_YOU_WDForAlarmState"

    outYggl is "YG_GL"
    outCangwWd is "CANGW_WD"
    outClxYouWd is "CLX_YOU_WD"
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
                           2231D, 18002D, 18003D, 3004D]
def remoteStopCode = 18001D
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
        case 0: remove(statusList, [1, 2, 9, 10, 11, 12])
            break
        case 1:
            remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 9, 12])
            break
        case 2: remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 10, 11])
            break
        case 3: remove(statusList, [3, 4, 5, 6, 7, 8, 9, 12])
            break
        default: remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12])
    }
    statusList
}
def rule2 = { statusList, condition ->
    switch (condition) {
        case 0: remove(statusList, [3, 4, 5, 6, 7, 8])
            break
        case 1: remove(statusList, [1, 2, 3, 4, 6, 7, 8, 10, 11])
            break
        case 2: remove(statusList, [1, 2, 5, 6, 7, 8, 10, 11])
            break
        case 3: remove(statusList, [1, 2, 3, 4, 5, 6, 10, 11])
            break
        case 4: remove(statusList, [1, 2, 3, 4, 5, 7, 8, 10, 11])
            break
        default: remove(statusList, [1, 2, 3, 4, 5, 6, 7, 8, 10, 11])

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
    switch (condition) {
        case 0: remove(statusList, [6, 8, 9])
            break
        case 1: remove(statusList, [7, 9, 10, 11])
            break
        case 3: remove(statusList, [6, 7, 8, 10, 11])
            break
        default: remove(statusList, [6, 7, 8, 9, 10, 11])
    }
    statusList
}

def rule5 = { statusList, condition ->
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
    def virtualStatus = null

    def mp = [1 : Status.GOOD_RUNNING, 2: Status.ALARM_RUNNING, 3: Status.TROUBLE_STOP, 4: Status.NETWORK_STOP,
              5 : Status.MAINTAINING_STOP, 6: Status.REMOTE_STOP, 7: Status.WEATHER_STOP,
              8 : Status.LIMIT_LOAD_STOP, 9: Status.TEST_STOP, 10: Status.STANDBY,
              11: Status.ALARM_RUNNING_STANDBY, 12: Status.GOOD_RUNNING]

    def commErr_metric = [fengsu, fengxang, cangwWd, yeYali]
    def isCommunicateStop = commErr_metric.any { entry -> entry != null && entry.value != 0D }

    def time_items = [devStatus, devStatus3, failcode, stateAvaile, yggl, fengsu, fengxang, cangwWd, yeYali]

    //假如风机重要数据为空,取上一次的数据
    if (devStatus == null) devStatus = lastDevStatus else lastDevStatus = devStatus
    if (devStatus3 == null) devStatus3 = lastDevStatus3 else lastDevStatus3 = devStatus3
    if (failcode == null) failcode = lastFailcode else lastFailcode = failcode
    if (stateAvaile == null) stateAvaile = lastStateAvaile else lastStateAvaile = stateAvaile
    if (yggl == null) yggl = lastYggl else lastYggl = yggl

    //获取timestamp
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    def filters = [[devStatus, rule1], [devStatus3, rule2], [failcode, rule3], [stateAvaile, rule4], [yggl, rule5]]

    //计算抽象状态
    def isFatchLastRule = false   //是否因取不到值，而取上一次规则的记录
    def dealCount = 1  //处理次数
    for (f in filters) {
        def temp = [:]
        temp.putAll(mp)
        mp = f.get(1)(mp, f.get(0))
        if (mp.size() > 0) {
            dealCount++
            continue
        } else {  //没记录则优先取上一次的
            mp = temp
            isFatchLastRule = true
            break
        }
    }

    if (dealCount == 1) {    //若处理次数为第一次就结束了，则直接未知
        virtualStatus = Status.UNKNOWN_STATUS.code
    } else {
        if (!isFatchLastRule) {  //直接取值
            virtualStatus = mp.get(mp.keySet().min()).code
        } else {  //找不到取上一次规则的值
            if (!isCommunicateStop) {
                virtualStatus = Status.COMMUNICATE_STOP.code
            } else if (mp.size() > 0) {
                virtualStatus = mp.get(mp.keySet().min()).code
            } else {
                virtualStatus = Status.UNKNOWN_STATUS.code
            }
        }
    }

    //判断不出状态,使用上一次的状态
    if (virtualStatus == null) {
        virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    }

    /***分割线***/

    //计算是否需要输出
    def isNeedOut = { firstTime ->
        if (firstTime == null) {
            return false
        }
        if ((timestamp - firstTime) / 60 >= 30) {
            return true
        } else {
            return false
        }
    }

    //定义基础变量
    def commonStateAry = [Status.GOOD_RUNNING.code , Status.LIMIT_LOAD_POWER.code ]
    def alarmStateAry = [Status.ALARM_RUNNING.code]
    def commonStateOrAlarmStateAry = [Status.GOOD_RUNNING.code , Status.LIMIT_LOAD_POWER.code, Status.ALARM_RUNNING.code ]

    //先考虑正常运行
    def isOutCommonState = false
    if (lastStatus != null && ((int)lastStatus.value) in commonStateAry
            && ((int)virtualStatus) in commonStateAry ) {
        isOutCommonState = isNeedOut(firstCommonStateTimestamp)
    } else {
        firstCommonStateTimestamp = (((int)virtualStatus) in commonStateAry) ? timestamp : null
    }

    //再考虑报警运行
    def isOutAlarmState = false
    if (lastStatus != null && ((int)lastStatus.value) in alarmStateAry
            && ((int)virtualStatus) in alarmStateAry) {
        isOutAlarmState = isNeedOut(firstAlarmStateTimestamp)
    } else {
        firstAlarmStateTimestamp = (((int)virtualStatus) in alarmStateAry) ? timestamp : null
    }

    //最后考虑正常运行或报警运行
    def isOutCommonStateOrAlarmState = false
    if (lastStatus != null && ((int)lastStatus.value) in commonStateOrAlarmStateAry
            && ((int)virtualStatus) in commonStateOrAlarmStateAry) {
        isOutCommonStateOrAlarmState = isNeedOut(firstCommonStateOrAlarmStateTimestamp)
    } else {
        firstCommonStateOrAlarmStateTimestamp = (((int)virtualStatus) in commonStateOrAlarmStateAry) ? timestamp : null
    }

    //开始根据输出进行计算
    if (yggl != null) {
        if (cangwWd != null && yggl.timestamp == cangwWd.timestamp) {
            if (isOutCommonState) {
                outYgglForCommonState = yggl.value
                outCangwWdForCommonState = cangwWd.value
            }
            if (isOutAlarmState) {
                outYgglForAlarmState = yggl.value
                outCangwWdForAlarmState = cangwWd.value
            }
            if (isOutCommonStateOrAlarmState) {
                outYggl = yggl.value
                outCangwWd = cangwWd.value
            }
        }

        if (clxYouWd != null && yggl.timestamp == clxYouWd.timestamp) {
            if (isOutCommonState) {
                outYgglForCommonState = yggl.value
                outClxYouWdForCommonState = clxYouWd.value
            }
            if (isOutAlarmState) {
                outYgglForAlarmState = yggl.value
                outClxYouWdForAlarmState = clxYouWd.value
            }
            if (isOutCommonStateOrAlarmState) {
                outYggl = yggl.value
                outClxYouWd = clxYouWd.value
            }
        }
    }

    //重置相关状态
    def ResetStates = { vs ->
        lastTime = timestamp
        if (vs != null) {
            lastStatus = new DataPoint((Long) timestamp, (Double) vs)
        }
    }(virtualStatus)
}