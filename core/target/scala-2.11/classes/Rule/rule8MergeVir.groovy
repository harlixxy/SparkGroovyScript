import io.unimatic.scripting.groovy.DataPoint

//适用于rule8 适用于国华新巴尔虎左旗、右旗、国华陈巴尔虎旗
enum Status {
    GOOD_RUNNING(200), //正常运行
    STANDBY(201),//待机
    TEST_STOP(202),//测试停机
    MAINTAINING_STOP(203),//维护停机
    WEATHER_STOP(204),//天气原因停机
    REMOTE_STOP(205),//远程停机
    alarm_RUNNING(400),//报警运行
    alarm_RUNNING_STANDBY(401),//带报警待机
    LIMIT_LOAD_POWER(402),//限负荷发电
    TROUBLE_STOP(500),//故障停机
    NETWORK_STOP(501),//电网原因停机
    COMMUNICATE_STOP(502),//通讯中断
    UNKNOWN_STATUS(601)//未知状态
    int code

    Status(int code) {
        this.code = code;
    }
}

require {
    devStatus is "风机主状态";
    yggl is "有功功率";

    alm1 is "alm1"
    alm2 is "alm2"
    alm3 is "alm3"
    alm4 is "alm4"
    alm5 is "alm5"
    alm6 is "alm6"
    alm7 is "alm7"
    alm8 is "alm8"
    alm9 is "alm9"

    fengsu is "风速";
    fengxang is "风向";
    cangwWd is "舱外温度";
    yeYali is "液压压力";
}

status {
    lastDevStatus is "上一次的devStatus"
    lastYggl is "上一次的有功功率"

    lastStatus is "上一次状态"
}

onInit {
    lastDevStatus = null
    lastYggl = null

    lastStatus = null
}


time_align { nullable }

output {
    mergeVirStatus is "合并抽象状态"
}

def devStatusMap = [0D: Status.GOOD_RUNNING.code, 1D: Status.STANDBY.code, 2D: Status.MAINTAINING_STOP.code,
                    3D: Status.TROUBLE_STOP.code, 4D: Status.MAINTAINING_STOP.code, 5D: Status.REMOTE_STOP.code,
                    6D: Status.WEATHER_STOP.code]

//def normalStatus = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态

task {
    //假如风机当前风机主状态为空,取上一次的主状态
    devStatus = devStatus != null ? devStatus : lastDevStatus
    yggl = yggl != null ? yggl : lastYggl

    def alms = [alm1, alm2, alm3, alm4, alm5, alm6, alm7, alm8, alm9]
    def hasalarm = {
        return alms.any { entry -> entry != null && entry.value != 0D }
    }
    def virtualStatus = null

    def isCommunicateStop = {
        return [fengsu, fengxang, cangwWd, yeYali].any { entry -> entry != null && entry.value != 0D }
    }

    def ygglRule = {
        def con = (Double) yggl
        if (con != null && con.value > 0D) {
            virtualStatus = hasalarm() ? Status.alarm_RUNNING.code : Status.GOOD_RUNNING.code
        }
    }

    def devStatusRule1 = {
        def con = (Double) devStatus
        if (con != null) {
            def statu = devStatusMap.find { it.key == con.value }
            if (statu != null) virtualStatus = statu.value
        }
    }

    def devStatusRule2 = {
        def con = (Double) devStatus
        if (!isCommunicateStop() || (con != null && con.value == -255D)) {
            virtualStatus = Status.COMMUNICATE_STOP.code
        } else if (con != null && con.value == -1D) {
            virtualStatus = Status.GOOD_RUNNING.code
        }
    }

    def filters = [ygglRule, devStatusRule1, devStatusRule2]

    for (filter in filters) {
        filter()
        if (virtualStatus != null) break
    }

    virtualStatus = virtualStatus != null ? virtualStatus : Status.UNKNOWN_STATUS.code

    //找到对应的时间戳，获取timestamp
    def time_items = [devStatus, yggl ,fengsu ,fengxang, cangwWd, yeYali]
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

    //给上一个状态赋值
    lastDevStatus = devStatus
    lastYggl = yggl

}