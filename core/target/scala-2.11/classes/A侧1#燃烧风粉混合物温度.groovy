//1HHE10CT011：A侧1#燃烧风粉混合物温度
//1、（1CBB1AN2XB1：给粉机2运行状态），1HHE10CT011≧1HLA10CT002+20；
//2、（1CBB1AN2XB1：给粉机2运行状态）取反即（给粉机停止），1HHE10CT011≦1HLA10CT002；

import io.unimatic.scripting.groovy.DataPoint

require {
    a_1CBB1AN2XB1 is "1CBB1AN2XB1"
    a_1HHE10CT011 is "1HHE10CT011"
    a_1HLA10CT002 is "1HLA10CT002"
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
    out_1HHE10CT011 is "异常_1HHE10CT011"
}
//
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
    def currentTime = a_1CBB1AN2XB1.timestamp;
    def oValue = a_1CBB1AN2XB1.value;
    if (lastExecuteTime == null || currentTime - lastExecuteTime >= 60 * 5 )
    {
        //逻辑计算
        out_1HHE10CT011 = oValue;
        lastExecuteTime = currentTime;
    }
}