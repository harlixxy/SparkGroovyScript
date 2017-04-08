import dao.IElasticModulus
import dao.IMaterialExpansionCoefficient
import dao.IMaterialParam
import dao.impl.ElasticModulus
import dao.impl.MaterialExpansionCoefficient
import dao.impl.MaterialParam
import dao.impl.StressAnalysis
import net.sf.json.JSONObject

import java.text.DecimalFormat


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
    I is "蠕变寿命损耗计算温度范围标识符I"
    II is "蠕变寿命损耗计算温度范围标识符II"

}


onInit {
    lastmaxStress = null
    lastminStress = null
    lastmaxTemperature = null
    lastminTemperature = null
    s = 0
    lastTimeMax = 0D
    lastTimeMin = 0D
    lastnumOfFatigueLifeLoss = 0D
    lasttotalFatigueLifeLoss = 0D

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

    maxStress is "最大应力"
    minStress is "最小应力"
    maxTemperature is "最高温度"
    minTemperature is "最低温度"
    timeMax is "最大应力时间"
    timeMin is "最小应力时间"
    numOfFatigueLifeLoss is "疲劳寿命损耗计算次数"
    fatigueLifeLoss is "本次疲劳寿命损耗"
    totalFatigueLifeLoss is "总的疲劳寿命损耗"

    fatigueStree is "疲劳应力"
    creepStress is "蠕变应力"

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

    if (lastmaxStress == null || dqpress.timestamp - s1 >= 5 * 60)
    {
        //计算
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        Random rand = new Random();

        //省煤器
        float fatigueStree_1 = rand.nextFloat() * 10000.00;
        String fatigueStree_s = dcmFmt.format(fatigueStree_1);  //疲劳应力_省煤器
        float fatigueStree = Float.parseFloat(fatigueStree_s);

        float creepStress_1  = rand.nextFloat() *10000;
        String creepStress_s = dcmFmt.format(creepStress_1);  //蠕变应力
        float creepStress = Float.parseFloat(creepStress_s);

        float totalLoss_1 = rand.nextFloat() * 3;
        String totalLoss_s = dcmFmt.format(totalLoss_1);   //总寿命损耗_省煤器
        float totalLoss = Float.parseFloat(totalLoss_s);

        //高过
        float fatigueStree_gaoguo = rand.nextFloat() * 10000;  //疲劳应力_高过

        float cx_1 = rand.nextFloat() * 1;  //C点x坐标_高过
        String cx_s = dcmFmt.format(cx_1);
        float cx = Float.parseFloat(cx_s);

        float cy_1 = rand.nextFloat() * 1;  //C点y坐标_高过
        String cy_s = dcmFmt.format(cy_1);
        float cy = Float.parseFloat(cy_s);

        float ax_1 = rand.nextFloat() * 1;  //A点x坐标_高过
        String ax_s = dcmFmt.format(ax_1);
        float ax = Float.parseFloat(ax_s);

        float ay_1 = rand.nextFloat() * 1;  //A点y坐标_高过
        String ay_s = dcmFmt.format(ay_1);
        float ay = Float.parseFloat(ay_s);

        float accepted_1 = rand.nextFloat() * 10000;  //高温部件允许的总的寿命损耗_高过
        String accepted_s = dcmFmt.format(accepted_1);  totalFatigueLifeLoss
        float accepted = Float.parseFloat(accepted_s);

        float totalCreepStress_1 = rand.nextFloat() * 1000;  //总的蠕变寿命损耗_高过
        String totalCreepStress_s = dcmFmt.format(totalCreepStress_1);
        float totalCreepStress = Float.parseFloat(totalCreepStress_s);

        float totalFatigueLifeLoss_1 = rand.nextFloat() * 1000;  //总的疲劳寿命损耗_高过
        String totalFatigueLifeLoss_s = dcmFmt.format(totalFatigueLifeLoss_1);
        float totalFatigueLifeLoss = Float.parseFloat(totalFatigueLifeLoss_s);

        //省煤器输出
        System.out.println(fatigueStree);
        System.out.println(creepStress);
        System.out.println(totalLoss);

        //高过输出
        System.out.println(fatigueStree_gaoguo);
        System.out.println(cx);
        System.out.println(cy);
        System.out.println(ax);
        System.out.println(ay);
        System.out.println(accepted);
        System.out.println(totalCreepStress);
        System.out.println(totalFatigueLifeLoss);

        lastmaxStress = dqpress.timestamp;
    }

//    System.out.println("############计算比较后疲劳计算数据############");
//    System.out.println("疲劳应力计算值:" + yingliD);
//    System.out.println("疲劳最高温度:" + lastmaxTemperature);
//    System.out.println("疲劳最低温度:" + lastminTemperature);
//    System.out.println("疲劳最大应力时间:" + lastTimeMax);
//    System.out.println("疲劳最大应力:" + lastmaxStress);
//    System.out.println("疲劳最小应力时间:" + lastTimeMin);
//    System.out.println("疲劳最小应力:" + lastminStress);
//    System.out.println("标识符s:" + s);
//    System.out.println("总的疲劳寿命损耗：" + lasttotalFatigueLifeLoss)
//    System.out.println("###########################################");
//
//    System.out.println("###############output的值################")
//    System.out.println("最大应力：" + maxStress )
//    System.out.println("最小应力：" + minStress )
//    System.out.println("最高温度：" + maxTemperature  )
//    System.out.println("最低温度：" + minTemperature  )
//    System.out.println("最大应力时间：" + timeMax  )
//    System.out.println("最小应力时间：" + timeMin  )
//    System.out.println("疲劳寿命损耗计算次数：" + numOfFatigueLifeLoss  )
//
//    System.out.println("总的疲劳寿命损耗：" + totalFatigueLifeLoss  )
//    System.out.println("省煤器计算用疲劳应力 ：" + fatigueStree  )
//    System.out.println("高过集箱计算用疲劳应力 ：" + creepStress  )
//
//    System.out.println("起始时间：" + timeStart   )
//    System.out.println("结束时间：" + timeEnd   )
//    System.out.println("最高筒体温度：" + maxCreepTemperature   )
//    System.out.println("最低筒体温度：" + minCreepTemperature   )
//    System.out.println("最高蠕变应力：" + maxCreepStress   )
//    System.out.println("最低蠕变应力：" + minCreepStress    )
//    System.out.println("该时间段内的蠕变寿命损耗：" + creepStressi    )
//    System.out.println("该次蠕变寿命持续时间：" + periodOfTimei    )
//    System.out.println("总的蠕变寿命损耗：" + totalCreepStress    )
//
//    System.out.println("C点x坐标：" + cx     )
//    System.out.println("C点y坐标：" + cy     )
//    System.out.println("A点x坐标：" + ax     )
//    System.out.println("A点y坐标：" + ay     )
//    System.out.println("高温部件允许的总的寿命损耗：" + accepted      )
//    System.out.println("总寿命损耗：" + totalLoss      )
//
//
//    System.out.println("############################################")

}