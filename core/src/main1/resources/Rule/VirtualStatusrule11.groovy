import io.unimatic.scripting.groovy.DataPoint
import java.text.SimpleDateFormat

//适用于rule1 国电大安红岗子
require {
    devStatus is "风机主状态";
    //报警状态码
    alarm56 is "截止频率"
    alarm70 is "刹车片磨损"
    alarm84 is "变频器实际力矩信号错误"
    alarm85 is "变频器入口温度功率减小"
    alarm86 is "变频器功率减小"
    alarm87 is "UPS报警"
    alarm123 is "桨角偏差过大"
    alarm138 is "风速计1故障"
    alarm139 is "风速计2故障"
    alarm140 is "风向不一致"
    alarm141 is "风速不一致"
    alarm153 is "塔筒X向高频振动"
    alarm154 is "塔筒Y向高频振动"
    alarm155 is "驱动链X向高频振动"
    alarm156 is "驱动链Y向高频振动"
    alarm175 is "变频器超出工作范围"
    alarm192 is "齿轮箱轴承1温度偏高"
    alarm193 is "齿轮箱轴承2温度偏高"
    alarm194 is "发电机轴承1温度偏高"
    alarm195 is "发电机轴承2温度偏高"
    alarm196 is "发电机定子绕组温度偏高"
    alarm197 is "机舱温度偏高"
    alarm198 is "低速轴轴承温度偏高"
    alarm199 is "变压器功率减小"
    alarm206 is "齿轮箱旁路过滤器堵塞"
    alarm207 is "齿轮箱过滤器堵塞"
    alarm208 is "齿轮油温度功率减小"
    alarm215 is "发电机避雷器故障"
    alarm216 is "定子绕组温度功率减小"
    alarm219 is "变桨电机"
    alarm223 is "航标灯失效"
    alarm224 is "航标灯维护警报"
    alarm226 is "风场管理功率减小"
    alarm227 is "风场通讯错误"
    alarm230 is "检查机舱位置"
    alarm231 is "金属温度低"
    alarm233 is "力矩滤波器1复位"
    alarm234 is "力矩滤波器2复位"
    alarm235 is "力矩信号不匹配"
    alarm239 is "电能控制冲突"
    alarm240 is "变压器电压低"
    alarm241 is "变压器电压高"
    alarm242 is "变压器轻瓦斯保护降功率运行"
    alarm264 is "机舱加热供电漏电（保留16）"
    //电网故障停机状态码
    alarm47 is "电网电流不对称"
    alarm48 is "电网掉电"
    alarm49 is "电网频率过高"
    alarm50 is "电网频率过低"
    alarm51 is "三相相位偏差"
    alarm52 is "电网矢量涌动"
    alarm53 is "相电压过低"
    alarm54 is "相电压过高"
    alarm57 is "暂态电网错误"
    alarm79 is "变频器电网错误"
    alarm251 is "相电压瞬时过高（保留3）"
    //维护停机状态码
    alarm21 is "机舱急停"
    alarm22 is "控制柜急停"
    alarm23 is "塔基急停"
    alarm25 is "叶片维护"
    alarm66 is "手动刹车"
    alarm157 is "手动停机"
    alarm158 is "手动变桨"
    alarm159 is "手动力矩"
    alarm55 is "生产过剩"
    alarm130 is "风速计结冰"
    alarm137 is "风向标结冰"
    alarm166 is "高风切出"
    alarm172 is "切入转速超范围"
    alarm173 is "通过共振区超时"
    alarm174 is "低风切出"
    alarm179 is "变频器入口温度过高"
    alarm188 is "舱外温度过高"
    alarm189 is "舱外温度过低"
    alarm221 is "轮毂温度过高"
    alarm236 is "启动超时"
    alarm272 is "空转切出（保留24）"
    alarm168 is "风场控制停机"
    alarm170 is "通讯停机"

    fengsu is "风速";
    fengxang is "风向";
    cangwWd is "舱外温度";
    yeYali is "液压压力";
}

