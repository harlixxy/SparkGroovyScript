require {
    alarm1 is "t1"
    alarm2 is "t2"
}
output {
    mergeAlarm0 is "正常"
    mergeAlarm1 is "t1"
    mergeAlarm2 is "t2"
    mergeAlarm3 is "t3"
    mergeAlarm4 is "t4"
    mergeAlarm5 is "t5"
    mergeAlarm6 is "t6"
    mergeAlarm7 is "t7"
    mergeAlarm8 is "t8"
    mergeAlarm9 is "t9"
    mergeAlarm10 is "t1"
    mergeAlarm11 is "t2"
    mergeAlarm12 is "t3"
    mergeAlarm13 is "t4"
    mergeAlarm14 is "t5"
    mergeAlarm15 is "t6"
    mergeAlarm16 is "t7"
    mergeAlarm17 is "t8"
    mergeAlarm18 is "t9"
    mergeAlarm19 is "t1"
    mergeAlarm20 is "t2"
    mergeAlarm21 is "t3"
    mergeAlarm22 is "t4"
    mergeAlarm23 is "t5"
    mergeAlarm24 is "t6"
    mergeAlarm25 is "t7"
    mergeAlarm26 is "t8"
    mergeAlarm27 is "t9"
    mergeAlarm28 is "t9"
    mergeAlarm29 is "t9"
    mergeAlarm30 is "t9"
    mergeAlarm31 is "t9"
    mergeAlarm32 is "t9"
}
status {
    midAlarm0 is "正常"
    midAlarm1 is "t1"
    midAlarm2 is "t2"
    midAlarm3 is "t3"
    midAlarm4 is "t4"
    midAlarm5 is "t5"
    midAlarm6 is "t6"
    midAlarm7 is "t7"
    midAlarm8 is "t8"
    midAlarm9 is "t9"
    midAlarm10 is "t1"
    midAlarm11 is "t2"
    midAlarm12 is "t3"
    midAlarm13 is "t4"
    midAlarm14 is "t5"
    midAlarm15 is "t6"
    midAlarm16 is "t7"
    midAlarm17 is "t8"
    midAlarm18 is "t9"
    midAlarm19 is "t1"
    midAlarm20 is "t2"
    midAlarm21 is "t3"
    midAlarm22 is "t4"
    midAlarm23 is "t5"
    midAlarm24 is "t6"
    midAlarm25 is "t7"
    midAlarm26 is "t8"
    midAlarm27 is "t9"
    midAlarm28 is "t9"
    midAlarm29 is "t9"
    midAlarm30 is "t9"
    midAlarm31 is "t9"
    midAlarm32 is "t9"
}
onInit {
    midAlarm0 is "正常"
    midAlarm1 is "t1"
    midAlarm2 is "t2"
    midAlarm3 is "t3"
    midAlarm4 is "t4"
    midAlarm5 is "t5"
    midAlarm6 is "t6"
    midAlarm7 is "t7"
    midAlarm8 is "t8"
    midAlarm9 is "t9"
    midAlarm10 is "t1"
    midAlarm11 is "t2"
    midAlarm12 is "t3"
    midAlarm13 is "t4"
    midAlarm14 is "t5"
    midAlarm15 is "t6"
    midAlarm16 is "t7"
    midAlarm17 is "t8"
    midAlarm18 is "t9"
    midAlarm19 is "t1"
    midAlarm20 is "t2"
    midAlarm21 is "t3"
    midAlarm22 is "t4"
    midAlarm23 is "t5"
    midAlarm24 is "t6"
    midAlarm25 is "t7"
    midAlarm26 is "t8"
    midAlarm27 is "t9"
    midAlarm28 is "t9"
    midAlarm29 is "t9"
    midAlarm30 is "t9"
    midAlarm31 is "t9"
    midAlarm32 is "t9"

    midAlarm33 is "t1"
    midAlarm34 is "t2"
    midAlarm35 is "t3"
    midAlarm36 is "t4"
    midAlarm37 is "t5"
    midAlarm38 is "t6"
    midAlarm39 is "t7"
    midAlarm40 is "t8"
    midAlarm41 is "t9"
    midAlarm42 is "t1"
    midAlarm43 is "t2"
    midAlarm44 is "t3"
    midAlarm45 is "t4"
    midAlarm46 is "t5"
    midAlarm47 is "t6"
    midAlarm48 is "t7"
    midAlarm49 is "t8"
    midAlarm50 is "t9"
    midAlarm51 is "t1"
    midAlarm52 is "t2"
    midAlarm53 is "t3"
    midAlarm54 is "t4"
    midAlarm55 is "t5"
    midAlarm56 is "t6"
    midAlarm57 is "t7"
    midAlarm58 is "t8"
    midAlarm59 is "t9"
    midAlarm60 is "t9"
    midAlarm61 is "t9"
    midAlarm62 is "t9"
    midAlarm63 is "t9"
    midAlarm64 is "t9"
}
time_align { nullable }
task {
    //如果全 0，则表示正常，设置 mergeAlarm0 = 0，反之则为 1
    def alarmAry = [ alarm1 ]
    def isAlaram = alarmAry.any { item -> item != null && item != 0 }
    if (isAlaram) {
        if (midAlarm0 == null  || midAlarm0 == 0){
            mergeAlarm0 = 1
        }
        midAlarm0 = 1;
    }else{
        if (midAlarm0 == null || midAlarm0 == 1){
            mergeAlarm0 = 0
        }
        midAlarm0 = 0;
    }


    //先解析成 alarmTemp 再和 midAlarm 进行比较
    def alarmTempValue = null
    def midAlarm = null
    for (int i = 0; i < 32; i++) {
        def idx = i + 1
        int cv = delegate["alarm" + ((int) (i / 32) + 1)].value
        def v = cv & (1 << (i % 32))

        alarmTempValue = (v == 0 ? 0 : 1)
        midAlarm = delegate["midAlarm" + idx]
        if (midAlarm == null || midAlarm.value != alarmTempValue) {
            delegate["mergeAlarm" + idx] = alarmTempValue
        }
        delegate["midAlarm" + idx] = alarmTempValue
        //println("mergeAlarm${idx} = " + delegate["mergeAlarm" + idx])
    }
}