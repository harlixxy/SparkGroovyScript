import io.unimatic.scripting.groovy.DataPoint


//适用于rule5 适用于左云二期风场
enum Status {
    GOOD_RUNNING(200),//正常运行
    STANDBY(201),//待机
    TROUBLE_STOP(500),//故障停机
    COMMUNICATE_STOP(502),//通讯中断
    UNKNOWN_STATUS(601)//未知状态

    int code

    Status(int code) {
        this.code = code;
    }
}

require {
    devStatus is "风机主状态";

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

//def normalStatus = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态

task {
    def virtualStatus = null

    if ((fengsu == null || fengsu.value == 0D) && (fengxang == null || fengxang.value == 0D) &&
            (cangwWd == null || cangwWd.value == 0D) && (yeYali == null || yeYali.value == 0D)) {
        virtualStatus = Status.COMMUNICATE_STOP.code
    } else if (devStatus != null) {
        def con = (Double) devStatus
        switch (con.value) {
            case 0D: virtualStatus = Status.GOOD_RUNNING.code
                break
            case 1D: virtualStatus = Status.STANDBY.code
                break
            case 2D: virtualStatus = Status.TROUBLE_STOP.code
                break
            default:
                virtualStatus = Status.UNKNOWN_STATUS.code
        }
    }

    //找到对应的时间戳，获取timestamp
    def time_items = [devStatus, fengsu, fengxang, cangwWd, yeYali]
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    //判断不出状态,使用上一次的状态
    if (virtualStatus == null) {
        if (lastStatus == null || lastStatus == Status.COMMUNICATE_STOP.code) {   //通讯中断不允许延续
            virtualStatus = Status.UNKNOWN_STATUS.code
        } else {
            virtualStatus = lastStatus.value
        }
    }
    //合并状态码输出
    if (lastStatus == null || virtualStatus.value != lastStatus.value) mergeVirStatus = virtualStatus
    //对上一次状态赋值
    if (virtualStatus != null) lastStatus = new DataPoint((Long) timestamp, (Double) virtualStatus)
}