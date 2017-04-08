import io.unimatic.scripting.groovy.DataPoint

import java.text.SimpleDateFormat

//适用于rule6 霍林河风场
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
    UNKNOWN_STATUS(601)//未知状态
    int code

    Status(int code) {
        this.code = code;
    }
}

require {
    devStatus is "风机主状态"
    stateAvaile is "可利用率状态"
    failcode is "实际停机条件（故障码）"
    yggl is "有功功率"
    fengsu is "实际风速"
    fengxang is "实际风向"
    cangwWd is "机舱外温度"
    yeYali is "液压站压力"
}

status {
    lastDevStatus is "上一次风机主状态"
    lastStateAvaile is "上一次可利用率状态"
    lastFailcode is "上一次实际停机条件（故障码）"
    lastYggl is "上一次有功功率"
//    lastFengsu is "上一次实际风速"
//    lastFengxang is "上一次实际风向"
//    lastCangwWd is "上一次机舱外温度"
//    lastYeYali is "上一次液压站压力"

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
    lastDevStatus = null
    lastStateAvaile = null
    lastFailcode = null
    lastYggl = null
//    lastFengsu = null
//    lastFengxang = null
//    lastCangwWd = null
//    lastYeYali = null

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

def alarmCode = [1015D, 4006D, 4034D, 4035D, 4036D, 4040D, 4041D, 4042D, 4054D, 4055D, 4063D, 6007D, 6009D, 6010D, 6023D,//报警状态码
                 9008D, 9009D, 11014D, 11017D, 11018D, 13001D, 13002D, 13003D, 13004D, 13005D, 13006D, 13007D, 13008D,
                 13009D, 13010D, 13011D, 15011D]
def testCode = [4047D]//测试停机
def maintainingCode = [1002D, 3003D, 3004D, 3005D, 3007D, 16017D, 26001D, 26002D, 26003D, 26006D]//停机类型为3(RCP人工停机,远程操作屏停机除外)
def remoteCode = [26004D, 26005D]//RCP人工停机,远程操作屏停机
def systemCode = [4047D]//系统状态码
def troubleCode = [1001D, 1003D, 1004D, 1005D, 1006D, 1007D, 1008D, 1009D, 1010D, 1011D, 1012D, 1013D, 1014D, 2021D,//故障状态码
                   2022D, 2031D, 2032D, 2033D, 2041D, 2042D, 2043D, 2021D, 2022D, 2031D, 2032D, 2033D, 2041D, 2042D,
                   2043D, 3001D, 3002D, 3006D, 3008D, 3009D, 3010D, 3011D, 3012D, 3013D, 3014D, 3015D, 3016D, 4001D,
                   4002D, 4003D, 4004D, 4005D, 4007D, 4008D, 4009D, 4010D, 4011D, 4012D, 4013D, 4014D, 4015D, 4016D,
                   4017D, 4018D, 4019D, 4020D, 4021D, 4022D, 4023D, 4024D, 4025D, 4026D, 4027D, 4028D, 4029D, 4030D,
                   4031D, 4032D, 4033D, 4037D, 4038D, 4039D, 4043D, 4044D, 4045D, 4046D, 4048D, 4049D, 4050D, 4051D,
                   4052D, 4053D, 4056D, 4057D, 4058D, 4059D, 4062D, 4061D, 4062D, 4065D, 6001D, 6002D, 6003D, 6004D,
                   6005D, 6006D, 6008D, 6011D, 6012D, 6013D, 6014D, 6015D, 6016D, 6017D, 6018D, 6019D, 6020D, 6021D,
                   6022D, 7001D, 7002D, 7003D, 7004D, 7005D, 7006D, 7007D, 7008D, 7009D, 7010D, 7011D, 7012D, 7013D,
                   7014D, 7015D, 8010D, 8011D, 8012D, 9001D, 9002D, 9003D, 9004D, 9005D, 9006D, 9007D, 9010D, 9011D,
                   9012D, 9013D, 10001D, 10002D, 10003D, 10004D, 10005D, 10006D, 11001D, 11002D, 11003D, 11004D, 11005D,
                   11006D, 11007D, 11008D, 11009D, 11010D, 11011D, 11012D, 11013D, 11015D, 11016D, 12001D, 12002D, 12003D,
                   12004D, 12005D, 12006D, 12007D, 12008D, 12009D, 12010D, 15006D, 15007D, 15010D, 15012D, 15013D, 16001D,
                   16002D, 16003D, 16004D, 16005D, 16006D, 16007D, 16008D, 16009D, 16010D, 16011D, 16012D, 16013D, 16014D,
                   16015D, 16016D, 17001D, 17002D, 17003D, 17004D, 17005D, 17006D, 17007D, 17008D, 17009D, 17010D, 17011D]
def weatherCode = [4064D, 15001D, 15002D, 15003D, 15004D, 15005D, 15008D, 15009D]//天气停机状态码
def networkCode = [8001D, 8002D, 8003D, 8004D, 8005D, 8006D, 8007D, 8008D, 8009D, 8013D, 8014D]//电网状态码

def remove = { statusList, rang ->
    for (item in rang) {
        statusList.remove(item)
    }
    statusList
}
def devStatusRule = { statusList, condition ->
    def con = (Double) condition
    switch (con) {
        case 0D: remove(statusList, [1, 2, 8, 9, 10, 11])
            break
        case 1D:
            remove(statusList, [1, 2, 4, 5, 6, 7, 8, 11])
            break
        case 2D: remove(statusList, [4, 5, 6, 7, 8, 9, 10, 11])
            break
        case 3D: remove(statusList, [4, 5, 6, 7, 8, 9, 10])
            break
        case 4D: remove(statusList, [1, 2, 4, 5, 6, 7, 9, 10, 11])
            break
        default: remove(statusList, [1, 2, 4, 5, 6, 7, 8, 9, 10, 11])
    }
    statusList
}
def stateAvaileRule = { statusList, condition ->
    def con = (Double) condition
    switch (con) {
        case 0D: remove(statusList, [4, 5, 6, 7, 8])
            break
        case 1D: remove(statusList, [1, 2, 4, 6, 7, 8])
            break
        case 2D: remove(statusList, [1, 2, 4, 5, 7, 8])
            break
        case 3D: remove(statusList, [1, 2, 4, 5, 6])
            break
        case 4D: remove(statusList, [1, 2, 5, 6, 7, 8])
            break
        default: remove(statusList, [1, 2, 4, 5, 6, 7, 8])
    }
    statusList
}
def failCodeRule1 = { statusList, condition ->
    def con = (Double) condition
    if (con != null) {
        if (con.value == 0D) remove(statusList, [2, 7, 8, 9, 3])
        else if (testCode.contains(con.value)) remove(statusList, [1, 2, 7, 8, 9])
        else if (alarmCode.contains(con.value)) remove(statusList, [1, 7, 8, 3])
        else if (remoteCode.contains(con.value)) remove(statusList, [1, 2, 8, 9, 3])
        else if (maintainingCode.contains(con.value)) remove(statusList, [1, 2, 7, 9, 3])
        else remove(statusList, [1, 2, 7, 8, 9, 3])
    } else {
        remove(statusList, [1, 2, 7, 8, 9, 3])
    }
    statusList
}
def ygglRule = { statusList, condition ->
    def con = (Double) condition
    if (con != null) {
        if (con.value > 0D) remove(statusList, [11])
        else if (con.value < 0D) remove(statusList, [1])
        else remove(statusList, [1])  //去掉 11 （需求修改）
    } else {
        remove(statusList, [1, 11])
    }
    statusList
}

def failCodRule2 = { condition ->
    def con = (Double) condition
    if (con != null) {
        if (systemCode.contains(con.value)) return Status.GOOD_RUNNING.code
        else if (alarmCode.contains(con.value)) return Status.ALARM_RUNNING.code
        else if (troubleCode.contains(con.value)) return Status.TROUBLE_STOP.code
        else if (maintainingCode.contains(con.value)) return Status.MAINTAINING_STOP.code
        else if (remoteCode.contains(con.value)) return Status.REMOTE_STOP.code
        else if (weatherCode.contains(con.value)) return Status.WEATHER_STOP.code
        else if (networkCode.contains(con.value)) return Status.NETWORK_STOP.code
    }
    return Status.UNKNOWN_STATUS.code
}

//1 正常运行,2 报警运行,3 测试停机,4 故障停机,5 天气原因停机,6 电网原因停机,
// 7 远程停机,8 维护停机,9 待机,10 待机,11 带报警待机,12 通讯中断
task {
    def mp = [1 : Status.GOOD_RUNNING.code, 2: Status.ALARM_RUNNING.code, 3: Status.TEST_STOP.code,
              4 : Status.TROUBLE_STOP.code, 5: Status.WEATHER_STOP.code, 6: Status.NETWORK_STOP.code,
              7 : Status.REMOTE_STOP.code, 8: Status.MAINTAINING_STOP.code, 9: Status.ALARM_RUNNING_STANDBY.code,
              10: Status.STANDBY.code, 11: Status.STANDBY.code]

    //找到对应的时间戳，获取timestamp
    def time_items = [devStatus ,stateAvaile,failcode,yggl ,fengsu ,fengxang, cangwWd, yeYali]
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    //假如风机重要数据为空,取上一次的数据
    devStatus = devStatus != null ? devStatus : lastDevStatus
    stateAvaile = stateAvaile != null ? stateAvaile : lastStateAvaile
    failcode = failcode != null ? failcode : lastFailcode
    yggl = yggl != null ? yggl : lastYggl

    def isCommunicateStop = [fengsu, fengxang, cangwWd, yeYali].any { entry -> entry != null && entry.value != 0D }
    def filters = [[devStatus, devStatusRule], [stateAvaile, stateAvaileRule], [failcode, failCodeRule1], [yggl, ygglRule]]
    //计算抽象状态
    for (f in filters) {
        mp = f.get(1)(mp, f.get(0))
    }
    if (mp.isEmpty()) {
        if (!isCommunicateStop) {
            virtualStatus = Status.COMMUNICATE_STOP.code
        } else {
            virtualStatus = failCodRule2(failcode)
        }
    } else {
        virtualStatus = mp.get(mp.keySet().min())
    }

    //给上一个状态赋值
    lastDevStatus = devStatus
    lastStateAvaile = stateAvaile
    lastFailcode = failcode
    lastYggl = yggl

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