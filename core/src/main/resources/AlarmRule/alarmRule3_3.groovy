//中电投蒙东协合霍林河

require {
    failcode is "输入状态码"
}

status {
    lastfailcode is "上一个状态码"
    midAlarm0 is "存在报警"
}

onInit {
    lastfailcode = null
    midAlarm0 = null
}

time_align { nullable }

output {
    mergeAlarm is "mergeAlarm"
    mergeAlarm0 is "mergeAlarm0"
}

def failcodelist = [0D, 1001D, 1002D, 1003D, 1004D, 1005D, 1006D, 1007D, 1008D, 1009D, 1010D, 1011D, 1012D,
                    1013D, 1014D, 1015D, 2021D, 2022D, 2031D, 2032D, 2033D, 2041D, 2042D, 2043D, 2021D,
                    2022D, 2031D, 2032D, 2033D, 2041D, 2042D, 2043D, 3001D, 3002D, 3003D, 3004D, 3005D,
                    3006D, 3007D, 3008D, 3009D, 3010D, 3011D, 3012D, 3013D, 3014D, 3015D, 3016D, 4001D,
                    4002D, 4003D, 4004D, 4005D, 4006D, 4007D, 4008D, 4009D, 4010D, 4011D, 4012D, 4013D,
                    4014D, 4015D, 4016D, 4017D, 4018D, 4019D, 4020D, 4021D, 4022D, 4023D, 4024D, 4025D,
                    4026D, 4027D, 4028D, 4029D, 4030D, 4031D, 4032D, 4033D, 4034D, 4035D, 4036D, 4037D,
                    4038D, 4039D, 4040D, 4041D, 4042D, 4043D, 4044D, 4045D, 4046D, 4047D, 4048D, 4049D,
                    4050D, 4051D, 4052D, 4053D, 4054D, 4055D, 4056D, 4057D, 4058D, 4059D, 4062D, 4061D,
                    4062D, 4063D, 4064D, 4065D, 6001D, 6002D, 6003D, 6004D, 6005D, 6006D, 6007D, 6008D,
                    6009D, 6010D, 6011D, 6012D, 6013D, 6014D, 6015D, 6016D, 6017D, 6018D, 6019D, 6020D,
                    6021D, 6022D, 6023D, 7001D, 7002D, 7003D, 7004D, 7005D, 7006D, 7007D, 7008D, 7009D,
                    7010D, 7011D, 7012D, 7013D, 7014D, 7015D, 8001D, 8002D, 8003D, 8004D, 8005D, 8006D,
                    8007D, 8008D, 8009D, 8010D, 8011D, 8012D, 8013D, 8014D, 9001D, 9002D, 9003D, 9004D,
                    9005D, 9006D, 9007D, 9008D, 9009D, 9010D, 9011D, 9012D, 9013D, 10001D, 10002D,
                    10003D, 10004D, 10005D, 10006D, 11001D, 11002D, 11003D, 11004D, 11005D, 11006D,
                    11007D, 11008D, 11009D, 11010D, 11011D, 11012D, 11013D, 11014D, 11015D, 11016D,
                    11017D, 11018D, 12001D, 12002D, 12003D, 12004D, 12005D, 12006D, 12007D, 12008D,
                    12009D, 12010D, 13001D, 13002D, 13003D, 13004D, 13005D, 13006D, 13007D, 13008D,
                    13009D, 13010D, 13011D, 15001D, 15002D, 15003D, 15004D, 15005D, 15006D, 15007D,
                    15008D, 15009D, 15010D, 15011D, 15012D, 15013D, 16001D, 16002D, 16003D, 16004D,
                    16005D, 16006D, 16007D, 16008D, 16009D, 16010D, 16011D, 16012D, 16013D, 16014D,
                    16015D, 16016D, 16017D, 17001D, 17002D, 17003D, 17004D, 17005D, 17006D, 17007D,
                    17008D, 17009D, 17010D, 17011D, 26001D, 26002D, 26003D, 26004D, 26005D, 26006D]

task {
    if (failcode != null) {
        def isAlarm = false
        def current = (Double) failcode
        if (failcodelist.contains(current)) {
            if (lastfailcode == null || lastfailcode != current) {
                mergeAlarm = failcode
            }
            isAlarm = (current != 0)
            lastfailcode = failcode

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
    }
}