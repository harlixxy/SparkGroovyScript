import io.unimatic.scripting.groovy.DataPoint


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
    lastStatus is "上一次状态"
}

onInit {
    lastStatus = null
}

time_align { nullable }

output {
    mergeVirStatus is "合并抽象状态"
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

//def normalStatus = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态

task {
    def virtualStatus = null

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
    if (virtualStatus == null) virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    //合并状态码输出
    if (lastStatus == null || virtualStatus.value != lastStatus.value) mergeVirStatus = virtualStatus
    //对上一次状态赋值
    if (virtualStatus != null) lastStatus = new DataPoint((Long) timestamp, (Double) virtualStatus)
}