//0HLA30CF101：冷态高压风流量测点
//（0HLB3AN1XB1：罗茨风机运行状态）取反即（罗茨风机已停止），0HLA30CF101≧10Nm3/h

import io.unimatic.scripting.groovy.DataPoint

require {
    a_0HLB3AN1XB1 is "0HLB3AN1XB1"
    a_0HLA30CF101 is "0HLA30CF101"
}

status {
    lastExecuteTime is "上次执行时间"
}

onInit {
    lastExecuteTime  = null
}

//time_align { nullable }
time_align { latest }

output {
    out_0HLA30CF101 is "异常_0HLA30CF101"
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
    def currentTime = a_0HLB3AN1XB1.timestamp;
    def oValue = a_0HLB3AN1XB1.value;
    if(lastExecuteTime == null || currentTime - lastExecuteTime >= 60*5){
        out_0HLA30CF101 = oValue;
        lastExecuteTime = currentTime;
    }
}