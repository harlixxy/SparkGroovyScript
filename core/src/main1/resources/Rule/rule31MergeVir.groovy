import io.unimatic.scripting.groovy.DataPoint

//适用于 rule3 山西偏关
//（状态码解析规则修改为alarm1~8 31位）
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
    lastStatus is "上一次状态"
}

onInit {
    lastStatus = null
}

time_align { nullable }

output {
    mergeVirStatus is "合并抽象状态"
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

def rule3ShareState = [1, 8, 9, 10, 11, 12]
def rule3 = {metrics, range ->
    if (metrics.any { entry -> entry != null && entry.value == 1D }) range+rule3ShareState else rule3ShareState
}

//def normalStatus = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态

task {
    def virtualStatus = null

    def mp = [1 : Status.COMMUNICATE_STOP, 2: Status.WEATHER_STOP, 3: Status.NETWORK_STOP, 4: Status.MAINTAINING_STOP,
              5 : Status.REMOTE_STOP, 6: Status.LIMIT_LOAD_STOP, 7: Status.TEST_STOP,
              8 : Status.TROUBLE_STOP, 9: Status.MAINTAINING_STOP, 10: Status.GOOD_RUNNING,
              11: Status.ALARM_RUNNING, 12: Status.STANDBY]

    def alarms = [zAlarm1, zAlarm2, zAlarm3, zAlarm4, zAlarm5, zAlarm6, zAlarm7, zAlarm8]

    //对比状态码（若符合条件，则返回true,不符合返回false）
    def compareCodeInAlarm = { items ->
        for (item in items) {
            def res = ((int) item - 1).intdiv(31)
            def remr = ((int) item).mod(31)
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
    def time_items = [devStatus, scLevel, fengsu, fengxang, cangwWd, yeYali]
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    //判断不出状态,使用上一次的状态
    if (virtualStatus == null) virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    //合并状态码输出
    if (lastStatus == null || virtualStatus.value != lastStatus.value) mergeVirStatus = virtualStatus
    //对上一次状态赋值
    if (virtualStatus != null) lastStatus = new DataPoint((Long) timestamp, (Double) virtualStatus)
}