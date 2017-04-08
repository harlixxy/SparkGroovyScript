
import dao.IMaterialParam
import dao.impl.MaterialParam
import dao.impl.StressAnalysis
import net.sf.json.JSONObject


enum Status {
    bjName,//部件名称
    material//材料
}

require {
    dqpress is "部件压力"
    dqinnerT is "部件筒体内壁温度"
    dqoutT is "部件筒体外壁温度"
}

status {
    //蠕变寿命计算
    lasttimeStart is "起始时间"
    lasttimeEnd is "结束时间"
    lastCreepMaxTemperature is "本次计算前蠕变最高筒体温度"
    lastCreepMinTemperature is "本次计算前蠕变最低筒体温度"
    lastCreepMaxStress is "本次计算前最高蠕变应力"
    lastCreepMinStress is "本次计算前最低蠕变应力"
    lastcreepStressi is "该时间段内的蠕变寿命损耗"
    lasttotalCreepStress is "上次计算后总的蠕变寿命损耗"
    periodOfTime is "蠕变寿命计算时间段"
    startTemperature is "高温段内温度变化计算用初值"
    I is "蠕变寿命损耗计算温度范围标识符I"
    II is "蠕变寿命损耗计算温度范围标识符II"
}


onInit {
    //蠕变寿命计算
    lasttimeStart = null
    lasttimeEnd = null
    lastCreepMaxTemperature = null
    lastCreepMinTemperature = null
    lastCreepMaxStress = null
    lastCreepMinStress = null
    lastcreepStressi = null
    lasttotalCreepStress = null
    periodOfTime = 0
    startTemperature = null
    I = 0
    II = 0
}

time_align { nullable }

