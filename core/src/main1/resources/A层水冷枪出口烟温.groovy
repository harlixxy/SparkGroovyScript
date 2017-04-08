//1HAK10CT001：A层水冷枪出口烟温
//（PC炉已停止运行延时172800秒且1HAK10CT001≧200℃）

import io.unimatic.scripting.groovy.DataPoint

require {
    a_1HAK10CT001   is "1HAK10CT001"

    dx4_0CFB1CH3XB1 is "0CFB1CH3XB1"
    dx4_0CFB1CH4XB1 is "0CFB1CH4XB1"
    dx4_0CRA1A11XA1 is "0CRA1A11XA1"

    dx4_0CFB1H13XB1 is "0CFB1H13XB1"
    dx4_0CFB1H14XB3 is "0CFB1H14XB3"
    dx4_0CRA1A21XA1 is "0CRA1A21XA1"
    dx4_1CBB1AN1XB1 is "1CBB1AN1XB1"
    dx4_1CBB1AN2XB1 is "1CBB1AN2XB1"

    dx4_1CBB1AN3XB1 is "1CBB1AN3XB1"
    dx4_1CBB1AN4XB1 is "1CBB1AN4XB1"
    dx4_0CFB1CH1XB1 is "0CFB1CH1XB1"

    dx4_0CFB1CH2XB1 is "0CFB1CH2XB1"
    dx4_0CFB1H11XB1 is "0CFB1H11XB1"
    dx4_0CFB1H12XB1 is "0CFB1H12XB1"
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
    out_1HAK10CT001 is "异常_1HAK10CT001"
}


task {

    def currentTime = a_1HAK10CT001.timestamp;
    def oValue = a_1HAK10CT001.value;
    if (lastExecuteTime == null || currentTime - lastExecuteTime >= 60 * 5 )
    {
        //逻辑计算
        out_1HAK10CT001 = oValue;
        lastExecuteTime = currentTime;
    }


}