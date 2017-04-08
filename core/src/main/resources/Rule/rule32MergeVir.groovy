import io.unimatic.scripting.groovy.DataPoint

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

//def normalStatus = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态

task {
    def virtualStatus = null

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
    if (virtualStatus == null) virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    //合并状态码输出
    if (lastStatus == null || virtualStatus.value != lastStatus.value) mergeVirStatus = virtualStatus
    //对上一次状态赋值
    if (virtualStatus != null) lastStatus = new DataPoint((Long) timestamp, (Double) virtualStatus)
}