output {

    //蠕变寿命计算
    timeStart is "起始时间"
    timeEnd is "结束时间"
    maxCreepTemperature is "最高筒体温度"
    minCreepTemperature is "最低筒体温度"
    maxCreepStress is "最高蠕变应力"
    minCreepStress is "最低蠕变应力"
    creepStressi is "该时间段内的蠕变寿命损耗"
    totalCreepStress is "总的蠕变寿命损耗"


}
//计算疲劳寿命损耗计算用应力
task {
    //计算疲劳寿命损耗计算用应力
    System.out.println("###############原始数据################")
    System.out.println("时间戳：" + dqinnerT.timestamp);
    System.out.println("内壁温度：" + dqinnerT.value);

    //获取特征参数
    IMaterialParam materialParam = new MaterialParam();
    List<Map<String,String>> list = materialParam.getmaterialParam(bjName);
    double kcl = Double.valueOf(list.get(1).get("KCL").toString());

    //蠕变计算用应力
   // System.out.println("蠕变计算用应力====" + kcl + " * " + dqpress);
    double yingliDC = kcl * dqpress;
    System.out.println("计算值:" + yingliDC);
    StressAnalysis stressAnalysis = new StressAnalysis();
    def now = dqinnerT.timestamp;

    //第一次运行，则为中间变量赋初值
    if( 0 == lastCreepMaxStress){
        I = 0;
        II = 0;
        //时间段为零
        periodOfTime = 0;

    }
    //当温度大于400时，需要计算蠕变损耗
    //一：若t≥500，且Ⅰ=0，Ⅱ=0
    if(dqinnerT.value >= 500 && I == 0 && II == 0){
        I = 1;
        II = 1;
        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        startTemperature =  dqinnerT.value;
        lastCreepMaxStress =  yingliDC;
        lastCreepMinStress = yingliDC;
        //开始计时并累加
        lasttimeStart = now;
    }
    //二:若t≥450，且Ⅰ=0，Ⅱ=0
    if(dqinnerT.value >= 450 && I == 0 && II == 0){
        I = 1;
        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        lastCreepMaxStress =  yingliDC;
        lastCreepMinStress = yingliDC;
        //开始计时并累加
        lasttimeStart = now;
    }

    //三:若450<t<500，且Ⅰ=1，Ⅱ=0
    if(  450 < dqinnerT.value && dqinnerT.value < 500 && I == 1 && II == 0){
        if(dqinnerT.value > lastCreepMaxTemperature){
            lastCreepMaxTemperature = dqinnerT.value;
        }
        if(dqinnerT.value < lastCreepMinTemperature){
            lastCreepMinTemperature = dqinnerT.value;
        }
        if(yingliDC > lastCreepMaxStress){
            lastCreepMaxStress = yingliDC;
        }
        if(yingliDC < lastCreepMinStress){
            lastCreepMinStress = yingliDC;
        }

        //计算periodOfTime
        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(),now.toString(),"yyyy-MM-dd HH:mm:ss SSS");
        if(periodOfTime > 100 ){
            //拼接传入参数
            JSONObject jsonObjectCreep = new JSONObject();
            jsonObjectCreep.put("material",material);
            jsonObjectCreep.put("maxCreepStress",lastCreepMaxStress);
            //计算用温度选取:若此时该部件计算温度判断用的标识符I=1，II=1，则以最高问题tmax作为查该部件筒体材料持久强度的温度；
            // 若I=1，II=0，则选用500°C下SA-335p91材料的持久强度
            if(I == 1 && II == 1){
                jsonObjectCreep.put("temperature",lastCreepMaxTemperature);
            }else if(I == 1 && II ==0){
                if("SA-335P91" == material){
                    jsonObjectCreep.put("temperature","500");
                }else if("12Cr1MoV" == material){
                    jsonObjectCreep.put("temperature","450");
                }else if("SA-335P92" == material){
                    jsonObjectCreep.put("temperature","520");
                }else if("SA-336F12" == material){
                    jsonObjectCreep.put("temperature","450");
                }
            }
            jsonObjectCreep.put("periodOfTime",periodOfTime);
            jsonObjectCreep.put("totalCreepStress",lasttotalCreepStress);
            String inputStringCreep = jsonObjectCreep.toString();

            //蠕变寿命计算
            String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

            //time_end$ = time$
            lasttimeEnd = now;

            //蠕变寿命存储
            timeStart = lasttimeStart;
            timeEnd = lasttimeEnd;
            maxCreepTemperature = lastCreepMaxTemperature;
            minCreepTemperature = lastCreepMinTemperature;
            maxCreepStress = lastCreepMaxStress;
            minCreepStress = lastCreepMinStress;

            JSONObject outputJsonCreep =  JSONObject.fromObject(ouputStringCreep);
            lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
            creepStressi = lastcreepStressi;
            lasttotalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
            totalCreepStress = lasttotalCreepStress;

            System.out.println("###############蠕变计算的值################")
            System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
            System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
            System.out.println("本次蠕变寿命损耗：" + creepStressi)
            System.out.println("总的蠕变寿命损耗：" + totalCreepStress)

            //将实时计算的筒体壁温dqinnerT分别赋给lastCreepMaxTemperature，lastCreepMinTemperature，startTemperature
            //实时计算的蠕变应力赋给lastCreepMaxStress，lastCreepMinStress
            lastCreepMaxTemperature = dqinnerT.value;
            lastCreepMinTemperature = dqinnerT.value;
            startTemperature = dqinnerT.value;
            lastCreepMaxStress = yingliDC;
            lastCreepMinStress = yingliDC;

            //periodOfTime清零重新开始计时
            periodOfTime = 0;
            lasttimeStart = lasttimeEnd;
        }
    }
    //四：若t<450，且Ⅰ=1，Ⅱ=0
    if(dqinnerT.value < 450 && I == 1 && II == 0){
        //计算持续时间
        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(),now.toString(),"yyyy-MM-dd HH:mm:ss SSS");
        //拼接传入参数
        JSONObject jsonObjectCreep = new JSONObject();
        jsonObjectCreep.put("material",material);
        jsonObjectCreep.put("maxCreepStress",lastCreepMaxStress);
        //计算用温度选取:若此时该部件计算温度判断用的标识符I=1，II=1，则以最高问题tmax作为查该部件筒体材料持久强度的温度；若I=1，II=0，则选用500°C下SA-335p91材料的持久强度
        if(I == 1 && II == 1){
            jsonObjectCreep.put("temperature",lastCreepMaxTemperature);
        }else if(I == 1 && II ==0){
            if("SA-335P91" == material){
                jsonObjectCreep.put("temperature","500");
            }else if("12Cr1MoV" == material){
                jsonObjectCreep.put("temperature","450");
            }else if("SA-335P92" == material){
                jsonObjectCreep.put("temperature","520");
            }else if("SA-336F12" == material){
                jsonObjectCreep.put("temperature","450");
            }
        }
        jsonObjectCreep.put("periodOfTime",periodOfTime);
        jsonObjectCreep.put("totalCreepStress",lasttotalCreepStress);
        String inputStringCreep = jsonObjectCreep.toString();
        //蠕变寿命计算
        String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

        //time_end$ = time$
        lasttimeEnd = now;

        //蠕变寿命存储
        timeStart = lasttimeStart;
        timeEnd = lasttimeEnd;
        maxCreepTemperature = lastCreepMaxTemperature;
        minCreepTemperature = lastCreepMinTemperature;
        maxCreepStress = lastCreepMaxStress;
        minCreepStress = lastCreepMinStress;

        JSONObject outputJsonCreep =  JSONObject.fromObject(ouputStringCreep);
        lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
        creepStressi = lastcreepStressi;
        lasttotalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
        totalCreepStress = lasttotalCreepStress;

        System.out.println("###############蠕变计算的值################")
        System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
        System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
        System.out.println("本次蠕变寿命损耗：" + creepStressi)
        System.out.println("总的蠕变寿命损耗：" + totalCreepStress)

        //将实时计算的筒体壁温dqinnerT分别赋给lastCreepMaxTemperature，lastCreepMinTemperature，startTemperature
        //实时计算的蠕变应力赋给lastCreepMaxStress，lastCreepMinStress
        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        startTemperature = dqinnerT.value;
        lastCreepMaxStress = yingliDC;
        lastCreepMinStress = yingliDC;

        //periodOfTime清零
        periodOfTime = 0;
        I = 0
    }
    //五：若t≥500，且Ⅰ=1，Ⅱ=0
    if(dqinnerT.value >= 500 && I == 1 && II == 0){
        //计算持续时间
        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(),now.toString(),"yyyy-MM-dd HH:mm:ss SSS");
        //拼接传入参数
        JSONObject jsonObjectCreep = new JSONObject();
        jsonObjectCreep.put("material",material);
        jsonObjectCreep.put("maxCreepStress",lastCreepMaxStress);
        //计算用温度选取:若此时该部件计算温度判断用的标识符I=1，II=1，则以最高问题tmax作为查该部件筒体材料持久强度的温度；
        // 若I=1，II=0，则选用500°C下SA-335p91材料的持久强度
        if(I == 1 && II == 1){
            jsonObjectCreep.put("temperature",lastCreepMaxTemperature);
        }else if(I == 1 && II ==0){
            if("SA-335P91" == material){
                jsonObjectCreep.put("temperature","500");
            }else if("12Cr1MoV" == material){
                jsonObjectCreep.put("temperature","450");
            }else if("SA-335P92" == material){
                jsonObjectCreep.put("temperature","520");
            }else if("SA-336F12" == material){
                jsonObjectCreep.put("temperature","450");
            }
        }
        jsonObjectCreep.put("periodOfTime",periodOfTime);
        jsonObjectCreep.put("totalCreepStress",lasttotalCreepStress);
        String inputStringCreep = jsonObjectCreep.toString();
        //蠕变寿命计算
        String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

        //time_end$ = time$
        lasttimeEnd = now;

        //蠕变寿命存储
        timeStart = lasttimeStart;
        timeEnd = lasttimeEnd;
        maxCreepTemperature = lastCreepMaxTemperature;
        minCreepTemperature = lastCreepMinTemperature;
        maxCreepStress = lastCreepMaxStress;
        minCreepStress = lastCreepMinStress;

        JSONObject outputJsonCreep =  JSONObject.fromObject(ouputStringCreep);
        lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
        totalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
        creepStressi = lastcreepStressi;
        totalCreepStress = lasttotalCreepStress;

        System.out.println("###############蠕变计算的值################")
        System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
        System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
        System.out.println("本次蠕变寿命损耗：" + creepStressi)
        System.out.println("总的蠕变寿命损耗：" + totalCreepStress)

        //将实时计算的筒体壁温dqinnerT分别赋给lastCreepMaxTemperature，lastCreepMinTemperature，startTemperature
        //实时计算的蠕变应力赋给lastCreepMaxStress，lastCreepMinStress
        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        startTemperature = dqinnerT.value;
        lastCreepMaxStress = yingliDC;
        lastCreepMinStress = yingliDC;

        //periodOfTime清零重新开始计时
        periodOfTime = 0;
        lasttimeStart = lasttimeEnd;

        II = 1;
        startTemperature = dqinnerT.value;
    }
    //六：若t<500，且Ⅰ=1，Ⅱ=1
    if(dqinnerT.value < 500 && I == 1 && II == 1 ){
        //计算持续时间
        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(),now.toString(),"yyyy-MM-dd HH:mm:ss SSS");
        //拼接传入参数
        JSONObject jsonObjectCreep = new JSONObject();
        jsonObjectCreep.put("material",material);
        jsonObjectCreep.put("maxCreepStress",lastCreepMaxStress);
        //计算用温度选取:若此时该部件计算温度判断用的标识符I=1，II=1，则以最高问题tmax作为查该部件筒体材料持久强度的温度；
        // 若I=1，II=0，则选用500°C下SA-335p91材料的持久强度
        if(I == 1 && II == 1){
            jsonObjectCreep.put("temperature",lastCreepMaxTemperature);
        }else if(I == 1 && II ==0){
            if("SA-335P91" == material){
                jsonObjectCreep.put("temperature","500");
            }else if("12Cr1MoV" == material){
                jsonObjectCreep.put("temperature","450");
            }else if("SA-335P92" == material){
                jsonObjectCreep.put("temperature","520");
            }else if("SA-336F12" == material){
                jsonObjectCreep.put("temperature","450");
            }
        }
        jsonObjectCreep.put("periodOfTime",periodOfTime);
        jsonObjectCreep.put("totalCreepStress",lasttotalCreepStress);
        String inputStringCreep = jsonObjectCreep.toString();
        //蠕变寿命计算
        String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

        //time_end$=time$
        lasttimeEnd = now ;

        //蠕变寿命存储
        timeStart = lasttimeStart;
        timeEnd = lasttimeEnd;
        maxCreepTemperature = lastCreepMaxTemperature;
        minCreepTemperature = lastCreepMinTemperature;
        maxCreepStress = lastCreepMaxStress;
        minCreepStress = lastCreepMinStress;

        JSONObject outputJsonCreep =  JSONObject.fromObject(ouputStringCreep);
        lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
        totalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
        creepStressi = lastcreepStressi;
        totalCreepStress = lasttotalCreepStress;

        System.out.println("###############蠕变计算的值################")
        System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
        System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
        System.out.println("本次蠕变寿命损耗：" + creepStressi)
        System.out.println("总的蠕变寿命损耗：" + totalCreepStress)

        //?????这里未计算，需要讲实时计算温度等赋给相应值吗
        //将实时计算的筒体壁温dqinnerT分别赋给lastCreepMaxTemperature，lastCreepMinTemperature，startTemperature
        //实时计算的蠕变应力赋给lastCreepMaxStress，lastCreepMinStress
        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        startTemperature = dqinnerT.value;
        lastCreepMaxStress = yingliDC;
        lastCreepMinStress = yingliDC;

        II = 0;
        //periodOfTime清零重新开始计时
        periodOfTime = 0;
        lasttimeStart = lasttimeEnd;

    }
    //七：若t>500，且Ⅰ=1，Ⅱ=1
    if(dqinnerT.value > 500 && I == 1 && II == 1){
        if(dqinnerT.value > lastCreepMaxTemperature ){
            lastCreepMaxTemperature = dqinnerT.value;
        }
        if(dqinnerT.value < lastCreepMinTemperature){
            lastCreepMinTemperature = dqinnerT.value;
        }
        if(yingliDC > lastCreepMaxStress){
            lastCreepMaxStress = yingliDC;
        }
        if(yingliDC < lastCreepMinStress){
            lastCreepMinStress = yingliDC;
        }
        //新增：若|t-tt0|≥5
        if(Math.abs(dqinnerT.value - startTemperature) >= 5){
            //计算持续时间
            periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(),now.toString(),"yyyy-MM-dd HH:mm:ss SSS");
            //拼接传入参数
            JSONObject jsonObjectCreep = new JSONObject();
            jsonObjectCreep.put("material",material);
            jsonObjectCreep.put("maxCreepStress",lastCreepMaxStress);
            //计算用温度选取:若此时该部件计算温度判断用的标识符I=1，II=1，则以最高问题tmax作为查该部件筒体材料持久强度的温度；
            // 若I=1，II=0，则选用500°C下SA-335p91材料的持久强度
            if(I == 1 && II == 1){
                jsonObjectCreep.put("temperature",lastCreepMaxTemperature);
            }else if(I == 1 && II ==0){
                if("SA-335P91" == material){
                    jsonObjectCreep.put("temperature","500");
                }else if("12Cr1MoV" == material){
                    jsonObjectCreep.put("temperature","450");
                }else if("SA-335P92" == material){
                    jsonObjectCreep.put("temperature","520");
                }else if("SA-336F12" == material){
                    jsonObjectCreep.put("temperature","450");
                }
            }
            jsonObjectCreep.put("periodOfTime",periodOfTime);
            jsonObjectCreep.put("totalCreepStress",lasttotalCreepStress);
            String inputStringCreep = jsonObjectCreep.toString();
            //蠕变寿命计算
            String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

            //time_end$ = time$
            lasttimeEnd = now;

            //蠕变寿命存储
            timeStart = lasttimeStart;
            timeEnd = lasttimeEnd;
            maxCreepTemperature = lastCreepMaxTemperature;
            minCreepTemperature = lastCreepMinTemperature;
            maxCreepStress = lastCreepMaxStress;
            minCreepStress = lastCreepMinStress;

            JSONObject outputJsonCreep =  JSONObject.fromObject(ouputStringCreep);
            lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
            creepStressi = lastcreepStressi;
            lasttotalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
            totalCreepStress = lasttotalCreepStress;

            System.out.println("###############蠕变计算的值################")
            System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
            System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
            System.out.println("本次蠕变寿命损耗：" + creepStressi)
            System.out.println("总的蠕变寿命损耗：" + totalCreepStress)

            //将实时计算的筒体壁温dqinnerT分别赋给lastCreepMaxTemperature，lastCreepMinTemperature，startTemperature
            //实时计算的蠕变应力赋给lastCreepMaxStress，lastCreepMinStress
            lastCreepMaxTemperature = dqinnerT.value;
            lastCreepMinTemperature = dqinnerT.value;
            startTemperature = dqinnerT.value;
            lastCreepMaxStress = yingliDC;
            lastCreepMinStress = yingliDC;

            //periodOfTime清零重新开始计时
            periodOfTime = 0;
            lasttimeStart = lasttimeEnd;


        }
        //计算持续时间
        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(),now.toString(),"yyyy-MM-dd HH:mm:ss SSS");
        if(periodOfTime > 48){
            //拼接传入参数
            JSONObject jsonObjectCreep = new JSONObject();
            jsonObjectCreep.put("material",material);
            jsonObjectCreep.put("maxCreepStress",lastCreepMaxStress);
            //计算用温度选取:若此时该部件计算温度判断用的标识符I=1，II=1，则以最高问题tmax作为查该部件筒体材料持久强度的温度；
            // 若I=1，II=0，则选用500°C下SA-335p91材料的持久强度
            if(I == 1 && II == 1){
                jsonObjectCreep.put("temperature",lastCreepMaxTemperature);
            }else if(I == 1 && II ==0){
                if("SA-335P91" == material){
                    jsonObjectCreep.put("temperature","500");
                }else if("12Cr1MoV" == material){
                    jsonObjectCreep.put("temperature","450");
                }else if("SA-335P92" == material){
                    jsonObjectCreep.put("temperature","520");
                }else if("SA-336F12" == material){
                    jsonObjectCreep.put("temperature","450");
                }
            }
            jsonObjectCreep.put("periodOfTime",periodOfTime);
            jsonObjectCreep.put("totalCreepStress",lasttotalCreepStress);
            String inputStringCreep = jsonObjectCreep.toString();
            //蠕变寿命计算
            String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

            //time_end$ = time$
            lasttimeEnd = now;

            //蠕变寿命存储
            timeStart = lasttimeStart;
            timeEnd = lasttimeEnd;
            maxCreepTemperature = lastCreepMaxTemperature;
            minCreepTemperature = lastCreepMinTemperature;
            maxCreepStress = lastCreepMaxStress;
            minCreepStress = lastCreepMinStress;

            JSONObject outputJsonCreep =  JSONObject.fromObject(ouputStringCreep);
            lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
            creepStressi = lastcreepStressi;
            lasttotalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
            totalCreepStress = lasttotalCreepStress;

            System.out.println("###############蠕变计算的值################")
            System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
            System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
            System.out.println("本次蠕变寿命损耗：" + creepStressi)
            System.out.println("总的蠕变寿命损耗：" + totalCreepStress)


            //将实时计算的筒体壁温dqinnerT分别赋给lastCreepMaxTemperature，lastCreepMinTemperature，startTemperature
            //实时计算的蠕变应力赋给lastCreepMaxStress，lastCreepMinStress
            lastCreepMaxTemperature = dqinnerT.value;
            lastCreepMinTemperature = dqinnerT.value;
            startTemperature = dqinnerT.value;
            lastCreepMaxStress = yingliDC;
            lastCreepMinStress = yingliDC;

            //periodOfTime清零重新开始计时
            periodOfTime = 0;
            lasttimeStart = lasttimeEnd;
        }
    }
    System.out.println("###############比较后的值################")
    System.out.println("I：" + I)
    System.out.println("II：" + II)
    System.out.println("最高温度：" + lastCreepMaxTemperature )
    System.out.println("最低温度：" + lastCreepMinTemperature )
    System.out.println("最大应力：" + lastCreepMaxStress )
    System.out.println("最小应力：" + lastCreepMinStress )
    System.out.println("高温段内温度变化计算用初值：" + startTemperature )
}