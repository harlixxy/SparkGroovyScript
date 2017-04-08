import dao.IElasticModulus
import dao.IMaterialExpansionCoefficient
import dao.IMaterialParam
import dao.impl.ElasticModulus
import dao.impl.MaterialExpansionCoefficient
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
    lastmaxStress is "本次计算前最大应力"
    lastminStress is "本次计算前最小应力"
    lastmaxTemperature is "本次计算前最高温度"
    lastminTemperature is "本次计算前最低温度"
    s is "应力升、降方向标识符"
    lastTimeMax is "本次计算前最大应力时间"
    lastTimeMin is "本次计算前最小应力时间"
    lastnumOfFatigueLifeLoss is "疲劳寿命损耗计算次数"
    lasttotalFatigueLifeLoss is "本次计算前总的疲劳寿命损耗"

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
    I is    "蠕变寿命损耗计算温度范围标识符I"
    II is    "蠕变寿命损耗计算温度范围标识符II"

}


onInit {
    lastmaxStress = 0
    lastminStress = 0
    lastmaxTemperature = 0
    lastminTemperature = 0
    s = 0
    lastTimeMax = 0D
    lastTimeMin = 0D
    lastnumOfFatigueLifeLoss = 0D
    lasttotalFatigueLifeLoss = 0D

    //蠕变寿命计算
    lasttimeStart = 0
    lasttimeEnd = 0
    lastCreepMaxTemperature = 0
    lastCreepMinTemperature = 0
    lastCreepMaxStress = 0
    lastCreepMinStress = 0
    lastcreepStressi = 0
    lasttotalCreepStress = 0
    periodOfTime = 0
    startTemperature = 0
    I = 0
    II = 0
}

time_align { nullable }

output {

    maxStress is "最大应力"
    minStress is "最小应力"
    maxTemperature is "最高温度"
    minTemperature is "最低温度"
    timeMax is "最大应力时间"
    timeMin is "最小应力时间"
    numOfFatigueLifeLoss is "疲劳寿命损耗计算次数"
    fatigueLifeLoss is "本次疲劳寿命损耗"
    totalFatigueLifeLoss is "总的疲劳寿命损耗"

    fatigueStress is "高过集箱疲劳计算用应力"
    creepStress is "高过集箱蠕变计算用应力"

    //蠕变寿命计算
    timeStart is "起始时间"
    timeEnd is "结束时间"
    maxCreepTemperature is "最高筒体温度"
    minCreepTemperature is "最低筒体温度"
    maxCreepStress is "最高蠕变应力"
    minCreepStress is "最低蠕变应力"
    creepStressi is "该时间段内的蠕变寿命损耗"
    periodOfTimei is "该次蠕变寿命持续时间"
    totalCreepStress is "总的蠕变寿命损耗"

    cx is "C点x坐标"
    cy is "C点y坐标"
    ax is "A点x坐标"
    ay is "A点y坐标"
    accepted is "高温部件允许的总的寿命损耗"
    totalLoss is "总寿命损耗"
}

