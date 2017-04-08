import io.unimatic.scripting.groovy.DataPoint
import java.text.SimpleDateFormat

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

    disusetime_dsum is "日不可用时间sum"
    disusetime_msum is "月不可用时间sum"
    disusetime_ysum is "年不可用时间sum"
    last500Timestamp is "上一次出现500的时间戳"  //遇超过10分钟的正常状态清除
    last203Timestamp is "上一次出现203的时间戳"  //遇超过10分钟的正常状态清除
    firstNsTimestamp is "第一个正常状态的时间戳"  //遇未超过10分钟相同正常状态清除
    firstNsStatus is "第一个正常状态" //遇未超过10分钟相同正常状态清除或改变
}

onInit {
    lastDevStatus = null
    lastFengsu = null
    lastFengxang = null
    lastCangwWd = null
    lastYeYali = null

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
    lastTime = null

    disusetime_dsum = 0
    disusetime_msum = 0
    disusetime_ysum = 0
    last500Timestamp = null
    last203Timestamp = null
    firstNsTimestamp = null
    firstNsStatus = null
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

    disusetime_day is "日不可利用时间"
    disusetime_month is "月不可利用时间"
    disusetime_year is "年不可利用时间"
}

//定义一些常量
def NORMALSTATUS = [200D, 400D, 201D, 401D, 402D, 200, 400, 201, 401, 402]  //正常状态
def SECONDTOMILLISECOND = 1000
def HOURTOSECOND = 3600
def DAYTOHOUR = 24

def statusList = [0D: Status.COMMUNICATE_STOP.code, 1D: Status.TROUBLE_STOP.code, 2D: Status.MAINTAINING_STOP.code,
                  3D: Status.STANDBY.code, 4D: Status.ALARM_RUNNING.code, 5D: Status.GOOD_RUNNING.code]

