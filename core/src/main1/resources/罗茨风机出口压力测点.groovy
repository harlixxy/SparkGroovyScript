//0HLB30CP001: 罗茨风机出口压力测点
//（0HLB3AN1XB1：罗茨风机运行状态）且（0HLB2A11XQ1≤3%：冷态试验高压用风阀门开度反馈）延时10秒且0HLB30CP001≤2kPa。

import io.unimatic.scripting.groovy.DataPoint

require {
    a_0HLB3AN1XB1 is "0HLB3AN1XB1"
    a_0HLB2A11XQ1 is "0HLB2A11XQ1"
    a_0HLB30CP001 is "0HLB30CP001"
}

status {
    lastExecuteTime is "上次执行时间"
}

onInit {
    lastExecuteTime = null
}

//time_align { nullable }
time_align { latest }

output {
    out_0HLB30CP001 is "异常_0HLB30CP001"
}

//def NORMALCODE = -999999;
//
//def RFUNC = { cData, cExpection, bExpetion ->
//    if (cExpection == 1 && bExpetion.value == 0) {   //表示报警
//        bExpetion.value = 1;
//        return cData;
//    } else if (cExpection == 0 && bExpetion.value == 1) {   //表示恢复
//        bExpetion.value = 0;
//        return NORMALCODE
//    }
//}

task {
//    def conditionResult = {
//        if (a_0HLB2A11XQ1.value <= 3) {
//            return true
//        }
//        return false
//    }();
    def currentTime = a_0HLB3AN1XB1.timestamp;
    def oValue = a_0HLB3AN1XB1.value;
    if(lastExecuteTime == null || currentTime - lastExecuteTime >= 60*5){
        out_0HLB30CP001 = oValue;
        lastExecuteTime = currentTime;
    }
}