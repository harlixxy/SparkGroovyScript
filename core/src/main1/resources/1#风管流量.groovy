//1HLA10CF001：1#风管流量
//1HBK10CP001≧200Pa且1HLA1AA2XQ1≧15%且1HLA10CF001≦5 Nm3/h。

import io.unimatic.scripting.groovy.DataPoint

require {
    a_1HBK10CP001 is "1HBK10CP001"
    a_1HLA1AA2XQ1 is "1HLA1AA2XQ1"
    a_1HLA10CF001 is "1HLA10CF001"
}

status {
    lastExecuteTime is "上次执行时间"
}

onInit {
    lastExecuteTime = null
    //beforeExpetion = new DataPoint()
}

//time_align { nullable }
time_align { latest }

output {
    out_1HLA10CF001 is "异常_1HLA10CF001"
}

//def NORMALCODE = -999999;

task {
    def currentTime = a_1HBK10CP001.timestamp
    def oValue = a_1HBK10CP001.value
    if (lastExecuteTime == null || currentTime - lastExecuteTime >= 60 * 5 )
    {
        //逻辑计算
        out_1HLA10CF001 = oValue
        lastExecuteTime = currentTime
    }

    //根据当前条件判断 是否有问题
    //1HBK10CP001≧200Pa且1HLA1AA2XQ1≧15%且1HLA10CF001≦5 Nm3/h。
//    def currentExpection = 0
//    if (a_1HBK10CP001.value >= 200 && a_1HLA1AA2XQ1.value >= 15 && a_1HLA10CF001.value <= 5) {
//        currentExpection = 1
//    }
//    while (timestamp - origin <= 5*60){
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask(){
//            @Override
//            public void run() {
//                timestamp = timestamp + 60;
//            }
//
//        }, 0,300000);
//    }
//    long timestamp = 0L;
//    timestamp = System.currentTimeMillis();
//    System.out.println(timestamp);
//    if (timestamp - time >= 10){
//        time = timestamp;
//        out_1HLA10CF001 = 1111;
//        System.out.println(out_1HLA10CF001);
//    }
//    if(timestamp - origin == 5*60){
//        origin = timestamp;
//        out_1HLA10CF001 = 1111;
//        System.out.println(out_1HLA10CF001);
//    }


    //out_1HLA10CF001 = RFUNC(a_1HLA10CF001.value, currentExpection, beforeExpetion);
}