task {
    //找到对应的时间戳，获取timestamp
    def time_items = [devStatus ,fengsu ,fengxang, cangwWd, yeYali]
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

    //若当前点位为空，则用上一次的，若不为空，则覆盖上次的点位数据
    if (devStatus == null) devStatus = lastDevStatus else lastDevStatus = devStatus
    if (fengsu == null) fengsu = lastFengsu else lastFengsu = fengsu
    if (fengxang == null) fengxang = lastFengxang else lastFengxang = fengxang
    if (cangwWd == null) cangwWd = lastCangwWd else lastCangwWd = cangwWd
    if (yeYali == null) yeYali = lastYeYali else lastYeYali = yeYali

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

    //判断不出状态,使用上一次的状态
    if (virtualStatus == null) {
        virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    }

    //计算合并抽象状态
    def CalMergeVirStatus = { vs ->
        if (lastStatus == null || vs != lastStatus.value)
            mergeVirStatus = vs
    }(virtualStatus)

    //计算各类时间、次数
    def CalAllKindsTimes = { vs, ts ->
        //计算运行时间，故障时间，故障次数
        def isNeedAdd = false  //是否需要增加时长 用于跨天计算扣减
        def CalYXGZGZCSFun = {
            if (lastTime != null) {
                isNeedAdd = ((ts - lastTime) / 60 < 5)
            }

            //计算运行时间
            if (lastStatus != null) {
                if (lastStatus.value in NORMALSTATUS && lastTime != null
                        && isNeedAdd) {  //相邻两点位大于等于5分钟，则不累加时长
                    yxsj_dsum += (ts - lastStatus.timestamp)
                }
            }

            //计算故障停机时间和次数
            if (lastgzStatus != null && lastgzStatus.value == 500D) {
                if (lastStatus.value == 500D && lastTime != null
                        && isNeedAdd) {  //若上一次的状态也是500，才进行累加时间。若是其他的，则不累加
                    //相邻两点位大于等于5分钟，则不累加时长
                    gzsj_dsum += (ts - lastStatus.timestamp)
                }
                if (vs in NORMALSTATUS) { //如果这次正常，则纳入统计次数，并且清除上次故障状态
                    gztjcs_dsum = gztjcs_dsum + 1
                    lastgzStatus = null
                }
            }
        }()

        //计算 不可利用时间
        def isAddCalDisusetime = false  //是否增加时间，用于考虑跨天之后输出扣除多余的不可利用时间
        def CalDisusetimeFun = {
            def tempFirstNsTimestamp = null
            def tempFirstNsStatus = null
            def isIgnoreCal = false  //是否不参与计算 (默认是参与计算)
            if (vs == 500 && last203Timestamp == null) {
                if (last500Timestamp == null) {  //第一次进入500状态 后面不参与计算
                    isIgnoreCal = true
                }
                last500Timestamp = ts
                if (firstNsTimestamp != null) {
                    tempFirstNsTimestamp = firstNsTimestamp
                    tempFirstNsStatus = firstNsStatus
                }
                firstNsTimestamp = null
                firstNsStatus = null
            }
            if (vs == 203 && last500Timestamp == null) {
                last203Timestamp = ts
                firstNsTimestamp = null
                firstNsStatus = null
            }
            if (vs in NORMALSTATUS && firstNsTimestamp == null) {
                firstNsTimestamp = ts
                firstNsStatus = vs
            }
            if (lastStatus != null && lastStatus.value in NORMALSTATUS
                    && (firstNsTimestamp ?: tempFirstNsTimestamp) != null ) {
                if (ts - (firstNsTimestamp ?: tempFirstNsTimestamp) >= 60 * 10
                        && lastStatus.value == (firstNsStatus ?: tempFirstNsStatus)) {
                    isIgnoreCal = true  //超过10分钟不参与计算
                    //清除所有状态
                    last203Timestamp = null
                    last500Timestamp = null
                    firstNsTimestamp = null
                    firstNsStatus = null
                    if (vs == 500) {
                        last500Timestamp = ts
                    } else if (vs == 203) {
                        last203Timestamp = ts
                    } else {
                        //其它不管
                    }
                }
            }

            if (isIgnoreCal){  //超过10分钟 NS状态 或 第一次进入500状态
                //不进行任何累加操作
            } else if ((firstNsTimestamp ?: tempFirstNsTimestamp) != null
                    && lastStatus != null && lastStatus.value != vs
                    && firstNsTimestamp != ts) {  //存在正常情况但上一次的状态不等于这一次的状态
                if (last500Timestamp != null) {
                    isAddCalDisusetime = true
                    disusetime_dsum += ts - (firstNsTimestamp ?: tempFirstNsTimestamp)
                    disusetime_msum += ts - (firstNsTimestamp ?: tempFirstNsTimestamp)
                    disusetime_ysum += ts - (firstNsTimestamp ?: tempFirstNsTimestamp)
                }

                if (vs in NORMALSTATUS && lastStatus.value != vs) {  //重置正常状态
                    firstNsTimestamp = ts
                    firstNsStatus = vs
                } else {
                    firstNsTimestamp = null
                    firstNsStatus = null
                }
            } else {  //其他情况
                //持续500状态，并且没有203和没有进入正常状态(第一次进入正常状态不算)，则统计
                if (last500Timestamp != null && lastTime != null && last203Timestamp == null
                        && (firstNsTimestamp == null || firstNsTimestamp == ts))
                {
                    isAddCalDisusetime = true
                    disusetime_dsum += ts - lastTime
                    disusetime_msum += ts - lastTime
                    disusetime_ysum += ts - lastTime
                }
            }
        }()

        //初始化相关计算输出判定变量
        def today = null
        def month = null
        def year = null
        def curdatestamp = null  //当日时间正点
        def overtimestamp = null //超过当天的整点时间戳
        def InitRalationParaFun = {
            if (vs != null && ts > 0) {
                def date = new Date(ts * SECONDTOMILLISECOND)
                def dayformat = new SimpleDateFormat("yyyy-MM-dd");
                def monthformat = new SimpleDateFormat("yyyy-MM");
                def yearformat = new SimpleDateFormat("yyyy");
                today = dayformat.format(date)
                month = monthformat.format(date)
                year = yearformat.format(date)

                Calendar cal = Calendar.getInstance();
                cal.setTime(dayformat.parse(today));
                curdatestamp = cal.getTimeInMillis() / SECONDTOMILLISECOND
                overtimestamp = ts - curdatestamp  //超过当天的整点时间
            }
            if (lastday == null && today != null) {
                lastday = today
                lastmonth = month
                lastyear = year
            }
        }()

        //日统计输出
        if (lastday != today && lastday != null) {
            //计算运行时间
            if (isNeedAdd && lastStatus.value in NORMALSTATUS) {
                ryxsj = new DataPoint((Long) lastTime,
                        (double) ((yxsj_dsum - overtimestamp) / HOURTOSECOND))
                yxsj_msum += yxsj_dsum - overtimestamp   //减去超时的
                yxsj_dsum = overtimestamp
            } else {
                ryxsj = new DataPoint((Long) lastTime,
                        (double) ((yxsj_dsum) / HOURTOSECOND))
                yxsj_msum += yxsj_dsum
                yxsj_dsum = 0D
            }

            //计算故障时间
            if (isNeedAdd && lastStatus.value == 500D) {
                rgztjsj = new DataPoint((Long) lastTime,
                        (double) ((gzsj_dsum - overtimestamp) / HOURTOSECOND))
                gzsj_msum += gzsj_dsum - overtimestamp   //减去超时的
                gzsj_dsum = overtimestamp   //供计算下次的
            } else {
                rgztjsj = new DataPoint((Long) lastTime,
                        (double) ((gzsj_dsum) / HOURTOSECOND))
                gzsj_msum += gzsj_dsum
                gzsj_dsum = 0D
            }

            //输出停机次数
            rgztjcs = new DataPoint((Long) lastTime, (double) gztjcs_dsum)
            gztjcs_msum += gztjcs_dsum

            lastday = today
            gztjcs_dsum = 0D

            //输出不可利用时间 日
            def overtimestampclone = 0  //用于结转上一次的扣除的
            if (isAddCalDisusetime) {
                disusetime_dsum -= overtimestamp
                overtimestampclone = overtimestamp
                //计算结果 日不能大于24小时
                if (disusetime_dsum / HOURTOSECOND > DAYTOHOUR) {
                    disusetime_dsum = DAYTOHOUR * HOURTOSECOND
                }
            }

            disusetime_day = new DataPoint((Long) lastTime,
                    (double) ((disusetime_dsum) / HOURTOSECOND))
            disusetime_dsum = overtimestampclone
        }

        //月统计输出
        if (lastmonth != month && lastmonth != null) {
            yyxsj = new DataPoint((Long) lastTime, (Double) (yxsj_msum / HOURTOSECOND))
            yxsj_ysum += yxsj_msum
            yxsj_msum = 0D

            ygztjsj = new DataPoint((Long) lastTime, (Double) (gzsj_msum / HOURTOSECOND))
            gzsj_ysum += gzsj_msum
            gzsj_msum = 0D

            ygztjcs = new DataPoint((Long) lastTime, (Double) gztjcs_msum)
            gztjcs_ysum += gztjcs_msum;
            gztjcs_msum = 0D

            lastmonth = month

            //输出不可利用时间 月(扣减超出该月1号的时间)
            def overtimestampclone = 0  //用于结转上一次的扣除的
            if (isAddCalDisusetime) {
                def date = new Date(ts * SECONDTOMILLISECOND)
                def dayformat = new SimpleDateFormat("yyyy-MM-01")
                Calendar cal = Calendar.getInstance()
                cal.setTime(dayformat.parse(dayformat.format(date)));
                def curmonthstamp = cal.getTimeInMillis() / SECONDTOMILLISECOND  //取得当前月初的第一个时间戳

                disusetime_msum -= (ts - curmonthstamp)
                overtimestampclone = ts - curmonthstamp
                cal.add(Calendar.DATE, -1) //减去一天，为了取得上个月最大的日期
                def maxDay = cal.get(Calendar.DAY_OF_MONTH)

                //计算结果 月不能大于上个月的天数*24
                if (disusetime_msum / HOURTOSECOND > maxDay * DAYTOHOUR) {
                    disusetime_msum = maxDay * DAYTOHOUR * HOURTOSECOND
                }
            }

            disusetime_month = new DataPoint((Long) lastTime,
                    (double) ((disusetime_msum) / HOURTOSECOND))
            disusetime_msum = overtimestampclone
        }

        //年统计输出
        if (lastyear != year && lastyear != null) {
            nyxsj = new DataPoint((Long) lastTime, (Double) (yxsj_ysum / HOURTOSECOND))
            yxsj_ysum = 0D

            ngztjsj = new DataPoint((Long) lastTime, (Double) (gzsj_ysum / HOURTOSECOND))
            gzsj_ysum = 0D

            ngztjcs = new DataPoint((Long) lastTime, (Double) gztjcs_ysum)
            gztjcs_ysum = 0D

            lastyear = year

            //输出不可利用时间 年(扣减超出计算的时间)
            def overtimestampclone = 0
            if (isAddCalDisusetime) {
                def date = new Date(ts * SECONDTOMILLISECOND)
                def dayformat = new SimpleDateFormat("yyyy-01-01");
                Calendar cal = Calendar.getInstance();
                cal.setTime(dayformat.parse(dayformat.format(date)));
                def curyearstamp = cal.getTimeInMillis() / SECONDTOMILLISECOND

                disusetime_ysum -= ts - curyearstamp
                overtimestampclone = ts - curyearstamp
                cal.add(Calendar.DATE, -1) //减去一天，为了取得去年一共有多少天
                def maxDay = cal.get(Calendar.DAY_OF_YEAR)

                //计算结果 年不能大于去年的天数*24
                if (disusetime_ysum / HOURTOSECOND > maxDay * DAYTOHOUR) {
                    disusetime_ysum = maxDay * DAYTOHOUR * HOURTOSECOND
                }
            }

            disusetime_year = new DataPoint((Long) lastTime,
                    (double) ((disusetime_ysum) / HOURTOSECOND))
            disusetime_ysum = overtimestampclone
        }
    }(virtualStatus, timestamp)

    //重置相关状态
    def ResetStates = { vs, ts ->
        //重置变量
        lastTime = ts
        if (vs != null) {
            lastStatus = new DataPoint((Long) ts, (Double) vs)
            if (vs == 500D) {
                lastgzStatus = new DataPoint((Long) ts, (Double) vs)
            }
        }
    }(virtualStatus, timestamp)
}