status {
    lastDevStatus is '上一次主控状态'

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

enum VirtualStatus {
    GOOD_RUNNING(200), //	正常运行
    STANDBY(201),//	待机
    TEST_STOP(202),//	测试停机
    MAINTAINING_STOP(203),//	维护停机
    WEATHER_STOP(204),//	天气原因停机
    REMOTE_STOP(205),//	远程停机
    ALARM_RUNNING(400),//	报警运行
    ALARM_RUNNING_STANDBY(401),//	带报警待机
    LIMIT_LOAD_POWER(402),//	限负荷发电
    TROUBLE_STOP(500),//	故障停机
    NETWORK_STOP(501),//	电网原因停机
    COMMUNICATE_STOP(502),//通讯中断
    UNKNOWN_STATUS(601)//通讯中断

    int code

    VirtualStatus(int code) {
        this.code = code;
    }
}

task {
    def communicate_stop_code = [-128D, -30D, -29D, -20D, -10D, 0D]

    def common_devStatus_code = [60D, 70D, 80D, 81D, 90D, 100D]

    def stayby_devStatus_code = [18D, 20D, 25D, 26D, 27D, 30D, 31D, 32D, 33D, 40D, 45D, 46D, 50D, 51D, 52D, 53D, 54D]

    def alarm_item = [alarm56, alarm70, alarm84, alarm85, alarm86, alarm87, alarm123, alarm138, alarm139, alarm140,
                      alarm141, alarm153, alarm154, alarm155, alarm156, alarm175, alarm192, alarm193, alarm194,
                      alarm195, alarm196, alarm197, alarm198, alarm199, alarm206, alarm207, alarm208, alarm215,
                      alarm216, alarm219, alarm223, alarm224, alarm226, alarm227, alarm230, alarm231, alarm233,
                      alarm234, alarm235, alarm239, alarm240, alarm241, alarm242, alarm264]

    def network_stop_item = [alarm47, alarm48, alarm49, alarm50, alarm51, alarm52, alarm53, alarm54, alarm57, alarm79,
                             alarm251]

    def maintaining_stop_item = [alarm21, alarm22, alarm23, alarm25, alarm66, alarm157, alarm158, alarm159]

    def remote_stop_item = [alarm168, alarm170]

    def weather_stop_item = [alarm55, alarm130, alarm137, alarm166, alarm172, alarm173, alarm174, alarm179, alarm188,
                             alarm189, alarm221, alarm236, alarm272]

    def basic_item = [fengsu, fengxang, cangwWd, yeYali]

    def compareCodeInAlarm = { items ->
        for (item in items) if (item != null && item.value == 1D)
            return true
        return false
    }

    def isCommunicateStop = (fengsu == null || fengsu.value == 0D) &&
            (fengxang == null || fengxang.value == 0D) &&
            (cangwWd == null || cangwWd.value == 0D) &&
            (yeYali == null || yeYali.value == 0D)

    //rules
    def rule1 = {
        def con = (Double) devStatus
        if (con != null && virtualStatus == null)
            switch (con.doubleValue()) {
                case communicate_stop_code:
                    virtualStatus = VirtualStatus.COMMUNICATE_STOP.code
                    break
                case 2D:
                    virtualStatus = VirtualStatus.WEATHER_STOP.code
                    break
                case 3D:
                    if (compareCodeInAlarm(weather_stop_item))
                        virtualStatus = VirtualStatus.WEATHER_STOP.code
                    else
                        virtualStatus = VirtualStatus.TROUBLE_STOP.code
                    break
                case 4D:
                    virtualStatus = VirtualStatus.TEST_STOP.code
                    break
                case 5D:
                    virtualStatus = VirtualStatus.MAINTAINING_STOP.code
                    break
                case 6D..11D:
                    virtualStatus = VirtualStatus.TROUBLE_STOP.code
                    break
                case 18D..54D:
                    if (compareCodeInAlarm(alarm_item))
                        virtualStatus = VirtualStatus.ALARM_RUNNING_STANDBY.code
                    else if (con in stayby_devStatus_code) //如果不能确定，则判断是否为待机状态
                        virtualStatus = VirtualStatus.STANDBY.code
                    break
                case 60D..100D:
                    if (compareCodeInAlarm(alarm_item))
                        virtualStatus = VirtualStatus.ALARM_RUNNING.code
                    else if (con in common_devStatus_code)
                        virtualStatus = VirtualStatus.GOOD_RUNNING.code
                    break;
            }
    }

    def rule2 = {
        if (virtualStatus == null) {
            if (compareCodeInAlarm(maintaining_stop_item))
                virtualStatus = VirtualStatus.MAINTAINING_STOP.code
            else if (compareCodeInAlarm(network_stop_item))
                virtualStatus = VirtualStatus.NETWORK_STOP.code
            else if (compareCodeInAlarm(remote_stop_item))
                virtualStatus = VirtualStatus.REMOTE_STOP.code
        }
    }

    def rule3 = {
        if (virtualStatus == null && isCommunicateStop) virtualStatus = VirtualStatus.COMMUNICATE_STOP.code
    }

    def rule4 = {
        if (virtualStatus == null && devStatus != null) virtualStatus = VirtualStatus.UNKNOWN_STATUS.code
    }

    //获取timestamp
    def getTimeFromAlarms = { items ->
        for (item in items) if (item != null) return item.timestamp
    }

    def getTime = {
        if (devStatus != null)
            return devStatus.timestamp

        def result
        if ((result = getTimeFromAlarms(alarm_item)) != null) return result
        if ((result = getTimeFromAlarms(network_stop_item)) != null) return result
        if ((result = getTimeFromAlarms(maintaining_stop_item)) != null) return result
        if ((result = getTimeFromAlarms(remote_stop_item)) != null) return result
        if ((result = getTimeFromAlarms(weather_stop_item)) != null) return result
        if ((result = getTimeFromAlarms(basic_item)) != null) return result
    }

    def timestamp = getTime()

    if (devStatus == null) devStatus = lastDevStatus else lastDevStatus = devStatus

    //计算抽象状态
    //根据需求，优先级调整：devStatus + 状态码 》 通讯中断 》维护停机 》 电网故障停机 》远程停机
    rule1()
    rule3()
    rule2()
    rule4()

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
