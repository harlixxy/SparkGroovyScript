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

}


onInit {
    lastmaxStress = 0
    lastminStress = 0
    lastmaxTemperature = 0
    lastminTemperature = 0
    s = -1
    lastTimeMax = 0D
    lastTimeMin = 0D
    lastnumOfFatigueLifeLoss = 0D
    lasttotalFatigueLifeLoss = 0D

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

    fatigueStress is "省煤器集箱疲劳计算用应力"

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

    StressAnalysis stressAnalysis = new StressAnalysis();

    def now = dqinnerT.timestamp;

    if (0 == lastmaxStress) {
        lastmaxStress = yingliD;
        lastminStress = yingliD;
        lastmaxTemperature = dqinnerT.value;
        lastminTemperature = dqinnerT.value;
        s = -1;
        lastnumOfFatigueLifeLoss = 0;
        lastTimeMax = now;
        lastTimeMin = now;
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
            System.out.println("省煤器集箱疲劳计算用应力：" + fatigueStress);

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
        }
    }
    if (s == -1) {
        System.out.println("进来了aaaaaa")
        if (Double.valueOf(lastmaxStress) < yingliD) {
            lastmaxStress = yingliD;
            lastTimeMax = now;
        }
        if (Double.valueOf(lastmaxStress) - yingliD > 250) {
            System.out.println("进来了bbbbbb")
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
            System.out.println("省煤器集箱疲劳计算用应力：" + fatigueStress);

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

    maxStress = lastmaxStress
    minStress = lastminStress
    maxTemperature = 100l
    minTemperature = lastminTemperature
    timeMax =lastTimeMax
    timeMin = lastTimeMin
    numOfFatigueLifeLoss = lastnumOfFatigueLifeLoss
    totalFatigueLifeLoss = lasttotalFatigueLifeLoss
    fatigueStress = yingliD
    System.out.println("###############output的值################")
    System.out.println("最大应力：" + maxStress)
    System.out.println("最小应力：" + minStress)
    System.out.println("最高温度：" + maxTemperature)
    System.out.println("最低温度：" + minTemperature)
    System.out.println("最大应力时间：" + timeMax)
    System.out.println("最小应力时间：" + timeMin)
    System.out.println("疲劳寿命损耗计算次数：" + numOfFatigueLifeLoss)
    System.out.println("总的疲劳寿命损耗：" + totalFatigueLifeLoss)
    System.out.println("省煤器计算用疲劳应力 ：" + fatigueStress)



    System.out.println("sparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkguisparkgui")

}