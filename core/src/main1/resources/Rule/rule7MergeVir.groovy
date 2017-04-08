import io.unimatic.scripting.groovy.DataPoint

//rule7 适用于 赶马路风场
enum Status {
    GOOD_RUNNING(200),//正常运行
    STANDBY(201),//待机
    MAINTAINING_STOP(203),//维护停机
    ALARM_RUNNING(400),//报警运行
    TROUBLE_STOP(500),//故障停机
    COMMUNICATE_STOP(502),//通讯中断
    UNKNOWN_STATUS(601)//状态非法
    int code

    Status(int code) {
        this.code = code;
    }
}

require {
    devStatus is "风机主状态"
    fengsu is "风速"
    fengxang is "风向"
    cangwWd is "舱外温度"
    yeYali is "液压压力"
}

status {
    lastDevStatus is "上一次风机主状态"
    lastFengsu is "上一次风速"
    lastFengxang is "上一次风向"
    lastCangwWd is "上一次舱外温度"
    lastYeYali is "上一次液压压力"

    lastStatus is "上一次状态"
}

onInit {
    lastDevStatus = null
    lastFengsu = null
    lastFengxang = null
    lastCangwWd = null
    lastYeYali = null

    lastStatus = null
}

time_align { nullable }

output {
    mergeVirStatus is "合并抽象状态"
}

def statusList = [0D: Status.COMMUNICATE_STOP.code, 1D: Status.TROUBLE_STOP.code, 2D: Status.MAINTAINING_STOP.code,
                  3D: Status.STANDBY.code, 4D: Status.ALARM_RUNNING.code, 5D: Status.GOOD_RUNNING.code]

def normalStatus = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态

task {
    //找到对应的时间戳，获取timestamp
    def time_items = [devStatus ,fengsu ,fengxang, cangwWd, yeYali]
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    def virtualStatus = null

    //假如风机当前风机主状态为空,取上一次的主状态
    devStatus = devStatus != null ? devStatus : lastDevStatus
    fengsu = fengsu != null ? fengsu : lastFengsu
    fengxang = fengxang != null ? fengxang : lastFengxang
    cangwWd = cangwWd != null ? cangwWd : lastCangwWd
    yeYali = yeYali != null ? yeYali : lastYeYali
    def isCommunicateStop = [fengsu, fengxang, cangwWd, yeYali].any { entry -> entry != null && entry.value != 0D }
    //计算抽象状态
    if (devStatus != null) {
        def con = (Double) devStatus
        def findStatus = statusList.find { it.key == con.value }
        if (findStatus == null) {
            virtualStatus = !isCommunicateStop ? Status.COMMUNICATE_STOP.code : Status.UNKNOWN_STATUS.code
        } else {
            virtualStatus = findStatus.value
        }
    } else {
        virtualStatus = !isCommunicateStop ? Status.COMMUNICATE_STOP.code : Status.UNKNOWN_STATUS.code
    }

    //给上一个状态赋值
    lastDevStatus = devStatus
    lastFengsu = fengsu
    lastFengxang = fengxang
    lastCangwWd = cangwWd
    lastYeYali = yeYali

    //判断不出状态,使用上一次的状态
    if (virtualStatus == null) virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    //合并状态码输出
    if (lastStatus == null || virtualStatus.value != lastStatus.value) mergeVirStatus = virtualStatus
    //对上一次状态赋值
    if (virtualStatus != null) lastStatus = new DataPoint((Long) timestamp, (Double) virtualStatus)
}