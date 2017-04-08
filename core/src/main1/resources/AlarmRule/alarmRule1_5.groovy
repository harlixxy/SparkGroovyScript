//大庆肇源新龙顺德

require {
    falarm15  is "安全链断开"
    falarm114 is "变桨错误"
    falarm93  is "变桨电池1电压错误"
    falarm94  is "变桨电池2电压错误"
    falarm95  is "变桨电池3电压错误"
    falarm84  is "变桨电池测试"
    falarm96  is "变桨电池充电回路故障"
    falarm97  is "变桨电池充电器故障"
    falarm98  is "变桨电池电压错误"
    falarm86  is "变桨驱动器1错误"
    falarm87  is "变桨驱动器2错误"
    falarm88  is "变桨驱动器3错误"
    falarm85  is "变桨通讯错误"
    falarm215 is "变桨自主运行"
    falarm170 is "变频器超出工作范围"
    falarm76  is "变频器错误"
    falarm174 is "变频器入口温度过高"
    falarm158 is "变频器脱网"
    falarm78  is "变频器未就绪"
    falarm73  is "变频器正在加热"
    falarm2   is "常规错误重复"
    falarm195 is "齿轮箱风扇故障"
    falarm200 is "齿轮箱油泵故障"
    falarm199 is "齿轮箱油位低"
    falarm187 is "齿轮箱轴承1温度偏高"
    falarm203 is "齿轮油温度功率减小"
    falarm177 is "齿轮油温度过高"
    falarm169 is "低风切出"
    falarm42  is "电网电流不对称"
    falarm44  is "电网频率过高"
    falarm211 is "定子绕组温度功率减小"
    falarm27  is "发电机超速"
    falarm191 is "发电机定子绕组温度偏高"
    falarm207 is "发电机风扇故障"
    falarm190 is "发电机轴承2温度偏高"
    falarm163 is "风场控制停机"
    falarm222 is "风场通讯错误"
    falarm136 is "风速不一致"
    falarm125 is "风速计结冰"
    falarm28  is "高速轴超速"
    falarm14  is "高速轴超速开关"
    falarm156 is "极限开关旁路超时"
    falarm102 is "桨角偏差时间过长"
    falarm92  is "桨角不一致"
    falarm6   is "交流电源故障"
    falarm231 is "启动超时"
    falarm115 is "设定与实际桨角1偏差过大"
    falarm116 is "设定与实际桨角2偏差过大"
    falarm117 is "设定与实际桨角3偏差过大"
    falarm83  is "收桨超时"
    falarm61  is "手动刹车"
    falarm152 is "手动停机"
    falarm18  is "塔基急停"
    falarm139 is "塔筒X向1级振动"
    falarm141 is "塔筒X向2级振动"
    falarm140 is "塔筒Y向1级振动"
    falarm142 is "塔筒Y向2级振动"
    falarm168 is "通过共振区超时"
    falarm48  is "相电压过低"
    falarm49  is "相电压过高"
    falarm165 is "业主控制停机"
    falarm119 is "叶片1收桨速度过慢"
    falarm120 is "叶片2收桨速度过慢"
    falarm20  is "叶片维护"
    falarm68  is "液压压力过低"
    falarm52  is "暂态电网错误"
}
output {
    mergeAlarm0   is "mergeAlarm0"
    mergeAlarm15  is "mergeAlarm15"
    mergeAlarm114 is "mergeAlarm114"
    mergeAlarm93  is "mergeAlarm93"
    mergeAlarm94  is "mergeAlarm94"
    mergeAlarm95  is "mergeAlarm95"
    mergeAlarm84  is "mergeAlarm84"
    mergeAlarm96  is "mergeAlarm96"
    mergeAlarm97  is "mergeAlarm97"
    mergeAlarm98  is "mergeAlarm98"
    mergeAlarm86  is "mergeAlarm86"
    mergeAlarm87  is "mergeAlarm87"
    mergeAlarm88  is "mergeAlarm88"
    mergeAlarm85  is "mergeAlarm85"
    mergeAlarm215 is "mergeAlarm215"
    mergeAlarm170 is "mergeAlarm170"
    mergeAlarm76  is "mergeAlarm76"
    mergeAlarm174 is "mergeAlarm174"
    mergeAlarm158 is "mergeAlarm158"
    mergeAlarm78  is "mergeAlarm78"
    mergeAlarm73  is "mergeAlarm73"
    mergeAlarm2   is "mergeAlarm2"
    mergeAlarm195 is "mergeAlarm195"
    mergeAlarm200 is "mergeAlarm200"
    mergeAlarm199 is "mergeAlarm199"
    mergeAlarm187 is "mergeAlarm187"
    mergeAlarm203 is "mergeAlarm203"
    mergeAlarm177 is "mergeAlarm177"
    mergeAlarm169 is "mergeAlarm169"
    mergeAlarm42  is "mergeAlarm42"
    mergeAlarm44  is "mergeAlarm44"
    mergeAlarm211 is "mergeAlarm211"
    mergeAlarm27  is "mergeAlarm27"
    mergeAlarm191 is "mergeAlarm191"
    mergeAlarm207 is "mergeAlarm207"
    mergeAlarm190 is "mergeAlarm190"
    mergeAlarm163 is "mergeAlarm163"
    mergeAlarm222 is "mergeAlarm222"
    mergeAlarm136 is "mergeAlarm136"
    mergeAlarm125 is "mergeAlarm125"
    mergeAlarm28  is "mergeAlarm28"
    mergeAlarm14  is "mergeAlarm14"
    mergeAlarm156 is "mergeAlarm156"
    mergeAlarm102 is "mergeAlarm102"
    mergeAlarm92  is "mergeAlarm92"
    mergeAlarm6   is "mergeAlarm6"
    mergeAlarm231 is "mergeAlarm231"
    mergeAlarm115 is "mergeAlarm115"
    mergeAlarm116 is "mergeAlarm116"
    mergeAlarm117 is "mergeAlarm117"
    mergeAlarm83  is "mergeAlarm83"
    mergeAlarm61  is "mergeAlarm61"
    mergeAlarm152 is "mergeAlarm152"
    mergeAlarm18  is "mergeAlarm18"
    mergeAlarm139 is "mergeAlarm139"
    mergeAlarm141 is "mergeAlarm141"
    mergeAlarm140 is "mergeAlarm140"
    mergeAlarm142 is "mergeAlarm142"
    mergeAlarm168 is "mergeAlarm168"
    mergeAlarm48  is "mergeAlarm48"
    mergeAlarm49  is "mergeAlarm49"
    mergeAlarm165 is "mergeAlarm165"
    mergeAlarm119 is "mergeAlarm119"
    mergeAlarm120 is "mergeAlarm120"
    mergeAlarm20  is "mergeAlarm20"
    mergeAlarm68  is "mergeAlarm68"
    mergeAlarm52  is "mergeAlarm52"
}
status {
    midAlarm0   is "至少有一个报警"  //取值中间变量
    midAlarm15  is "安全链断开"
    midAlarm114 is "变桨错误"
    midAlarm93  is "变桨电池1电压错误"
    midAlarm94  is "变桨电池2电压错误"
    midAlarm95  is "变桨电池3电压错误"
    midAlarm84  is "变桨电池测试"
    midAlarm96  is "变桨电池充电回路故障"
    midAlarm97  is "变桨电池充电器故障"
    midAlarm98  is "变桨电池电压错误"
    midAlarm86  is "变桨驱动器1错误"
    midAlarm87  is "变桨驱动器2错误"
    midAlarm88  is "变桨驱动器3错误"
    midAlarm85  is "变桨通讯错误"
    midAlarm215 is "变桨自主运行"
    midAlarm170 is "变频器超出工作范围"
    midAlarm76  is "变频器错误"
    midAlarm174 is "变频器入口温度过高"
    midAlarm158 is "变频器脱网"
    midAlarm78  is "变频器未就绪"
    midAlarm73  is "变频器正在加热"
    midAlarm2   is "常规错误重复"
    midAlarm195 is "齿轮箱风扇故障"
    midAlarm200 is "齿轮箱油泵故障"
    midAlarm199 is "齿轮箱油位低"
    midAlarm187 is "齿轮箱轴承1温度偏高"
    midAlarm203 is "齿轮油温度功率减小"
    midAlarm177 is "齿轮油温度过高"
    midAlarm169 is "低风切出"
    midAlarm42  is "电网电流不对称"
    midAlarm44  is "电网频率过高"
    midAlarm211 is "定子绕组温度功率减小"
    midAlarm27  is "发电机超速"
    midAlarm191 is "发电机定子绕组温度偏高"
    midAlarm207 is "发电机风扇故障"
    midAlarm190 is "发电机轴承2温度偏高"
    midAlarm163 is "风场控制停机"
    midAlarm222 is "风场通讯错误"
    midAlarm136 is "风速不一致"
    midAlarm125 is "风速计结冰"
    midAlarm28  is "高速轴超速"
    midAlarm14  is "高速轴超速开关"
    midAlarm156 is "极限开关旁路超时"
    midAlarm102 is "桨角偏差时间过长"
    midAlarm92  is "桨角不一致"
    midAlarm6   is "交流电源故障"
    midAlarm231 is "启动超时"
    midAlarm115 is "设定与实际桨角1偏差过大"
    midAlarm116 is "设定与实际桨角2偏差过大"
    midAlarm117 is "设定与实际桨角3偏差过大"
    midAlarm83  is "收桨超时"
    midAlarm61  is "手动刹车"
    midAlarm152 is "手动停机"
    midAlarm18  is "塔基急停"
    midAlarm139 is "塔筒X向1级振动"
    midAlarm141 is "塔筒X向2级振动"
    midAlarm140 is "塔筒Y向1级振动"
    midAlarm142 is "塔筒Y向2级振动"
    midAlarm168 is "通过共振区超时"
    midAlarm48  is "相电压过低"
    midAlarm49  is "相电压过高"
    midAlarm165 is "业主控制停机"
    midAlarm119 is "叶片1收桨速度过慢"
    midAlarm120 is "叶片2收桨速度过慢"
    midAlarm20  is "叶片维护"
    midAlarm68  is "液压压力过低"
    midAlarm52  is "暂态电网错误"
}
onInit {
    midAlarm0 = null
    midAlarm15  = null
    midAlarm114 = null
    midAlarm93  = null
    midAlarm94  = null
    midAlarm95  = null
    midAlarm84  = null
    midAlarm96  = null
    midAlarm97  = null
    midAlarm98  = null
    midAlarm86  = null
    midAlarm87  = null
    midAlarm88  = null
    midAlarm85  = null
    midAlarm215 = null
    midAlarm170 = null
    midAlarm76  = null
    midAlarm174 = null
    midAlarm158 = null
    midAlarm78  = null
    midAlarm73  = null
    midAlarm2   = null
    midAlarm195 = null
    midAlarm200 = null
    midAlarm199 = null
    midAlarm187 = null
    midAlarm203 = null
    midAlarm177 = null
    midAlarm169 = null
    midAlarm42  = null
    midAlarm44  = null
    midAlarm211 = null
    midAlarm27  = null
    midAlarm191 = null
    midAlarm207 = null
    midAlarm190 = null
    midAlarm163 = null
    midAlarm222 = null
    midAlarm136 = null
    midAlarm125 = null
    midAlarm28  = null
    midAlarm14  = null
    midAlarm156 = null
    midAlarm102 = null
    midAlarm92  = null
    midAlarm6   = null
    midAlarm231 = null
    midAlarm115 = null
    midAlarm116 = null
    midAlarm117 = null
    midAlarm83  = null
    midAlarm61  = null
    midAlarm152 = null
    midAlarm18  = null
    midAlarm139 = null
    midAlarm141 = null
    midAlarm140 = null
    midAlarm142 = null
    midAlarm168 = null
    midAlarm48  = null
    midAlarm49  = null
    midAlarm165 = null
    midAlarm119 = null
    midAlarm120 = null
    midAlarm20  = null
    midAlarm68  = null
    midAlarm52  = null
}
time_align { nullable }
task {
    //申明循环体变量
    def numAry = [15 , 114 , 93 , 94 , 95 , 84 , 96 , 97 , 98 , 86 ,
                  87 , 88 , 85 , 215 , 170 , 76 , 174 , 158 , 78 , 73 ,
                  2 , 195 , 200 , 199 , 187 , 203 , 177 , 169 , 42 , 44 ,
                  211 , 27 , 191 , 207 , 190 , 163 , 222 , 136 , 125 , 28 ,
                  14 , 156 , 102 , 92 , 6 , 231 , 115 , 116 , 117 , 83 , 61 ,
                  152 , 18 , 139 , 141 , 140 , 142 , 168 , 48 , 49 , 165 , 119 ,
                  120 , 20 , 68 , 52 ]

    def isAlarm = false

    //进行循环
    for (def i in numAry) {
        def alarm = delegate["falarm" + i]
        def midAlarm = delegate["midAlarm" + i]
        def ovalue = null
        if (alarm != null) {
            ovalue = alarm.value
            if (midAlarm == null || midAlarm.value != alarm.value) {
                delegate["mergeAlarm" + i] = ovalue
            }
            delegate["midAlarm" + i] = ovalue
            midAlarm = ovalue
        }
        if (!isAlarm && midAlarm == 1) {
            isAlarm = true
        }
    }

    //输出mergeAlarm0以及重算midAlarm0
    if (isAlarm) {
        if (midAlarm0 == null || midAlarm0 == 0) {
            mergeAlarm0 = 1
        }
        midAlarm0 = 1;
    } else {
        if (midAlarm0 == null || midAlarm0 == 1) {
            mergeAlarm0 = 0
        }
        midAlarm0 = 0;
    }
}