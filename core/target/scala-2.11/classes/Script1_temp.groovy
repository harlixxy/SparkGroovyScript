import io.unimatic.scripting.groovy.DataPoint
import java.text.SimpleDateFormat

//适用于rule5 适用于左云二期风场
//（增加合并抽象状态，调整各种时间的规则）
//（增加 相邻两点位大于等于5分钟，则不累加时长 的控制）
enum Status {
    GOOD_RUNNING(200),//正常运行
    STANDBY(201),//待机
    TROUBLE_STOP(500),//故障停机
    COMMUNICATE_STOP(502),//通讯中断
    UNKNOWN_STATUS(601), //未知状态
    ALARM_RUNNING(400)
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

    yggl is "有功功率" //供性能分析
    clxYouWd is "齿轮箱油温"  //供性能分析
}

status {
    lastStatus is "上一次状态"
    lastTime is "上一次时间戳"

    first200Timestamp is "第一次200状态时间戳"
    first400Timestamp is "第一次400状态时间戳"
    first200or400Timestamp is "第一次200或400状态时间戳"
}

onInit {
    lastStatus = null
    lastTime = 0D

    first200Timestamp = null
    first400Timestamp = null
    first200or400Timestamp = null
}

time_align { nullable }

output {
    outYgglCangwWdFor200 is "有功功率（舱外温度）-正常状态"
    outCangwWdFor200 is "舱外温度-正常状态"
    outYgglClxYouWdFor200 is "有功功率（齿轮箱油温）-正常状态"
    outClxYouWdFor200 is "齿轮箱油温-正常状态"

    outYgglCangwWdFor400 is "有功功率（舱外温度）-报警运行"
    outCangwWdFor400 is "舱外温度-报警运行"
    outYgglClxYouWdFor400 is "有功功率（齿轮箱油温）-报警运行"
    outClxYouWdFor400 is "齿轮箱油温-报警运行"

    outYgglCangwWd is "有功功率（舱外温度）-正常或报警运行"
    outCangwWd is "舱外温度-正常或报警运行"
    outYgglClxYouWd is "有功功率（齿轮箱油温）-正常或报警运行"
    outClxYouWd is "齿轮箱油温-正常或报警运行"
}


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

    /***分割线***/

    //先考虑正常运行
    def isOut200 = false
    if (lastStatus != null && lastStatus.value == virtualStatus && virtualStatus == Status.GOOD_RUNNING.code) {
        isOut200 = isNeedOut(first200Timestamp)
    } else {
        first200Timestamp = (virtualStatus == Status.GOOD_RUNNING) ? timestamp : null
    }

    //再考虑报警运行
    def isOut400 = false
    if (lastStatus != null && lastStatus.value == virtualStatus && virtualStatus == Status.ALARM_RUNNING.code) {
        isOut400 = isNeedOut(first400Timestamp)
    } else {
        first400Timestamp = (virtualStatus == Status.GOOD_RUNNING) ? timestamp : null
    }

    //最后考虑正常运行或报警运行
    def isOut200or400 = false
    if (lastStatus != null && lastStatus.value == virtualStatus
            && virtualStatus in [Status.ALARM_RUNNING.code, Status.GOOD_RUNNING.code]) {
        isOut200or400 = isNeedOut(first200or400Timestamp)
    } else {
        first200or400Timestamp = (virtualStatus in [Status.ALARM_RUNNING.code, Status.GOOD_RUNNING.code]) ? timestamp : null
    }

    //开始根据输出进行计算
    if (yggl != null) {
        if (cangwWd != null && yggl.timestamp == cangwWd.timestamp) {
            if (isOut200) {
                outYgglCangwWdFor200 = yggl.value
                outCangwWdFor200 = cangwWd.value
            }
            if (isOut400) {
                outYgglCangwWdFor400 = yggl.value
                outCangwWdFor400 = cangwWd.value
            }
            if (isOut200or400) {
                outYgglCangwWd = yggl.value
                outCangwWd = cangwWd.value
            }
        }

        if (clxYouWd != null && yggl.timestamp == clxYouWd.timestamp) {
            if (isOut200) {
                outYgglClxYouWdFor200 = yggl.value
                outCangwWdFor200 = clxYouWd.value
            }
            if (isOut400) {
                outYgglCangwWdFor400 = yggl.value
                outCangwWdFor400 = clxYouWd.value
            }
            if (isOut200or400) {
                outYgglCangwWd = yggl.value
                outCangwWd = clxYouWd.value
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