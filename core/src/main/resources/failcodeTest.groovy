//failcode点位值相同,合并后输出
require {
    failcode is "输入状态码"
}

status {
    lastfailcode is "上一个状态码"
}

onInit {
    lastfailcode = null
}

time_align { nullable }

output {
    mergefailcode is "合并状态码"
}

def failcodelist = [0D, 1D, 2D, 4D, 5D, 6D, 7D, 8D, 9D, 10D, 11D, 12D, 120D, 121D, 122D, 123D, 130D,
                    131D, 132D, 133D, 140D, 141D, 142D, 143D, 132D, 150D, 151D, 152D, 160D, 161D,
                    162D, 163D, 180D, 181D, 182D, 183D, 184D, 1005D, 1006D, 1007D, 1008D, 1009D,
                    1010D, 4002D, 4003D, 4004D, 4005D, 4006D, 4007D, 4008D, 4009D, 4010D, 4011D,
                    4014D, 4015D, 4020D, 4099D, 4101D, 4102D, 4103D, 4104D, 4105D, 4106D, 4107D,
                    4108D, 4109D, 4110D, 4111D, 4112D, 4113D, 4119D, 5001D, 5002D, 5003D, 5004D,
                    5005D, 5006D, 5007D, 6001D, 6002D, 6003D, 6004D, 6005D, 6006D, 6007D, 6008D,
                    6009D, 6010D, 6011D, 6012D, 6013D, 6014D, 6015D, 6016D, 6101D, 6102D, 6103D,
                    6104D, 6105D, 6106D, 6107D, 6108D, 6109D, 6110D, 7001D, 7002D, 7003D, 7004D,
                    7005D, 7006D, 7007D, 7008D, 7009D, 7010D, 7011D, 7012D, 8001D, 8002D, 8003D,
                    8004D, 8005D, 8006D, 8007D, 8008D, 8009D, 8010D, 8011D, 9001D, 9002D, 9003D,
                    9004D, 9005D, 9006D, 9007D, 9008D, 9009D, 9010D, 9011D, 9012D, 9013D, 9014D,
                    9015D, 9016D, 9018D, 9019D, 9020D, 9021D, 9022D, 9023D, 9024D, 9025D, 9026D,
                    9027D, 9028D, 9029D, 9030D, 9031D, 9032D, 9033D, 9034D, 9035D, 9036D, 9037D,
                    9038D, 9039D, 9040D, 9041D, 9042D, 10001D, 10002D, 10003D, 10004D, 10005D,
                    10006D, 10007D, 10008D, 10009D, 10010D, 10011D, 10012D, 10013D, 10014D, 10015D,
                    10016D, 10017D, 11001D, 11002D, 11003D, 11004D, 12001D, 12002D, 12003D, 12004D,
                    12005D, 12006D, 12007D, 12008D, 12009D, 12010D, 12011D, 12012D, 12013D, 12014D,
                    12015D, 12016D, 13001D, 13002D, 13003D, 13004D, 13005D, 13006D, 13007D, 13008D,
                    13009D, 13010D, 13011D, 13012D, 13013D, 13014D, 13015D, 13016D, 14001D, 14002D,
                    14003D, 14004D, 14005D, 14006D, 14007D, 14008D, 14009D, 14010D, 14011D, 14012D,
                    14013D, 14014D, 14015D, 17001D, 17002D, 19001D, 19005D]

task {
    if (failcode != null) {
        def current = (Double) failcode
        if (failcodelist.contains(current.value)) {
            if (lastfailcode == null) {
                mergefailcode = failcode
            } else {
                def last = (Double) lastfailcode
                if (last.value != current.value) mergefailcode = failcode
            }
            lastfailcode = failcode
        }
    }
}