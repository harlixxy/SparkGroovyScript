require {
    origin_alarm0 is "t1"
    origin_alarm1 is "t2"
    origin_alarm2 is "t3"
    origin_alarm3 is "t4"
    origin_alarm4 is "t5"
    origin_alarm5 is "t6"
    origin_alarm6 is "t7"
    origin_alarm7 is "t8"
    origin_alarm8 is "t9"
    origin_alarm9 is "t10"
}
output {
    alarm1 is "t1"
    alarm2 is "t2"
    alarm3 is "t3"
    alarm4 is "t4"
    alarm5 is "t5"
    alarm6 is "t6"
    alarm7 is "t7"
    alarm8 is "t8"
    alarm9 is "t9"
    alarm10 is "t10"
}
status {
    s1 is ""
}
onInit {
    s1 = null
}
time_align { nullable }
task {
    if (s1 == null || origin_alarm5.timestamp - s1 >= 5 * 60)
    {
        //计算

        s1 = origin_alarm5.timestamp;
    }

}