task {
    System.out.println("###############原始数据################")
    System.out.println("时间：" + dqinnerT.timestamp);
    System.out.println("压力：" + dqpress);
    System.out.println("内壁温度：" + dqinnerT.value);
    System.out.println("外壁温度：" + dqoutT.value);

    def IMwencha = dqoutT.value - dqinnerT.value;
    if (IMwencha >= 80) {
        IMwencha = 0
    }
/*
    IMaterialParam materialParam = new MaterialParam();
    List<Map<String,String>> list = materialParam.getmaterialParam(bjName);
    double kp = Double.valueOf(list.get(0).get("KP").toString());
    double kcl = Double.valueOf(list.get(1).get("KCL").toString());
    double kt = Double.valueOf(list.get(2).get("KT").toString());
*/

    IElasticModulus em = new ElasticModulus();
    double elasticModulus = Double.valueOf(em.getElasticModulus(material, String.valueOf(dqinnerT.value)));

    IMaterialExpansionCoefficient mec = new MaterialExpansionCoefficient();
    double materialExpansionCoefficient = Double.valueOf(mec.getMaterialExpansionCoefficient(material, String.valueOf(dqinnerT.value)));

    double yingliD = 10.0 * dqpress + 1.6 * elasticModulus * materialExpansionCoefficient * IMwencha;

    double yingliDC = 3.0 * dqpress;

    //目前为定值
    double x1 = 0.1, y1 = 0.01;
    cx = x1;
    cy = y1;

    StressAnalysis stressAnalysis = new StressAnalysis();

    def now = dqinnerT.timestamp;

    if (0 == lastmaxStress) {
        lastmaxStress = yingliD;
        lastminStress = yingliD;
        lastmaxTemperature = dqinnerT.value;
        lastminTemperature = dqinnerT.value;
        s = 0;
        lastnumOfFatigueLifeLoss = 0;
        lastTimeMax = now;
        lastTimeMin = now;
        I = 0;
        II = 0;
        periodOfTime = 0;
    }

    if (Double.valueOf(lastmaxTemperature) < dqinnerT.value) {
        lastmaxTemperature = dqinnerT.value;
    }
    if (Double.valueOf(lastminTemperature) > dqinnerT.value) {
        lastminTemperature = dqinnerT.value;
    }

    if (s == 1) {
        if (Double.valueOf(lastminStress) > yingliD) {
            lastminStress = yingliD;
            lastTimeMin = now;
        }
        if ((yingliD - Double.valueOf(lastminStress)) > 250) {
            s = -1;
            //计算
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("material", material);
            jsonObject.put("maxTemperature", lastmaxTemperature);
            jsonObject.put("minTemperature", lastminTemperature);
            jsonObject.put("maxStress", lastmaxStress);
            jsonObject.put("minStress", lastminStress);
            jsonObject.put("numOfFatigueLifeLoss", lastnumOfFatigueLifeLoss);
            jsonObject.put("totalFatigueLifeLoss", lasttotalFatigueLifeLoss);

            String inputString = jsonObject.toString();
            String ouputString = stressAnalysis.fatigueCalculate(inputString);

            JSONObject outputJson = JSONObject.fromObject(ouputString);
            lastmaxStress = Double.valueOf(outputJson.getString("maxStress"));
            lastminStress = Double.valueOf(outputJson.getString("minStress"));
            lastmaxTemperature = Double.valueOf(outputJson.getString("maxTemperature"));
            lastminTemperature = Double.valueOf(outputJson.getString("minTemperature"));
            lastnumOfFatigueLifeLoss = Double.valueOf(outputJson.getString("numOfFatigueLifeLoss"));
            lasttotalFatigueLifeLoss = Double.valueOf(outputJson.getString("totalFatigueLifeLoss"));
            //给output变量赋值
            maxStress = Double.valueOf(outputJson.getString("maxStress"));
            minStress = Double.valueOf(outputJson.getString("minStress"));
            maxTemperature = Double.valueOf(outputJson.getString("maxTemperature"));
            minTemperature = Double.valueOf(outputJson.getString("minTemperature"));
            timeMax = lastTimeMax;
            timeMin = lastTimeMin;
            numOfFatigueLifeLoss = Double.valueOf(outputJson.getString("numOfFatigueLifeLoss"));
            fatigueLifeLoss = Double.valueOf(outputJson.getString("fatigueLifeLoss"));
            totalFatigueLifeLoss = Double.valueOf(outputJson.getString("totalFatigueLifeLoss"));
            fatigueStress = yingliD;

            System.out.println("高过集箱疲劳计算用应力：" + fatigueStress);

            System.out.println("###############疲劳计算的值################")
            System.out.println("疲劳计算用最高温度：" + lastmaxTemperature)
            System.out.println("疲劳计算用最低温度：" + lastminTemperature)
            System.out.println("疲劳计算用最大应力：" + lastmaxStress)
            System.out.println("疲劳计算用最小应力：" + lastminStress)
            System.out.println("疲劳寿命损耗计算次数：" + numOfFatigueLifeLoss)
            System.out.println("本次疲劳寿命损耗：" + fatigueLifeLoss)
            System.out.println("总的疲劳寿命损耗：" + totalFatigueLifeLoss)
            System.out.println("##########################################")

            lastmaxStress = yingliD;
            lastmaxTemperature = dqinnerT.value;
            lastTimeMax = now;

            System.out.println("###################疲劳计算——高温部件寿命损耗评估###################")
            /*double x1 = 0.1, y1 = 0.01;
            cx = x1;
            cy = y1;*/

            JSONObject jsonObjectEvaluate = new JSONObject();
            jsonObjectEvaluate.put("x1", x1);
            jsonObjectEvaluate.put("y1", y1);
            jsonObjectEvaluate.put("Df", lasttotalFatigueLifeLoss);
            jsonObjectEvaluate.put("Dc", lasttotalCreepStress);
            String inputStringEvaluate = jsonObjectEvaluate.toString();

            String outputStrEvaluate = stressAnalysis.lifeLossEvaluate(inputStringEvaluate);

            JSONObject outputJsonEvaluate = JSONObject.fromObject(outputStrEvaluate);
            ax = Double.valueOf(outputJsonEvaluate.getString("df1"));
            ay = Double.valueOf(outputJsonEvaluate.getString("dc1"));
            double acceptedV = Double.valueOf(outputJsonEvaluate.getString("acceptedV"));
            accepted = acceptedV;
            double loss = Double.valueOf(outputJsonEvaluate.getString("loss"));
            totalLoss = loss;
            double percentage = Double.valueOf(outputJsonEvaluate.getString("percentage"));

            System.out.println("使用寿命：" + (Double.valueOf(percentage) >= 100 ? "结束" : "未结束"))

        }
    }
    if (s == -1) {
        if (Double.valueOf(lastmaxStress) < yingliD) {
            lastmaxStress = yingliD;
            lastTimeMax = now;
        }
        if (Double.valueOf(lastmaxStress) - yingliD > 250) {
            s = 1;
            //计算
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("material", material);
            jsonObject.put("maxTemperature", lastmaxTemperature);
            jsonObject.put("minTemperature", lastminTemperature);
            jsonObject.put("maxStress", lastmaxStress);
            jsonObject.put("minStress", lastminStress);
            jsonObject.put("numOfFatigueLifeLoss", lastnumOfFatigueLifeLoss);
            jsonObject.put("totalFatigueLifeLoss", lasttotalFatigueLifeLoss);

            String inputString = jsonObject.toString();
            String ouputString = stressAnalysis.fatigueCalculate(inputString);

            JSONObject outputJson = JSONObject.fromObject(ouputString);
            lastmaxStress = outputJson.getString("maxStress");
            lastminStress = Double.valueOf(outputJson.getString("minStress"));
            lastmaxTemperature = outputJson.getString("maxTemperature");
            lastminTemperature = outputJson.getString("minTemperature");
            lastnumOfFatigueLifeLoss = outputJson.getString("numOfFatigueLifeLoss");
            lasttotalFatigueLifeLoss = outputJson.getString("totalFatigueLifeLoss");
            //为output变量赋值
            maxStress = Double.valueOf(outputJson.getString("maxStress"));
            minStress = Double.valueOf(outputJson.getString("minStress"));
            maxTemperature = Double.valueOf(outputJson.getString("maxTemperature"));
            minTemperature = Double.valueOf(outputJson.getString("minTemperature")); ;
            timeMax = lastTimeMax;
            timeMin = lastTimeMin;
            numOfFatigueLifeLoss = Double.valueOf(outputJson.getString("numOfFatigueLifeLoss"));
            fatigueLifeLoss = Double.valueOf(outputJson.getString("fatigueLifeLoss"));
            totalFatigueLifeLoss = Double.valueOf(outputJson.getString("totalFatigueLifeLoss"));
            fatigueStress = yingliD;
            System.out.println("高过集箱疲劳计算用应力：" + fatigueStress);

            System.out.println("###############疲劳计算的值################")
            System.out.println("疲劳计算用最高温度：" + lastmaxTemperature)
            System.out.println("疲劳计算用最低温度：" + lastminTemperature)
            System.out.println("疲劳计算用最大应力：" + lastmaxStress)
            System.out.println("疲劳计算用最小应力：" + lastminStress)
            System.out.println("疲劳寿命损耗计算次数：" + numOfFatigueLifeLoss)
            System.out.println("本次疲劳寿命损耗：" + fatigueLifeLoss)
            System.out.println("总的疲劳寿命损耗：" + totalFatigueLifeLoss)
            System.out.println("##########################################")

            lastminStress = yingliD;
            lastminTemperature = dqinnerT.value;
            lastTimeMin = now;

            System.out.println("###################疲劳计算——高温部件寿命损耗评估###################")
            /*double x1 = 0.1, y1 = 0.01;
            cx = x1;
            cy = y1;*/
            JSONObject jsonObjectEvaluate = new JSONObject();
            jsonObjectEvaluate.put("x1", x1);
            jsonObjectEvaluate.put("y1", y1);
            jsonObjectEvaluate.put("Df", lasttotalFatigueLifeLoss);
            jsonObjectEvaluate.put("Dc", lasttotalCreepStress);
            String inputStringEvaluate = jsonObjectEvaluate.toString();

            String outputStrEvaluate = stressAnalysis.lifeLossEvaluate(inputStringEvaluate);

            JSONObject outputJsonEvaluate = JSONObject.fromObject(outputStrEvaluate);
            ax = Double.valueOf(outputJsonEvaluate.getString("df1"));
            ay = Double.valueOf(outputJsonEvaluate.getString("dc1"));
            double acceptedV = Double.valueOf(outputJsonEvaluate.getString("acceptedV"));
            accepted = acceptedV;
            double loss = Double.valueOf(outputJsonEvaluate.getString("loss"));
            totalLoss = loss;
            double percentage = Double.valueOf(outputJsonEvaluate.getString("percentage"));
            System.out.println("使用寿命：" + (Double.valueOf(percentage) >= 100 ? "结束" : "未结束"))

        }
    }
    if (s == 0) {
        if (Double.valueOf(lastmaxStress) < yingliD) {
            lastmaxStress = yingliD;
            lastTimeMax = now;
        }
        if (Double.valueOf(lastminStress) > yingliD) {
            lastminStress = yingliD;
            lastTimeMin = now;
        }
        if (Double.valueOf(lastmaxStress) - yingliD > 250) {
            s = 1;
        }
        if (yingliD - Double.valueOf(lastminStress) > 250) {
            s = -1;
        }
    }

    System.out.println("############计算比较后疲劳计算数据############");
    System.out.println("疲劳应力计算值:" + yingliD);
    System.out.println("疲劳最高温度:" + lastmaxTemperature);
    System.out.println("疲劳最低温度:" + lastminTemperature);
    System.out.println("疲劳最大应力时间:" + lastTimeMax);
    System.out.println("疲劳最大应力:" + lastmaxStress);
    System.out.println("疲劳最小应力时间:" + lastTimeMin);
    System.out.println("疲劳最小应力:" + lastminStress);
    System.out.println("标识符s:" + s);
    System.out.println("总的疲劳寿命损耗：" + lasttotalFatigueLifeLoss)
    System.out.println("###########################################");


    if (dqinnerT.value >= 500 && I == 0 && II == 0) {
        I = 1;
        II = 1;
        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        startTemperature = dqinnerT.value;
        lastCreepMaxStress = yingliDC;
        lastCreepMinStress = yingliDC;
        //开始计时并累加
        lasttimeStart = now;
    }
    if (dqinnerT.value >= 450 && I == 0 && II == 0) {
        I = 1;
        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        lastCreepMaxStress = yingliDC;
        lastCreepMinStress = yingliDC;
        //开始计时并累加
        lasttimeStart = now;
    }

    //三:若450<t<500，且Ⅰ=1，Ⅱ=0
    if (450 < dqinnerT.value && dqinnerT.value < 500 && I == 1 && II == 0) {
        if (dqinnerT.value > lastCreepMaxTemperature) {
            lastCreepMaxTemperature = dqinnerT.value;
        }
        if (dqinnerT.value < lastCreepMinTemperature) {
            lastCreepMinTemperature = dqinnerT.value;
        }
        if (yingliDC > lastCreepMaxStress) {
            lastCreepMaxStress = yingliDC;
        }
        if (yingliDC < lastCreepMinStress) {
            lastCreepMinStress = yingliDC;
        }

        //计算periodOfTime
        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(), now.toString(), "yyyy-MM-dd HH:mm:ss SSS");
        if (periodOfTime > 100) {
            //计算
            JSONObject jsonObjectCreep = new JSONObject();
            jsonObjectCreep.put("material", material);
            jsonObjectCreep.put("maxCreepStress", lastCreepMaxStress);

            if (I == 1 && II == 1) {
                jsonObjectCreep.put("temperature", lastCreepMaxTemperature);
            } else if (I == 1 && II == 0) {
                if ("SA-335P91" == material) {
                    jsonObjectCreep.put("temperature", "500");
                } else if ("12Cr1MoV" == material) {
                    jsonObjectCreep.put("temperature", "450");
                } else if ("SA-335P92" == material) {
                    jsonObjectCreep.put("temperature", "520");
                } else if ("SA-336F12" == material) {
                    jsonObjectCreep.put("temperature", "450");
                }
            }
            jsonObjectCreep.put("periodOfTime", periodOfTime);
            jsonObjectCreep.put("totalCreepStress", lasttotalCreepStress);
            String inputStringCreep = jsonObjectCreep.toString();

            String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

            lasttimeEnd = now;
            //为output变量赋值
            periodOfTimei = periodOfTime
            timeStart = lasttimeStart;
            timeEnd = lasttimeEnd;
            maxCreepTemperature = lastCreepMaxTemperature;
            minCreepTemperature = lastCreepMinTemperature;
            maxCreepStress = lastCreepMaxStress;
            minCreepStress = lastCreepMinStress;

            JSONObject outputJsonCreep = JSONObject.fromObject(ouputStringCreep);
            lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
            creepStressi = lastcreepStressi;
            lasttotalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
            totalCreepStress = lasttotalCreepStress;
            creepStress = yingliDC;
            System.out.println("高过集箱蠕变计算用应力：" + creepStress);

            System.out.println("###############蠕变计算的值################")
            System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
            System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
            System.out.println("本次蠕变寿命损耗：" + creepStressi)
            System.out.println("总的蠕变寿命损耗：" + totalCreepStress)
            System.out.println("############################################")


            lastCreepMaxTemperature = dqinnerT.value;
            lastCreepMinTemperature = dqinnerT.value;
            startTemperature = dqinnerT.value;
            lastCreepMaxStress = yingliDC;
            lastCreepMinStress = yingliDC;

            periodOfTime = 0;
            lasttimeStart = lasttimeEnd;

            System.out.println("###################高温部件寿命损耗评估###################")

            /*double x1 = 0.1, y1 = 0.01;
            cx = x1;
            cy = y1;*/

            JSONObject jsonObjectEvaluate = new JSONObject();
            jsonObjectEvaluate.put("x1", x1);
            jsonObjectEvaluate.put("y1", y1);
            jsonObjectEvaluate.put("Df", lasttotalFatigueLifeLoss);
            jsonObjectEvaluate.put("Dc", lasttotalCreepStress);
            String inputStringEvaluate = jsonObjectEvaluate.toString();

            String outputStrEvaluate = stressAnalysis.lifeLossEvaluate(inputStringEvaluate);

            JSONObject outputJsonEvaluate = JSONObject.fromObject(outputStrEvaluate);
            ax = Double.valueOf(outputJsonEvaluate.getString("df1"));
            ay = Double.valueOf(outputJsonEvaluate.getString("dc1"));
            double acceptedV = Double.valueOf(outputJsonEvaluate.getString("acceptedV"));
            accepted = acceptedV;
            double loss = Double.valueOf(outputJsonEvaluate.getString("loss"));
            totalLoss = loss;
            double percentage = Double.valueOf(outputJsonEvaluate.getString("percentage"));
            System.out.println("使用寿命：" + (Double.valueOf(percentage) >= 100 ? "结束" : "未结束"))

        }
    }
    if (dqinnerT.value < 450 && I == 1 && II == 0) {

        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(), now.toString(), "yyyy-MM-dd HH:mm:ss SSS");
        //计算
        JSONObject jsonObjectCreep = new JSONObject();
        jsonObjectCreep.put("material", material);
        jsonObjectCreep.put("maxCreepStress", lastCreepMaxStress);
        if (I == 1 && II == 1) {
            jsonObjectCreep.put("temperature", lastCreepMaxTemperature);
        } else if (I == 1 && II == 0) {
            if ("SA-335P91" == material) {
                jsonObjectCreep.put("temperature", "500");
            } else if ("12Cr1MoV" == material) {
                jsonObjectCreep.put("temperature", "450");
            } else if ("SA-335P92" == material) {
                jsonObjectCreep.put("temperature", "520");
            } else if ("SA-336F12" == material) {
                jsonObjectCreep.put("temperature", "450");
            }
        }
        jsonObjectCreep.put("periodOfTime", periodOfTime);
        jsonObjectCreep.put("totalCreepStress", lasttotalCreepStress);
        String inputStringCreep = jsonObjectCreep.toString();
        String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

        lasttimeEnd = now;
        //为output变量赋值
        periodOfTimei = periodOfTime
        timeStart = lasttimeStart;
        timeEnd = lasttimeEnd;
        maxCreepTemperature = lastCreepMaxTemperature;
        minCreepTemperature = lastCreepMinTemperature;
        maxCreepStress = lastCreepMaxStress;
        minCreepStress = lastCreepMinStress;

        JSONObject outputJsonCreep = JSONObject.fromObject(ouputStringCreep);
        lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
        creepStressi = lastcreepStressi;
        lasttotalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
        totalCreepStress = lasttotalCreepStress;
        creepStress = yingliDC;
        System.out.println("高过集箱蠕变计算用应力：" + creepStress);

        System.out.println("###############蠕变计算的值################")
        System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
        System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
        System.out.println("本次蠕变寿命损耗：" + creepStressi)
        System.out.println("总的蠕变寿命损耗：" + totalCreepStress)
        System.out.println("############################################")

        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        startTemperature = dqinnerT.value;
        lastCreepMaxStress = yingliDC;
        lastCreepMinStress = yingliDC;

        periodOfTime = 0;
        I = 0
        System.out.println("###################高温部件寿命损耗评估###################")
        /*double x1 = 0.1, y1 = 0.01;
        cx = x1;
        cy = y1;*/

        JSONObject jsonObjectEvaluate = new JSONObject();
        jsonObjectEvaluate.put("x1", x1);
        jsonObjectEvaluate.put("y1", y1);
        jsonObjectEvaluate.put("Df", lasttotalFatigueLifeLoss);
        jsonObjectEvaluate.put("Dc", lasttotalCreepStress);
        String inputStringEvaluate = jsonObjectEvaluate.toString();

        String outputStrEvaluate = stressAnalysis.lifeLossEvaluate(inputStringEvaluate);

        JSONObject outputJsonEvaluate = JSONObject.fromObject(outputStrEvaluate);
        ax = Double.valueOf(outputJsonEvaluate.getString("df1"));
        ay = Double.valueOf(outputJsonEvaluate.getString("dc1"));
        double acceptedV = Double.valueOf(outputJsonEvaluate.getString("acceptedV"));
        accepted = acceptedV;
        double loss = Double.valueOf(outputJsonEvaluate.getString("loss"));
        totalLoss = loss;
        double percentage = Double.valueOf(outputJsonEvaluate.getString("percentage"));
        System.out.println("使用寿命：" + (Double.valueOf(percentage) >= 100 ? "结束" : "未结束"))

    }
    if (dqinnerT.value >= 500 && I == 1 && II == 0) {
        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(), now.toString(), "yyyy-MM-dd HH:mm:ss SSS");
        //计算
        JSONObject jsonObjectCreep = new JSONObject();
        jsonObjectCreep.put("material", material);
        jsonObjectCreep.put("maxCreepStress", lastCreepMaxStress);
        if (I == 1 && II == 1) {
            jsonObjectCreep.put("temperature", lastCreepMaxTemperature);
        } else if (I == 1 && II == 0) {
            if ("SA-335P91" == material) {
                jsonObjectCreep.put("temperature", "500");
            } else if ("12Cr1MoV" == material) {
                jsonObjectCreep.put("temperature", "450");
            } else if ("SA-335P92" == material) {
                jsonObjectCreep.put("temperature", "520");
            } else if ("SA-336F12" == material) {
                jsonObjectCreep.put("temperature", "450");
            }
        }
        jsonObjectCreep.put("periodOfTime", periodOfTime);
        jsonObjectCreep.put("totalCreepStress", lasttotalCreepStress);
        String inputStringCreep = jsonObjectCreep.toString();
        String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

        lasttimeEnd = now;
        //为output变量赋值
        periodOfTimei = periodOfTime
        timeStart = lasttimeStart;
        timeEnd = lasttimeEnd;
        maxCreepTemperature = lastCreepMaxTemperature;
        minCreepTemperature = lastCreepMinTemperature;
        maxCreepStress = lastCreepMaxStress;
        minCreepStress = lastCreepMinStress;

        JSONObject outputJsonCreep = JSONObject.fromObject(ouputStringCreep);
        lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
        totalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
        creepStressi = lastcreepStressi;
        totalCreepStress = lasttotalCreepStress;
        creepStress = yingliDC;
        System.out.println("高过集箱蠕变计算用应力：" + creepStress);

        System.out.println("###############蠕变计算的值################")
        System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
        System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
        System.out.println("本次蠕变寿命损耗：" + creepStressi)
        System.out.println("总的蠕变寿命损耗：" + totalCreepStress)
        System.out.println("############################################")

        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        startTemperature = dqinnerT.value;
        lastCreepMaxStress = yingliDC;
        lastCreepMinStress = yingliDC;

        periodOfTime = 0;
        lasttimeStart = lasttimeEnd;

        II = 1;
        startTemperature = dqinnerT.value;
        System.out.println("###################高温部件寿命损耗评估###################")
        /*double x1 = 0.1, y1 = 0.01;
        cx = x1;
        cy = y1;*/
        JSONObject jsonObjectEvaluate = new JSONObject();
        jsonObjectEvaluate.put("x1", x1);
        jsonObjectEvaluate.put("y1", y1);
        jsonObjectEvaluate.put("Df", lasttotalFatigueLifeLoss);
        jsonObjectEvaluate.put("Dc", lasttotalCreepStress);
        String inputStringEvaluate = jsonObjectEvaluate.toString();

        String outputStrEvaluate = stressAnalysis.lifeLossEvaluate(inputStringEvaluate);

        JSONObject outputJsonEvaluate = JSONObject.fromObject(outputStrEvaluate);
        ax = Double.valueOf(outputJsonEvaluate.getString("df1"));
        ay = Double.valueOf(outputJsonEvaluate.getString("dc1"));
        double acceptedV = Double.valueOf(outputJsonEvaluate.getString("acceptedV"));
        accepted = acceptedV;
        double loss = Double.valueOf(outputJsonEvaluate.getString("loss"));
        totalLoss = loss;
        double percentage = Double.valueOf(outputJsonEvaluate.getString("percentage"));
        System.out.println("使用寿命：" + (Double.valueOf(percentage) >= 100 ? "结束" : "未结束"))

    }
    if (dqinnerT.value < 500 && I == 1 && II == 1) {
        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(), now.toString(), "yyyy-MM-dd HH:mm:ss SSS");
        //计算
        JSONObject jsonObjectCreep = new JSONObject();
        jsonObjectCreep.put("material", material);
        jsonObjectCreep.put("maxCreepStress", lastCreepMaxStress);
        if (I == 1 && II == 1) {
            jsonObjectCreep.put("temperature", lastCreepMaxTemperature);
        } else if (I == 1 && II == 0) {
            if ("SA-335P91" == material) {
                jsonObjectCreep.put("temperature", "500");
            } else if ("12Cr1MoV" == material) {
                jsonObjectCreep.put("temperature", "450");
            } else if ("SA-335P92" == material) {
                jsonObjectCreep.put("temperature", "520");
            } else if ("SA-336F12" == material) {
                jsonObjectCreep.put("temperature", "450");
            }
        }
        jsonObjectCreep.put("periodOfTime", periodOfTime);
        jsonObjectCreep.put("totalCreepStress", lasttotalCreepStress);
        String inputStringCreep = jsonObjectCreep.toString();
        String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

        lasttimeEnd = now;
        //为output变量赋值
        periodOfTimei = periodOfTime
        timeStart = lasttimeStart;
        timeEnd = lasttimeEnd;
        maxCreepTemperature = lastCreepMaxTemperature;
        minCreepTemperature = lastCreepMinTemperature;
        maxCreepStress = lastCreepMaxStress;
        minCreepStress = lastCreepMinStress;

        JSONObject outputJsonCreep = JSONObject.fromObject(ouputStringCreep);
        lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
        totalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
        creepStressi = lastcreepStressi;
        totalCreepStress = lasttotalCreepStress;
        creepStress = yingliDC;
        System.out.println("高过集箱蠕变计算用应力：" + creepStress);

        System.out.println("###############蠕变计算的值################")
        System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
        System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
        System.out.println("本次蠕变寿命损耗：" + creepStressi)
        System.out.println("总的蠕变寿命损耗：" + totalCreepStress)
        System.out.println("############################################")

        lastCreepMaxTemperature = dqinnerT.value;
        lastCreepMinTemperature = dqinnerT.value;
        startTemperature = dqinnerT.value;
        lastCreepMaxStress = yingliDC;
        lastCreepMinStress = yingliDC;

        II = 0;
        periodOfTime = 0;
        lasttimeStart = lasttimeEnd;
        System.out.println("###################高温部件寿命损耗评估###################")

        /*double x1 = 0.1, y1 = 0.01;
        cx = x1;
        cy = y1;*/
        JSONObject jsonObjectEvaluate = new JSONObject();
        jsonObjectEvaluate.put("x1", x1);
        jsonObjectEvaluate.put("y1", y1);
        jsonObjectEvaluate.put("Df", lasttotalFatigueLifeLoss);
        jsonObjectEvaluate.put("Dc", lasttotalCreepStress);
        String inputStringEvaluate = jsonObjectEvaluate.toString();
        //   System.out.println("#############################        " +inputStringEvaluate)

        String outputStrEvaluate = stressAnalysis.lifeLossEvaluate(inputStringEvaluate);

        JSONObject outputJsonEvaluate = JSONObject.fromObject(outputStrEvaluate);
        ax = Double.valueOf(outputJsonEvaluate.getString("df1"));
        ay = Double.valueOf(outputJsonEvaluate.getString("dc1"));
        double acceptedV = Double.valueOf(outputJsonEvaluate.getString("acceptedV"));
        accepted = acceptedV;
        double loss = Double.valueOf(outputJsonEvaluate.getString("loss"));
        totalLoss = loss;
        double percentage = Double.valueOf(outputJsonEvaluate.getString("percentage"));
        System.out.println("使用寿命：" + (Double.valueOf(percentage) >= 100 ? "结束" : "未结束"))

    }
    if (dqinnerT.value > 500 && I == 1 && II == 1) {
        if (dqinnerT.value > lastCreepMaxTemperature) {
            lastCreepMaxTemperature = dqinnerT.value;
        }
        if (dqinnerT.value < lastCreepMinTemperature) {
            lastCreepMinTemperature = dqinnerT.value;
        }
        if (yingliDC > lastCreepMaxStress) {
            lastCreepMaxStress = yingliDC;
        }
        if (yingliDC < lastCreepMinStress) {
            lastCreepMinStress = yingliDC;
        }
        if (Math.abs(dqinnerT.value - startTemperature) >= 5) {
            periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(), now.toString(), "yyyy-MM-dd HH:mm:ss SSS");
            //计算
            JSONObject jsonObjectCreep = new JSONObject();
            jsonObjectCreep.put("material", material);
            jsonObjectCreep.put("maxCreepStress", lastCreepMaxStress);
            if (I == 1 && II == 1) {
                jsonObjectCreep.put("temperature", lastCreepMaxTemperature);
            } else if (I == 1 && II == 0) {
                if ("SA-335P91" == material) {
                    jsonObjectCreep.put("temperature", "500");
                } else if ("12Cr1MoV" == material) {
                    jsonObjectCreep.put("temperature", "450");
                } else if ("SA-335P92" == material) {
                    jsonObjectCreep.put("temperature", "520");
                } else if ("SA-336F12" == material) {
                    jsonObjectCreep.put("temperature", "450");
                }
            }
            jsonObjectCreep.put("periodOfTime", periodOfTime);
            jsonObjectCreep.put("totalCreepStress", lasttotalCreepStress);
            String inputStringCreep = jsonObjectCreep.toString();
            String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

            lasttimeEnd = now;
            //为output变量赋值
            periodOfTimei = periodOfTime
            timeStart = lasttimeStart;
            timeEnd = lasttimeEnd;
            maxCreepTemperature = lastCreepMaxTemperature;
            minCreepTemperature = lastCreepMinTemperature;
            maxCreepStress = lastCreepMaxStress;
            minCreepStress = lastCreepMinStress;

            JSONObject outputJsonCreep = JSONObject.fromObject(ouputStringCreep);
            lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
            creepStressi = lastcreepStressi;
            lasttotalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
            creepStress = yingliDC;
            System.out.println("高过集箱蠕变计算用应力：" + creepStress);

            totalCreepStress = lasttotalCreepStress;
            System.out.println("###############蠕变计算的值################")
            System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
            System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
            System.out.println("本次蠕变寿命损耗：" + creepStressi)
            System.out.println("总的蠕变寿命损耗：" + totalCreepStress)
            System.out.println("############################################")

            lastCreepMaxTemperature = dqinnerT.value;
            lastCreepMinTemperature = dqinnerT.value;
            startTemperature = dqinnerT.value;
            lastCreepMaxStress = yingliDC;
            lastCreepMinStress = yingliDC;

            periodOfTime = 0;
            lasttimeStart = lasttimeEnd;
            System.out.println("###################高温部件寿命损耗评估###################")
            /*double x1 = 0.1, y1 = 0.01;
            cx = x1;
            cy = y1;*/
            JSONObject jsonObjectEvaluate = new JSONObject();
            jsonObjectEvaluate.put("x1", x1);
            jsonObjectEvaluate.put("y1", y1);
            jsonObjectEvaluate.put("Df", lasttotalFatigueLifeLoss);
            jsonObjectEvaluate.put("Dc", lasttotalCreepStress);
            String inputStringEvaluate = jsonObjectEvaluate.toString();

            String outputStrEvaluate = stressAnalysis.lifeLossEvaluate(inputStringEvaluate);

            JSONObject outputJsonEvaluate = JSONObject.fromObject(outputStrEvaluate);
            ax = Double.valueOf(outputJsonEvaluate.getString("df1"));
            ay = Double.valueOf(outputJsonEvaluate.getString("dc1"));
            double acceptedV = Double.valueOf(outputJsonEvaluate.getString("acceptedV"));
            accepted = acceptedV;
            double loss = Double.valueOf(outputJsonEvaluate.getString("loss"));
            totalLoss = loss;
            double percentage = Double.valueOf(outputJsonEvaluate.getString("percentage"));
            System.out.println("使用寿命：" + (Double.valueOf(percentage) >= 100 ? "结束" : "未结束"))
        }

        periodOfTime = stressAnalysis.hoursCalculate(lasttimeStart.toString(), now.toString(), "yyyy-MM-dd HH:mm:ss SSS");

        if (periodOfTime > 48) {
            //计算
            JSONObject jsonObjectCreep = new JSONObject();
            jsonObjectCreep.put("material", material);
            jsonObjectCreep.put("maxCreepStress", lastCreepMaxStress);

            if (I == 1 && II == 1) {
                jsonObjectCreep.put("temperature", lastCreepMaxTemperature);
            } else if (I == 1 && II == 0) {
                if ("SA-335P91" == material) {
                    jsonObjectCreep.put("temperature", "500");
                } else if ("12Cr1MoV" == material) {
                    jsonObjectCreep.put("temperature", "450");
                } else if ("SA-335P92" == material) {
                    jsonObjectCreep.put("temperature", "520");
                } else if ("SA-336F12" == material) {
                    jsonObjectCreep.put("temperature", "450");
                }
            }
            jsonObjectCreep.put("periodOfTime", periodOfTime);
            jsonObjectCreep.put("totalCreepStress", lasttotalCreepStress);
            String inputStringCreep = jsonObjectCreep.toString();
            String ouputStringCreep = stressAnalysis.creepCalculate(inputStringCreep);

            lasttimeEnd = now;
            //为output变量赋值
            periodOfTimei = periodOfTime
            timeStart = lasttimeStart;
            timeEnd = lasttimeEnd;
            maxCreepTemperature = lastCreepMaxTemperature;
            minCreepTemperature = lastCreepMinTemperature;
            maxCreepStress = lastCreepMaxStress;
            minCreepStress = lastCreepMinStress;

            JSONObject outputJsonCreep = JSONObject.fromObject(ouputStringCreep);
            lastcreepStressi = Double.valueOf(outputJsonCreep.getString("creepStressi"));
            creepStressi = lastcreepStressi;
            lasttotalCreepStress = Double.valueOf(outputJsonCreep.getString("totalCreepStress"));
            totalCreepStress = lasttotalCreepStress;
            creepStress = yingliDC;
            System.out.println("高过集箱蠕变计算用应力：" + creepStress);

            System.out.println("###############蠕变计算的值################")
            System.out.println("蠕变计算用温度：" + lastCreepMaxTemperature)
            System.out.println("蠕变计算用应力：" + lastCreepMaxStress)
            System.out.println("本次蠕变寿命损耗：" + creepStressi)
            System.out.println("总的蠕变寿命损耗：" + totalCreepStress)
            System.out.println("############################################")

            lastCreepMaxTemperature = dqinnerT.value;
            lastCreepMinTemperature = dqinnerT.value;
            startTemperature = dqinnerT.value;
            lastCreepMaxStress = yingliDC;
            lastCreepMinStress = yingliDC;

            periodOfTime = 0;
            lasttimeStart = lasttimeEnd;
            System.out.println("###################高温部件寿命损耗评估###################")
            /*double x1 = 0.1, y1 = 0.01;
            cx = x1;
            cy = y1;*/
            JSONObject jsonObjectEvaluate = new JSONObject();
            jsonObjectEvaluate.put("x1", x1);
            jsonObjectEvaluate.put("y1", y1);
            jsonObjectEvaluate.put("Df", lasttotalFatigueLifeLoss);
            jsonObjectEvaluate.put("Dc", lasttotalCreepStress);
            String inputStringEvaluate = jsonObjectEvaluate.toString();

            String outputStrEvaluate = stressAnalysis.lifeLossEvaluate(inputStringEvaluate);

            JSONObject outputJsonEvaluate = JSONObject.fromObject(outputStrEvaluate);
            ax = Double.valueOf(outputJsonEvaluate.getString("df1"));
            ay = Double.valueOf(outputJsonEvaluate.getString("dc1"));
            double acceptedV = Double.valueOf(outputJsonEvaluate.getString("acceptedV"));
            accepted = acceptedV;
            double loss = Double.valueOf(outputJsonEvaluate.getString("loss"));
            totalLoss = loss;
            double percentage = Double.valueOf(outputJsonEvaluate.getString("percentage"));
            System.out.println("使用寿命：" + (Double.valueOf(percentage) >= 100 ? "结束" : "未结束"))

        }
    }
    System.out.println("###############计算后的值################")
    System.out.println("I：" + I)
    System.out.println("II：" + II)
    System.out.println("蠕变最高温度：" + lastCreepMaxTemperature)
    System.out.println("蠕变最低温度：" + lastCreepMinTemperature)
    System.out.println("蠕变最大应力：" + lastCreepMaxStress)
    System.out.println("蠕变最小应力：" + lastCreepMinStress)
    System.out.println("高温段内温度变化计算用初值：" + startTemperature)
    System.out.println("############################################")

    System.out.println("###############output的值################")
    System.out.println("最大应力：" + maxStress)
    System.out.println("最小应力：" + minStress)
    System.out.println("最高温度：" + maxTemperature)
    System.out.println("最低温度：" + minTemperature)
    System.out.println("最大应力时间：" + timeMax)
    System.out.println("最小应力时间：" + timeMin)
    System.out.println("疲劳寿命损耗计算次数：" + numOfFatigueLifeLoss)

    System.out.println("总的疲劳寿命损耗：" + totalFatigueLifeLoss)
    System.out.println("高过集箱计算用疲劳应力 ：" + fatigueStress)
    System.out.println("高过集箱计算用疲劳应力 ：" + creepStress)

    System.out.println("起始时间：" + timeStart)
    System.out.println("结束时间：" + timeEnd)
    System.out.println("最高筒体温度：" + maxCreepTemperature)
    System.out.println("最低筒体温度：" + minCreepTemperature)
    System.out.println("最高蠕变应力：" + maxCreepStress)
    System.out.println("最低蠕变应力：" + minCreepStress)
    System.out.println("该时间段内的蠕变寿命损耗：" + creepStressi)
    System.out.println("该次蠕变寿命持续时间：" + periodOfTimei)
    System.out.println("总的蠕变寿命损耗：" + totalCreepStress)

    System.out.println("C点x坐标：" + cx)
    System.out.println("C点y坐标：" + cy)
    System.out.println("A点x坐标：" + ax)
    System.out.println("A点y坐标：" + ay)
    System.out.println("高温部件允许的总的寿命损耗：" + accepted)
    System.out.println("总寿命损耗：" + totalLoss)


    System.out.println("############################################")

}