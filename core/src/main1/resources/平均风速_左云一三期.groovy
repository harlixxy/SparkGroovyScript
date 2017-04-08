import io.unimatic.scripting.groovy.DataPoint
import java.text.SimpleDateFormat

//适用于左云一三期
//适用于(年月日)平均风速的算法
require {
    inPoint is "输入点位"
    failcode is "输入状态码"
}

time_align { nullable }

status {
    lastDay is "上个Day"
    dayValue is "当前Day累计值"
    dayCount is "当前Day累计次数"

    lastMonth is "上个Month"
    monthValue is "当前Month累计值"
    monthCount is "当前Month累计次数"

    lastYear is "上个Year"
    yearValue is "当前Year累计值"
    yearCount is "当前Year累计次数"

    lastTimeStamp is "上一次时间戳"
}

onInit {
    lastDay = null
    dayValue = 0D
    dayCount = 0D

    lastMonth = null
    monthValue = 0D
    monthCount = 0D

    lastYear = null
    yearValue = 0D
    yearCount = 0D

    lastTimeStamp = 0D
}

output {
    outDayPoint is "输出日平均点位"
    outMonthPoint is "输出月平均点位"
    outYearPoint is "输出年平均点位"
}

task {
    if (inPoint != null) {
        def today = new Date(inPoint.timestamp * 1000)
        def dayformat = new SimpleDateFormat("yyyy-MM-dd");
        def monthformat = new SimpleDateFormat("yyyy-MM");
        def yearformat = new SimpleDateFormat("yyyy");
        def day = dayformat.format(today)
        def month = monthformat.format(today)
        def year = yearformat.format(today)
        //计算初始化
        if (lastDay == null) {
            lastDay = day
            dayValue = inPoint.value
            dayCount++

            lastMonth = month
            monthValue = inPoint.value
            monthCount++

            lastYear = year
            yearValue = inPoint.value
            yearCount++

            lastTimeStamp = inPoint.timestamp
            return
        }

        //日平均计算
        if (day != lastDay) {
            outDayPoint = new DataPoint((Long) lastTimeStamp, (Double) (dayValue / dayCount))
            lastDay = day
            dayValue = 0D
            dayCount = 0D
        }
        //月平均计算
        if (month != lastMonth) {
            outMonthPoint = new DataPoint((Long) lastTimeStamp, (Double) (monthValue / monthCount))
            lastMonth = month
            monthValue = 0D
            monthCount = 0D
        }
        //年平均计算
        if (year != lastYear) {
            outYearPoint = new DataPoint((Long) lastTimeStamp, (Double) (yearValue / yearCount))
            lastYear = year
            yearValue = 0D
            yearCount = 0D
        }

        //failcode满足高风切出条件，则不参与此次统计
        def highCutMap = [16001, 16002, 16003]
        if (!(failcode != null && highCutMap.any { it == failcode.value })) {  //非高切才统计
            dayValue = dayValue + inPoint.value
            dayCount++

            monthValue = monthValue + inPoint.value
            monthCount++

            yearValue = yearValue + inPoint.value
            yearCount++
        }
        lastTimeStamp = inPoint.timestamp
    }
}