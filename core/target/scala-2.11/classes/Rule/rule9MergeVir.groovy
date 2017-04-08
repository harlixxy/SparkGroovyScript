import io.unimatic.scripting.groovy.DataPoint

//适用于rule9 中航安北乐胜
enum Status {
    GOOD_RUNNING(200),//正常运行
    STANDBY(201),//待机
    MAINTAINING_STOP(203),//维护停机
    WEATHER_STOP(204),//天气原因停机
    REMOTE_STOP(205),//远程停机
    LIMIT_LOAD_POWER(402),//限负荷发电
    TROUBLE_STOP(500),//故障停机
    COMMUNICATE_STOP(502),//通讯中断
    UNKNOWN_STATUS(601)//未知状态
    int code

    Status(int code) {
        this.code = code;
    }
}

require {
    fail is "故障停机"
    running is "运行"
    commErr is "通信中断"
    whStop is "维护"
    stop is "停机"
    generate is "正常发电"
    scadaPpmen is "限功率运行"
    start is "启动"

    fengsu is "风速";
    fengxang is "风向";
    cangwWd is "舱外温度";
    yeYali is "液压压力";
}

status {
    lastStatus is "上一次状态"

    lastFail is "上一次故障停机"
    lastRunning is "上一次运行"
    lastCommErr is "上一次通信中断"
    lastWhStop is "上一次维护"
    lastStop is "上一次停机"
    lastGenerate is "上一次正常发电"
    lastScadaPpmen is "上一次限功率运行"
    lastStart is "上一次启动"
}

onInit {
    lastStatus = null

    lastFail = null
    lastRunning = null
    lastCommErr = null
    lastWhStop = null
    lastStop = null
    lastGenerate = null
    lastScadaPpmen = null
    lastStart = null
}

time_align { nullable }

output {
    mergeVirStatus is "合并抽象状态"
}

//def normalStatus = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态

def statusList = [[0D, 0D, 1D, 0D, 0D, 0D, 0D, 1D]: Status.STANDBY.code,
                  [0D, 1D, 1D, 0D, 0D, 1D, 0D, 0D]: Status.GOOD_RUNNING.code,
                  [0D, 1D, 1D, 0D, 0D, 1D, 1D, 0D]: Status.LIMIT_LOAD_POWER.code,
                  [1D, 0D, 1D, 0D, 1D, 0D, 0D, 0D]: Status.TROUBLE_STOP.code,
                  [0D, 0D, 1D, 0D, 1D, 0D, 0D, 0D]: Status.WEATHER_STOP.code,
                  [0D, 0D, 1D, 1D, 1D, 0D, 0D, 0D]: Status.MAINTAINING_STOP.code,
                  [0D, 0D, 1D, 1D, 1D, 0D, 1D, 0D]: Status.REMOTE_STOP.code]

task {
    def virtualStatus = null

    //找到对应的时间戳，获取timestamp
    def time_items = [fengsu ,fengxang, cangwWd, yeYali ,
                      fail , running , commErr , whStop , stop ,
                      generate , scadaPpmen , start ]
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    //写入状态
    if (fail == null) fail = lastFail else lastFail = fail
    if (running == null) running = lastRunning else lastRunning = running
    if (commErr == null) commErr = lastCommErr else lastCommErr = commErr
    if (whStop == null) whStop = lastWhStop else lastWhStop = whStop
    if (stop == null) stop = lastStop else lastStop = stop
    if (generate == null) generate = lastGenerate else lastGenerate = generate
    if (scadaPpmen == null) scadaPpmen = lastScadaPpmen else lastScadaPpmen = scadaPpmen
    if (start == null) start = lastStart else lastStart = start

    //开始计算
    if (commErr != null && generate != null && commErr.value == 0D && generate.value == 0D) {
        virtualStatus = Status.COMMUNICATE_STOP.code
    } else {
        def judgeList = [fail, running, commErr, whStop, stop, generate, scadaPpmen, start]
        def status = statusList.find { it.key.equals(judgeList) }
        if (status == null) {
            def commErr_metric = [fengsu, fengxang, cangwWd, yeYali]
            def isCommunicateStop = commErr_metric.any { entry -> entry != null && entry.value != 0D }
            virtualStatus = !isCommunicateStop ? Status.COMMUNICATE_STOP.code : Status.UNKNOWN_STATUS.code
        } else {
            virtualStatus = status.value
        }
    }

    //判断不出状态,使用上一次的状态
    if (virtualStatus == null) {
        virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    }

    //合并状态码输出
    if (lastStatus == null || virtualStatus.value != lastStatus.value) mergeVirStatus = virtualStatus
    //对上一次状态赋值
    if (virtualStatus != null) lastStatus = new DataPoint((Long) timestamp, (Double) virtualStatus)
}