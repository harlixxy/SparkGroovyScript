import io.unimatic.scripting.groovy.DataPoint
import java.text.SimpleDateFormat

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
    inputCode is "输入抽象编号"
}

status {
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

task {
    virtualStatus = inputCode.value;

    //判断不出状态,使用上一次的状态
    if (virtualStatus == null) {
        virtualStatus = lastStatus == null ? Status.UNKNOWN_STATUS.code : lastStatus.value
    }

    //找到对应的时间戳，获取timestamp
    def time_items = [inputCode]
    def getTime = { items ->
        for (item in items) if (item != null) return item.timestamp
    }
    def timestamp = getTime(time_items)

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

            ygztjsj = new DataPoint((Long) lastTime, (Double) (gzsj_msum / 3600))
            gzsj_ysum += gzsj_msum

            if (isNeedAdd && lastStatus.value == 500D) {
                yxsj_msum = 0D
                gzsj_msum = timestamp - curdatestamp   //供计算下次的
            } else if (isNeedAdd && lastStatus.value in normalStatus) {
                yxsj_msum = timestamp - curdatestamp   //供计算下次的
                gzsj_msum = 0D
            } else {
                yxsj_msum = 0D
                gzsj_msum = 0D
            }

            ygztjcs = new DataPoint((Long) lastTime, (Double) gztjcs_msum)
            gztjcs_ysum += gztjcs_msum;

            lastmonth = month
            gztjcs_msum = 0D
        }

        //年统计输出
        if (lastyear != year && lastyear != null) {
            nyxsj = new DataPoint((Long) lastTime, (Double) (yxsj_ysum / 3600))
            ngztjsj = new DataPoint((Long) lastTime, (Double) (gzsj_ysum / 3600))

            if (isNeedAdd && lastStatus.value == 500D) {
                yxsj_ysum = 0D
                gzsj_ysum = timestamp - curdatestamp  //供计算下次的
            } else if (isNeedAdd && lastStatus.value in normalStatus) {
                yxsj_ysum = timestamp - curdatestamp  //供计算下次的
                gzsj_ysum = 0D
            } else {
                yxsj_ysum = 0D
                gzsj_ysum = 0D
            }

            ngztjcs = new DataPoint((Long) lastTime, (Double) gztjcs_ysum)
            lastyear = year
            gztjcs_ysum = 0D
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