﻿//国电大安红岗子

require {
    //Excel是以0~287，而数据库是1~288，目前算法以数据库为准
    alarm1 is "全部正常"
    alarm2 is "常规错误重复"
    alarm3 is "电网错误重复"
    alarm4 is "发电机温度错误重复"
    alarm5 is "保留"
    alarm6 is "保留"
    alarm7 is "保留"
    alarm8 is "保留"
    alarm9 is "保留"
    alarm10 is "报警测试"
    alarm11 is "交流电源故障"
    alarm12 is "直流电源1故障"
    alarm13 is "直流电源2故障"
    alarm14 is "参数文件错误"
    alarm15 is "事件文件错误"
    alarm16 is "电量变送器故障"
    alarm17 is "风机振动"
    alarm18 is "低速轴超速开关"
    alarm19 is "高速轴超速开关"
    alarm20 is "安全链断开"
    alarm21 is "机舱急停"
    alarm22 is "控制柜急停"
    alarm23 is "塔基急停"
    alarm24 is "控制器故障"
    alarm25 is "叶片维护"
    alarm26 is "上电复位继电器故障"
    alarm27 is "压力传感器故障"
    alarm28 is "通道故障"
    alarm29 is "模块故障"
    alarm30 is "低速轴转速极限"
    alarm31 is "高速轴转速极限"
    alarm32 is "发电机超速"
    alarm33 is "高速轴超速"
    alarm34 is "低速轴超速"
    alarm35 is "变频器转速信号故障"
    alarm36 is "发电机转速传感器故障"
    alarm37 is "低速轴传感器方向错误"
    alarm38 is "低速轴转速传感器故障"
    alarm39 is "偏航电气制动故障"
    alarm40 is "电缆必须解绕"
    alarm41 is "电缆需要解绕"
    alarm42 is "偏航电机13故障"
    alarm43 is "偏航电机24故障"
    alarm44 is "手动偏航错误"
    alarm45 is "偏航传感器故障"
    alarm46 is "偏航传感器方向错误"
    alarm47 is "电网电流不对称"
    alarm48 is "电网掉电"
    alarm49 is "电网频率过高"
    alarm50 is "电网频率过低"
    alarm51 is "三相相位偏差"
    alarm52 is "电网矢量涌动"
    alarm53 is "相电压过低"
    alarm54 is "相电压过高"
    alarm55 is "生产过剩"
    alarm56 is "截止频率"
    alarm57 is "暂态电网错误"
    alarm58 is "急停超时"
    alarm59 is "快速停机2超时"
    alarm60 is "快速停机3超时"
    alarm61 is "刹车收桨速度过慢"
    alarm62 is "快速停机1超时"
    alarm63 is "手动停机超时"
    alarm64 is "正常停机1超时"
    alarm65 is "正常停机2超时"
    alarm66 is "手动刹车"
    alarm67 is "刹车1反馈错误"
    alarm68 is "刹车2反馈错误"
    alarm69 is "更换刹车片"
    alarm70 is "刹车片磨损"
    alarm71 is "液压泵液位过低"
    alarm72 is "液压压力过高"
    alarm73 is "液压压力过低"
    alarm74 is "液压泵电机故障"
    alarm75 is "液压泵运行时间过长"
    alarm76 is "液压泵运行时间过短"
    alarm77 is "润滑泵电机故障"
    alarm78 is "变频器正在加热"
    alarm79 is "变频器电网错误"
    alarm80 is "变频器断路器断开"
    alarm81 is "变频器错误"
    alarm82 is "变频器超速"
    alarm83 is "变频器未就绪"
    alarm84 is "变频器实际力矩信号错误"
    alarm85 is "变频器入口温度功率减小"
    alarm86 is "变频器功率减小"
    alarm87 is "UPS报警"
    alarm88 is "收桨超时"
    alarm89 is "变桨电池测试"
    alarm90 is "变桨通讯错误"
    alarm91 is "变桨驱动器1错误"
    alarm92 is "变桨驱动器2错误"
    alarm93 is "变桨驱动器3错误"
    alarm94 is "变桨电机1超速"
    alarm95 is "变桨电机2超速"
    alarm96 is "变桨电机3超速"
    alarm97 is "桨角不一致"
    alarm98 is "变桨电池1电压错误"
    alarm99 is "变桨电池2电压错误"
    alarm100 is "变桨电池3电压错误"
    alarm101 is "变桨电池充电回路故障"
    alarm102 is "变桨电池充电器故障"
    alarm103 is "变桨电池电压错误"
    alarm104 is "叶片1极限开关故障"
    alarm105 is "叶片2极限开关故障"
    alarm106 is "叶片3极限开关故障"
    alarm107 is "桨角偏差时间过长"
    alarm108 is "变桨电机1电流信号错误"
    alarm109 is "变桨电机1故障"
    alarm110 is "变桨电机1过流"
    alarm111 is "变桨电机2电流信号错误"
    alarm112 is "变桨电机2故障"
    alarm113 is "变桨电机2过流"
    alarm114 is "变桨电机3电流信号错误"
    alarm115 is "变桨电机3故障"
    alarm116 is "变桨电机3过流"
    alarm117 is "变桨电机电流不对称"
    alarm118 is "变桨电机风扇故障"
    alarm119 is "变桨错误"
    alarm120 is "设定与实际桨角1偏差过大"
    alarm121 is "设定与实际桨角2偏差过大"
    alarm122 is "设定与实际桨角3偏差过大"
    alarm123 is "桨角偏差过大"
    alarm124 is "叶片1收桨速度过慢"
    alarm125 is "叶片2收桨速度过慢"
    alarm126 is "叶片3收桨速度过慢"
    alarm127 is "变桨电机1温度过高"
    alarm128 is "变桨电机2温度过高"
    alarm129 is "变桨电机3温度过高"
    alarm130 is "风速计结冰"
    alarm131 is "风速计全部故障"
    alarm132 is "测冰仪结冰报警"
    alarm133 is "风速测量值过高"
    alarm134 is "风速测量值过低"
    alarm135 is "风向偏离过大"
    alarm136 is "风向标故障"
    alarm137 is "风向标结冰"
    alarm138 is "风速计1故障"
    alarm139 is "风速计2故障"
    alarm140 is "风向不一致"
    alarm141 is "风速不一致"
    alarm142 is "振动通讯错误"
    alarm143 is "塔筒振动传感器故障"
    alarm144 is "塔筒X向1级振动"
    alarm145 is "塔筒Y向1级振动"
    alarm146 is "塔筒X向2级振动"
    alarm147 is "塔筒Y向2级振动"
    alarm148 is "驱动链振动传感器故障"
    alarm149 is "驱动链X向1级振动"
    alarm150 is "驱动链Y向1级振动"
    alarm151 is "驱动链X向2级振动"
    alarm152 is "驱动链Y向2级振动"
    alarm153 is "塔筒X向高频振动"
    alarm154 is "塔筒Y向高频振动"
    alarm155 is "驱动链X向高频振动"
    alarm156 is "驱动链Y向高频振动"
    alarm157 is "手动停机"
    alarm158 is "手动变桨"
    alarm159 is "手动力矩"
    alarm160 is "刹车打滑"
    alarm161 is "极限开关旁路超时"
    alarm162 is "变频器切入超时"
    alarm163 is "变频器脱网"
    alarm164 is "运行桨角超限"
    alarm165 is "输出功率极限"
    alarm166 is "高风切出"
    alarm167 is "并网超时"
    alarm168 is "风场控制停机"
    alarm169 is "输出功率过高"
    alarm170 is "通讯停机"
    alarm171 is "测试转速过高"
    alarm172 is "切入转速超范围"
    alarm173 is "通过共振区超时"
    alarm174 is "低风切出"
    alarm175 is "变频器超出工作范围"
    alarm176 is "极限阵风"
    alarm177 is "变压器温度过高关断变压器"
    alarm178 is "控制器温度过高"
    alarm179 is "变频器入口温度过高"
    alarm180 is "齿轮箱轴承1温度过高"
    alarm181 is "齿轮箱轴承2温度过高"
    alarm182 is "齿轮油温度过高"
    alarm183 is "齿轮油温度过低"
    alarm184 is "发电机轴承1温度过高"
    alarm185 is "发电机轴承2温度过高"
    alarm186 is "发电机定子绕组温度过高"
    alarm187 is "机舱温度过高"
    alarm188 is "舱外温度过高"
    alarm189 is "舱外温度过低"
    alarm190 is "低速轴轴承温度过高"
    alarm191 is "变压器停机"
    alarm192 is "齿轮箱轴承1温度偏高"
    alarm193 is "齿轮箱轴承2温度偏高"
    alarm194 is "发电机轴承1温度偏高"
    alarm195 is "发电机轴承2温度偏高"
    alarm196 is "发电机定子绕组温度偏高"
    alarm197 is "机舱温度偏高"
    alarm198 is "低速轴轴承温度偏高"
    alarm199 is "变压器功率减小"
    alarm200 is "齿轮箱风扇故障"
    alarm201 is "齿轮箱加热器故障"
    alarm202 is "齿轮箱入口压力高"
    alarm203 is "齿轮箱入口压力低"
    alarm204 is "齿轮箱油位低"
    alarm205 is "齿轮箱油泵故障"
    alarm206 is "齿轮箱旁路过滤器堵塞"
    alarm207 is "齿轮箱过滤器堵塞"
    alarm208 is "齿轮油温度功率减小"
    alarm209 is "齿轮箱次要油泵故障"
    alarm210 is "发电机碳刷磨损"
    alarm211 is "发电机风扇2故障"
    alarm212 is "发电机风扇故障"
    alarm213 is "发电机定子绕组PTC"
    alarm214 is "碳刷磨损过滤器堵塞时间过长"
    alarm215 is "发电机避雷器故障"
    alarm216 is "定子绕组温度功率减小"
    alarm217 is "碳刷磨损过滤器堵塞"
    alarm218 is "发电机加热器故障"
    alarm219 is "变桨电机1.2.3 PTC"
    alarm220 is "变桨自主运行"
    alarm221 is "轮毂温度过高"
    alarm222 is "塔筒监测"
    alarm223 is "航标灯失效"
    alarm224 is "航标灯维护警报"
    alarm225 is "发电失败"
    alarm226 is "风场管理功率减小"
    alarm227 is "风场通讯错误"
    alarm228 is "风机关机"
    alarm229 is "机舱位置错误"
    alarm230 is "检查机舱位置"
    alarm231 is "金属温度低"
    alarm232 is "力矩功率不匹配"
    alarm233 is "力矩滤波器1复位"
    alarm234 is "力矩滤波器2复位"
    alarm235 is "力矩信号不匹配"
    alarm236 is "启动超时"
    alarm237 is "刹车非正常抱闸"
    alarm238 is "温度传感器故障"
    alarm239 is "电能控制冲突"
    alarm240 is "变压器电压低"
    alarm241 is "变压器电压高"
    alarm242 is "变压器轻瓦斯保护降功率运行"
    alarm243 is "变压器重瓦斯保护关断变压器"
    alarm244 is "顺时针偏航限位开关"
    alarm245 is "逆时针偏航限位开关"
    alarm246 is "算法调用失败"
    alarm247 is "变浆AB编码器同时故障"
    alarm248 is "变桨驱动器错误"
    alarm249 is "变桨电机超速"
    alarm250 is "叶片交流收桨速度过慢"
    alarm251 is "相电压瞬时过高"
    alarm252 is "BP180叶片1直流收桨速度过慢"
    alarm253 is "BP180叶片2直流收桨速度过慢"
    alarm254 is "BP180叶片3直流收桨速度过慢"
    alarm255 is "快速停机1超时"
    alarm256 is "快速停机3超时"
    alarm257 is "BP75交流收桨速度过慢"
    alarm258 is "变桨电池测试"
    alarm259 is "测试停机超时"
    alarm260 is "变桨测试失败"
    alarm261 is "偏航电机加热温度开关"
    alarm262 is "发电机风扇3热继保护"
    alarm263 is "变桨看门狗故障"
    alarm264 is "机舱加热供电漏电"
    alarm265 is "机舱加热1热继保护"
    alarm266 is "机舱加热2热继保护"
    alarm267 is "机舱加热3热继保护"
    alarm268 is "机舱加热4热继保护"
    alarm269 is "机舱加热5热继保护"
    alarm270 is "偏航电机加热热继保护"
    alarm271 is "变桨润滑泵故障"
    alarm272 is "空转切出"
    alarm273 is "保留"
    alarm274 is "保留"
    alarm275 is "保留"
    alarm276 is "保留"
    alarm277 is "保留"
    alarm278 is "保留"
    alarm279 is "保留"
    alarm280 is "保留"
    alarm281 is "保留"
    alarm282 is "保留"
    alarm283 is "保留"
    alarm284 is "保留"
    alarm285 is "保留"
    alarm286 is "保留"
    alarm287 is "保留"
    alarm288 is "保留"
}
output {
    mergeAlarm0 is "mergeAlarm0"
    mergeAlarm1 is "mergeAlarm1"
    mergeAlarm2 is "mergeAlarm2"
    mergeAlarm3 is "mergeAlarm3"
    mergeAlarm4 is "mergeAlarm4"
    mergeAlarm5 is "mergeAlarm5"
    mergeAlarm6 is "mergeAlarm6"
    mergeAlarm7 is "mergeAlarm7"
    mergeAlarm8 is "mergeAlarm8"
    mergeAlarm9 is "mergeAlarm9"
    mergeAlarm10 is "mergeAlarm10"
    mergeAlarm11 is "mergeAlarm11"
    mergeAlarm12 is "mergeAlarm12"
    mergeAlarm13 is "mergeAlarm13"
    mergeAlarm14 is "mergeAlarm14"
    mergeAlarm15 is "mergeAlarm15"
    mergeAlarm16 is "mergeAlarm16"
    mergeAlarm17 is "mergeAlarm17"
    mergeAlarm18 is "mergeAlarm18"
    mergeAlarm19 is "mergeAlarm19"
    mergeAlarm20 is "mergeAlarm20"
    mergeAlarm21 is "mergeAlarm21"
    mergeAlarm22 is "mergeAlarm22"
    mergeAlarm23 is "mergeAlarm23"
    mergeAlarm24 is "mergeAlarm24"
    mergeAlarm25 is "mergeAlarm25"
    mergeAlarm26 is "mergeAlarm26"
    mergeAlarm27 is "mergeAlarm27"
    mergeAlarm28 is "mergeAlarm28"
    mergeAlarm29 is "mergeAlarm29"
    mergeAlarm30 is "mergeAlarm30"
    mergeAlarm31 is "mergeAlarm31"
    mergeAlarm32 is "mergeAlarm32"
    mergeAlarm33 is "mergeAlarm33"
    mergeAlarm34 is "mergeAlarm34"
    mergeAlarm35 is "mergeAlarm35"
    mergeAlarm36 is "mergeAlarm36"
    mergeAlarm37 is "mergeAlarm37"
    mergeAlarm38 is "mergeAlarm38"
    mergeAlarm39 is "mergeAlarm39"
    mergeAlarm40 is "mergeAlarm40"
    mergeAlarm41 is "mergeAlarm41"
    mergeAlarm42 is "mergeAlarm42"
    mergeAlarm43 is "mergeAlarm43"
    mergeAlarm44 is "mergeAlarm44"
    mergeAlarm45 is "mergeAlarm45"
    mergeAlarm46 is "mergeAlarm46"
    mergeAlarm47 is "mergeAlarm47"
    mergeAlarm48 is "mergeAlarm48"
    mergeAlarm49 is "mergeAlarm49"
    mergeAlarm50 is "mergeAlarm50"
    mergeAlarm51 is "mergeAlarm51"
    mergeAlarm52 is "mergeAlarm52"
    mergeAlarm53 is "mergeAlarm53"
    mergeAlarm54 is "mergeAlarm54"
    mergeAlarm55 is "mergeAlarm55"
    mergeAlarm56 is "mergeAlarm56"
    mergeAlarm57 is "mergeAlarm57"
    mergeAlarm58 is "mergeAlarm58"
    mergeAlarm59 is "mergeAlarm59"
    mergeAlarm60 is "mergeAlarm60"
    mergeAlarm61 is "mergeAlarm61"
    mergeAlarm62 is "mergeAlarm62"
    mergeAlarm63 is "mergeAlarm63"
    mergeAlarm64 is "mergeAlarm64"
    mergeAlarm65 is "mergeAlarm65"
    mergeAlarm66 is "mergeAlarm66"
    mergeAlarm67 is "mergeAlarm67"
    mergeAlarm68 is "mergeAlarm68"
    mergeAlarm69 is "mergeAlarm69"
    mergeAlarm70 is "mergeAlarm70"
    mergeAlarm71 is "mergeAlarm71"
    mergeAlarm72 is "mergeAlarm72"
    mergeAlarm73 is "mergeAlarm73"
    mergeAlarm74 is "mergeAlarm74"
    mergeAlarm75 is "mergeAlarm75"
    mergeAlarm76 is "mergeAlarm76"
    mergeAlarm77 is "mergeAlarm77"
    mergeAlarm78 is "mergeAlarm78"
    mergeAlarm79 is "mergeAlarm79"
    mergeAlarm80 is "mergeAlarm80"
    mergeAlarm81 is "mergeAlarm81"
    mergeAlarm82 is "mergeAlarm82"
    mergeAlarm83 is "mergeAlarm83"
    mergeAlarm84 is "mergeAlarm84"
    mergeAlarm85 is "mergeAlarm85"
    mergeAlarm86 is "mergeAlarm86"
    mergeAlarm87 is "mergeAlarm87"
    mergeAlarm88 is "mergeAlarm88"
    mergeAlarm89 is "mergeAlarm89"
    mergeAlarm90 is "mergeAlarm90"
    mergeAlarm91 is "mergeAlarm91"
    mergeAlarm92 is "mergeAlarm92"
    mergeAlarm93 is "mergeAlarm93"
    mergeAlarm94 is "mergeAlarm94"
    mergeAlarm95 is "mergeAlarm95"
    mergeAlarm96 is "mergeAlarm96"
    mergeAlarm97 is "mergeAlarm97"
    mergeAlarm98 is "mergeAlarm98"
    mergeAlarm99 is "mergeAlarm99"
    mergeAlarm100 is "mergeAlarm100"
    mergeAlarm101 is "mergeAlarm101"
    mergeAlarm102 is "mergeAlarm102"
    mergeAlarm103 is "mergeAlarm103"
    mergeAlarm104 is "mergeAlarm104"
    mergeAlarm105 is "mergeAlarm105"
    mergeAlarm106 is "mergeAlarm106"
    mergeAlarm107 is "mergeAlarm107"
    mergeAlarm108 is "mergeAlarm108"
    mergeAlarm109 is "mergeAlarm109"
    mergeAlarm110 is "mergeAlarm110"
    mergeAlarm111 is "mergeAlarm111"
    mergeAlarm112 is "mergeAlarm112"
    mergeAlarm113 is "mergeAlarm113"
    mergeAlarm114 is "mergeAlarm114"
    mergeAlarm115 is "mergeAlarm115"
    mergeAlarm116 is "mergeAlarm116"
    mergeAlarm117 is "mergeAlarm117"
    mergeAlarm118 is "mergeAlarm118"
    mergeAlarm119 is "mergeAlarm119"
    mergeAlarm120 is "mergeAlarm120"
    mergeAlarm121 is "mergeAlarm121"
    mergeAlarm122 is "mergeAlarm122"
    mergeAlarm123 is "mergeAlarm123"
    mergeAlarm124 is "mergeAlarm124"
    mergeAlarm125 is "mergeAlarm125"
    mergeAlarm126 is "mergeAlarm126"
    mergeAlarm127 is "mergeAlarm127"
    mergeAlarm128 is "mergeAlarm128"
    mergeAlarm129 is "mergeAlarm129"
    mergeAlarm130 is "mergeAlarm130"
    mergeAlarm131 is "mergeAlarm131"
    mergeAlarm132 is "mergeAlarm132"
    mergeAlarm133 is "mergeAlarm133"
    mergeAlarm134 is "mergeAlarm134"
    mergeAlarm135 is "mergeAlarm135"
    mergeAlarm136 is "mergeAlarm136"
    mergeAlarm137 is "mergeAlarm137"
    mergeAlarm138 is "mergeAlarm138"
    mergeAlarm139 is "mergeAlarm139"
    mergeAlarm140 is "mergeAlarm140"
    mergeAlarm141 is "mergeAlarm141"
    mergeAlarm142 is "mergeAlarm142"
    mergeAlarm143 is "mergeAlarm143"
    mergeAlarm144 is "mergeAlarm144"
    mergeAlarm145 is "mergeAlarm145"
    mergeAlarm146 is "mergeAlarm146"
    mergeAlarm147 is "mergeAlarm147"
    mergeAlarm148 is "mergeAlarm148"
    mergeAlarm149 is "mergeAlarm149"
    mergeAlarm150 is "mergeAlarm150"
    mergeAlarm151 is "mergeAlarm151"
    mergeAlarm152 is "mergeAlarm152"
    mergeAlarm153 is "mergeAlarm153"
    mergeAlarm154 is "mergeAlarm154"
    mergeAlarm155 is "mergeAlarm155"
    mergeAlarm156 is "mergeAlarm156"
    mergeAlarm157 is "mergeAlarm157"
    mergeAlarm158 is "mergeAlarm158"
    mergeAlarm159 is "mergeAlarm159"
    mergeAlarm160 is "mergeAlarm160"
    mergeAlarm161 is "mergeAlarm161"
    mergeAlarm162 is "mergeAlarm162"
    mergeAlarm163 is "mergeAlarm163"
    mergeAlarm164 is "mergeAlarm164"
    mergeAlarm165 is "mergeAlarm165"
    mergeAlarm166 is "mergeAlarm166"
    mergeAlarm167 is "mergeAlarm167"
    mergeAlarm168 is "mergeAlarm168"
    mergeAlarm169 is "mergeAlarm169"
    mergeAlarm170 is "mergeAlarm170"
    mergeAlarm171 is "mergeAlarm171"
    mergeAlarm172 is "mergeAlarm172"
    mergeAlarm173 is "mergeAlarm173"
    mergeAlarm174 is "mergeAlarm174"
    mergeAlarm175 is "mergeAlarm175"
    mergeAlarm176 is "mergeAlarm176"
    mergeAlarm177 is "mergeAlarm177"
    mergeAlarm178 is "mergeAlarm178"
    mergeAlarm179 is "mergeAlarm179"
    mergeAlarm180 is "mergeAlarm180"
    mergeAlarm181 is "mergeAlarm181"
    mergeAlarm182 is "mergeAlarm182"
    mergeAlarm183 is "mergeAlarm183"
    mergeAlarm184 is "mergeAlarm184"
    mergeAlarm185 is "mergeAlarm185"
    mergeAlarm186 is "mergeAlarm186"
    mergeAlarm187 is "mergeAlarm187"
    mergeAlarm188 is "mergeAlarm188"
    mergeAlarm189 is "mergeAlarm189"
    mergeAlarm190 is "mergeAlarm190"
    mergeAlarm191 is "mergeAlarm191"
    mergeAlarm192 is "mergeAlarm192"
    mergeAlarm193 is "mergeAlarm193"
    mergeAlarm194 is "mergeAlarm194"
    mergeAlarm195 is "mergeAlarm195"
    mergeAlarm196 is "mergeAlarm196"
    mergeAlarm197 is "mergeAlarm197"
    mergeAlarm198 is "mergeAlarm198"
    mergeAlarm199 is "mergeAlarm199"
    mergeAlarm200 is "mergeAlarm200"
    mergeAlarm201 is "mergeAlarm201"
    mergeAlarm202 is "mergeAlarm202"
    mergeAlarm203 is "mergeAlarm203"
    mergeAlarm204 is "mergeAlarm204"
    mergeAlarm205 is "mergeAlarm205"
    mergeAlarm206 is "mergeAlarm206"
    mergeAlarm207 is "mergeAlarm207"
    mergeAlarm208 is "mergeAlarm208"
    mergeAlarm209 is "mergeAlarm209"
    mergeAlarm210 is "mergeAlarm210"
    mergeAlarm211 is "mergeAlarm211"
    mergeAlarm212 is "mergeAlarm212"
    mergeAlarm213 is "mergeAlarm213"
    mergeAlarm214 is "mergeAlarm214"
    mergeAlarm215 is "mergeAlarm215"
    mergeAlarm216 is "mergeAlarm216"
    mergeAlarm217 is "mergeAlarm217"
    mergeAlarm218 is "mergeAlarm218"
    mergeAlarm219 is "mergeAlarm219"
    mergeAlarm220 is "mergeAlarm220"
    mergeAlarm221 is "mergeAlarm221"
    mergeAlarm222 is "mergeAlarm222"
    mergeAlarm223 is "mergeAlarm223"
    mergeAlarm224 is "mergeAlarm224"
    mergeAlarm225 is "mergeAlarm225"
    mergeAlarm226 is "mergeAlarm226"
    mergeAlarm227 is "mergeAlarm227"
    mergeAlarm228 is "mergeAlarm228"
    mergeAlarm229 is "mergeAlarm229"
    mergeAlarm230 is "mergeAlarm230"
    mergeAlarm231 is "mergeAlarm231"
    mergeAlarm232 is "mergeAlarm232"
    mergeAlarm233 is "mergeAlarm233"
    mergeAlarm234 is "mergeAlarm234"
    mergeAlarm235 is "mergeAlarm235"
    mergeAlarm236 is "mergeAlarm236"
    mergeAlarm237 is "mergeAlarm237"
    mergeAlarm238 is "mergeAlarm238"
    mergeAlarm239 is "mergeAlarm239"
    mergeAlarm240 is "mergeAlarm240"
    mergeAlarm241 is "mergeAlarm241"
    mergeAlarm242 is "mergeAlarm242"
    mergeAlarm243 is "mergeAlarm243"
    mergeAlarm244 is "mergeAlarm244"
    mergeAlarm245 is "mergeAlarm245"
    mergeAlarm246 is "mergeAlarm246"
    mergeAlarm247 is "mergeAlarm247"
    mergeAlarm248 is "mergeAlarm248"
    mergeAlarm249 is "mergeAlarm249"
    mergeAlarm250 is "mergeAlarm250"
    mergeAlarm251 is "mergeAlarm251"
    mergeAlarm252 is "mergeAlarm252"
    mergeAlarm253 is "mergeAlarm253"
    mergeAlarm254 is "mergeAlarm254"
    mergeAlarm255 is "mergeAlarm255"
    mergeAlarm256 is "mergeAlarm256"
    mergeAlarm257 is "mergeAlarm257"
    mergeAlarm258 is "mergeAlarm258"
    mergeAlarm259 is "mergeAlarm259"
    mergeAlarm260 is "mergeAlarm260"
    mergeAlarm261 is "mergeAlarm261"
    mergeAlarm262 is "mergeAlarm262"
    mergeAlarm263 is "mergeAlarm263"
    mergeAlarm264 is "mergeAlarm264"
    mergeAlarm265 is "mergeAlarm265"
    mergeAlarm266 is "mergeAlarm266"
    mergeAlarm267 is "mergeAlarm267"
    mergeAlarm268 is "mergeAlarm268"
    mergeAlarm269 is "mergeAlarm269"
    mergeAlarm270 is "mergeAlarm270"
    mergeAlarm271 is "mergeAlarm271"
    mergeAlarm272 is "mergeAlarm272"
    mergeAlarm273 is "mergeAlarm273"
    mergeAlarm274 is "mergeAlarm274"
    mergeAlarm275 is "mergeAlarm275"
    mergeAlarm276 is "mergeAlarm276"
    mergeAlarm277 is "mergeAlarm277"
    mergeAlarm278 is "mergeAlarm278"
    mergeAlarm279 is "mergeAlarm279"
    mergeAlarm280 is "mergeAlarm280"
    mergeAlarm281 is "mergeAlarm281"
    mergeAlarm282 is "mergeAlarm282"
    mergeAlarm283 is "mergeAlarm283"
    mergeAlarm284 is "mergeAlarm284"
    mergeAlarm285 is "mergeAlarm285"
    mergeAlarm286 is "mergeAlarm286"
    mergeAlarm287 is "mergeAlarm287"
    mergeAlarm288 is "mergeAlarm288"
}
status {
    midAlarm0 is "至少有一个报警"  //取值中间变量
    midAlarm1 is "全部正常"
    midAlarm2 is "常规错误重复"
    midAlarm3 is "电网错误重复"
    midAlarm4 is "发电机温度错误重复"
    midAlarm5 is "保留"
    midAlarm6 is "保留"
    midAlarm7 is "保留"
    midAlarm8 is "保留"
    midAlarm9 is "保留"
    midAlarm10 is "报警测试"
    midAlarm11 is "交流电源故障"
    midAlarm12 is "直流电源1故障"
    midAlarm13 is "直流电源2故障"
    midAlarm14 is "参数文件错误"
    midAlarm15 is "事件文件错误"
    midAlarm16 is "电量变送器故障"
    midAlarm17 is "风机振动"
    midAlarm18 is "低速轴超速开关"
    midAlarm19 is "高速轴超速开关"
    midAlarm20 is "安全链断开"
    midAlarm21 is "机舱急停"
    midAlarm22 is "控制柜急停"
    midAlarm23 is "塔基急停"
    midAlarm24 is "控制器故障"
    midAlarm25 is "叶片维护"
    midAlarm26 is "上电复位继电器故障"
    midAlarm27 is "压力传感器故障"
    midAlarm28 is "通道故障"
    midAlarm29 is "模块故障"
    midAlarm30 is "低速轴转速极限"
    midAlarm31 is "高速轴转速极限"
    midAlarm32 is "发电机超速"
    midAlarm33 is "高速轴超速"
    midAlarm34 is "低速轴超速"
    midAlarm35 is "变频器转速信号故障"
    midAlarm36 is "发电机转速传感器故障"
    midAlarm37 is "低速轴传感器方向错误"
    midAlarm38 is "低速轴转速传感器故障"
    midAlarm39 is "偏航电气制动故障"
    midAlarm40 is "电缆必须解绕"
    midAlarm41 is "电缆需要解绕"
    midAlarm42 is "偏航电机13故障"
    midAlarm43 is "偏航电机24故障"
    midAlarm44 is "手动偏航错误"
    midAlarm45 is "偏航传感器故障"
    midAlarm46 is "偏航传感器方向错误"
    midAlarm47 is "电网电流不对称"
    midAlarm48 is "电网掉电"
    midAlarm49 is "电网频率过高"
    midAlarm50 is "电网频率过低"
    midAlarm51 is "三相相位偏差"
    midAlarm52 is "电网矢量涌动"
    midAlarm53 is "相电压过低"
    midAlarm54 is "相电压过高"
    midAlarm55 is "生产过剩"
    midAlarm56 is "截止频率"
    midAlarm57 is "暂态电网错误"
    midAlarm58 is "急停超时"
    midAlarm59 is "快速停机2超时"
    midAlarm60 is "快速停机3超时"
    midAlarm61 is "刹车收桨速度过慢"
    midAlarm62 is "快速停机1超时"
    midAlarm63 is "手动停机超时"
    midAlarm64 is "正常停机1超时"
    midAlarm65 is "正常停机2超时"
    midAlarm66 is "手动刹车"
    midAlarm67 is "刹车1反馈错误"
    midAlarm68 is "刹车2反馈错误"
    midAlarm69 is "更换刹车片"
    midAlarm70 is "刹车片磨损"
    midAlarm71 is "液压泵液位过低"
    midAlarm72 is "液压压力过高"
    midAlarm73 is "液压压力过低"
    midAlarm74 is "液压泵电机故障"
    midAlarm75 is "液压泵运行时间过长"
    midAlarm76 is "液压泵运行时间过短"
    midAlarm77 is "润滑泵电机故障"
    midAlarm78 is "变频器正在加热"
    midAlarm79 is "变频器电网错误"
    midAlarm80 is "变频器断路器断开"
    midAlarm81 is "变频器错误"
    midAlarm82 is "变频器超速"
    midAlarm83 is "变频器未就绪"
    midAlarm84 is "变频器实际力矩信号错误"
    midAlarm85 is "变频器入口温度功率减小"
    midAlarm86 is "变频器功率减小"
    midAlarm87 is "UPS报警"
    midAlarm88 is "收桨超时"
    midAlarm89 is "变桨电池测试"
    midAlarm90 is "变桨通讯错误"
    midAlarm91 is "变桨驱动器1错误"
    midAlarm92 is "变桨驱动器2错误"
    midAlarm93 is "变桨驱动器3错误"
    midAlarm94 is "变桨电机1超速"
    midAlarm95 is "变桨电机2超速"
    midAlarm96 is "变桨电机3超速"
    midAlarm97 is "桨角不一致"
    midAlarm98 is "变桨电池1电压错误"
    midAlarm99 is "变桨电池2电压错误"
    midAlarm100 is "变桨电池3电压错误"
    midAlarm101 is "变桨电池充电回路故障"
    midAlarm102 is "变桨电池充电器故障"
    midAlarm103 is "变桨电池电压错误"
    midAlarm104 is "叶片1极限开关故障"
    midAlarm105 is "叶片2极限开关故障"
    midAlarm106 is "叶片3极限开关故障"
    midAlarm107 is "桨角偏差时间过长"
    midAlarm108 is "变桨电机1电流信号错误"
    midAlarm109 is "变桨电机1故障"
    midAlarm110 is "变桨电机1过流"
    midAlarm111 is "变桨电机2电流信号错误"
    midAlarm112 is "变桨电机2故障"
    midAlarm113 is "变桨电机2过流"
    midAlarm114 is "变桨电机3电流信号错误"
    midAlarm115 is "变桨电机3故障"
    midAlarm116 is "变桨电机3过流"
    midAlarm117 is "变桨电机电流不对称"
    midAlarm118 is "变桨电机风扇故障"
    midAlarm119 is "变桨错误"
    midAlarm120 is "设定与实际桨角1偏差过大"
    midAlarm121 is "设定与实际桨角2偏差过大"
    midAlarm122 is "设定与实际桨角3偏差过大"
    midAlarm123 is "桨角偏差过大"
    midAlarm124 is "叶片1收桨速度过慢"
    midAlarm125 is "叶片2收桨速度过慢"
    midAlarm126 is "叶片3收桨速度过慢"
    midAlarm127 is "变桨电机1温度过高"
    midAlarm128 is "变桨电机2温度过高"
    midAlarm129 is "变桨电机3温度过高"
    midAlarm130 is "风速计结冰"
    midAlarm131 is "风速计全部故障"
    midAlarm132 is "测冰仪结冰报警"
    midAlarm133 is "风速测量值过高"
    midAlarm134 is "风速测量值过低"
    midAlarm135 is "风向偏离过大"
    midAlarm136 is "风向标故障"
    midAlarm137 is "风向标结冰"
    midAlarm138 is "风速计1故障"
    midAlarm139 is "风速计2故障"
    midAlarm140 is "风向不一致"
    midAlarm141 is "风速不一致"
    midAlarm142 is "振动通讯错误"
    midAlarm143 is "塔筒振动传感器故障"
    midAlarm144 is "塔筒X向1级振动"
    midAlarm145 is "塔筒Y向1级振动"
    midAlarm146 is "塔筒X向2级振动"
    midAlarm147 is "塔筒Y向2级振动"
    midAlarm148 is "驱动链振动传感器故障"
    midAlarm149 is "驱动链X向1级振动"
    midAlarm150 is "驱动链Y向1级振动"
    midAlarm151 is "驱动链X向2级振动"
    midAlarm152 is "驱动链Y向2级振动"
    midAlarm153 is "塔筒X向高频振动"
    midAlarm154 is "塔筒Y向高频振动"
    midAlarm155 is "驱动链X向高频振动"
    midAlarm156 is "驱动链Y向高频振动"
    midAlarm157 is "手动停机"
    midAlarm158 is "手动变桨"
    midAlarm159 is "手动力矩"
    midAlarm160 is "刹车打滑"
    midAlarm161 is "极限开关旁路超时"
    midAlarm162 is "变频器切入超时"
    midAlarm163 is "变频器脱网"
    midAlarm164 is "运行桨角超限"
    midAlarm165 is "输出功率极限"
    midAlarm166 is "高风切出"
    midAlarm167 is "并网超时"
    midAlarm168 is "风场控制停机"
    midAlarm169 is "输出功率过高"
    midAlarm170 is "通讯停机"
    midAlarm171 is "测试转速过高"
    midAlarm172 is "切入转速超范围"
    midAlarm173 is "通过共振区超时"
    midAlarm174 is "低风切出"
    midAlarm175 is "变频器超出工作范围"
    midAlarm176 is "极限阵风"
    midAlarm177 is "变压器温度过高关断变压器"
    midAlarm178 is "控制器温度过高"
    midAlarm179 is "变频器入口温度过高"
    midAlarm180 is "齿轮箱轴承1温度过高"
    midAlarm181 is "齿轮箱轴承2温度过高"
    midAlarm182 is "齿轮油温度过高"
    midAlarm183 is "齿轮油温度过低"
    midAlarm184 is "发电机轴承1温度过高"
    midAlarm185 is "发电机轴承2温度过高"
    midAlarm186 is "发电机定子绕组温度过高"
    midAlarm187 is "机舱温度过高"
    midAlarm188 is "舱外温度过高"
    midAlarm189 is "舱外温度过低"
    midAlarm190 is "低速轴轴承温度过高"
    midAlarm191 is "变压器停机"
    midAlarm192 is "齿轮箱轴承1温度偏高"
    midAlarm193 is "齿轮箱轴承2温度偏高"
    midAlarm194 is "发电机轴承1温度偏高"
    midAlarm195 is "发电机轴承2温度偏高"
    midAlarm196 is "发电机定子绕组温度偏高"
    midAlarm197 is "机舱温度偏高"
    midAlarm198 is "低速轴轴承温度偏高"
    midAlarm199 is "变压器功率减小"
    midAlarm200 is "齿轮箱风扇故障"
    midAlarm201 is "齿轮箱加热器故障"
    midAlarm202 is "齿轮箱入口压力高"
    midAlarm203 is "齿轮箱入口压力低"
    midAlarm204 is "齿轮箱油位低"
    midAlarm205 is "齿轮箱油泵故障"
    midAlarm206 is "齿轮箱旁路过滤器堵塞"
    midAlarm207 is "齿轮箱过滤器堵塞"
    midAlarm208 is "齿轮油温度功率减小"
    midAlarm209 is "齿轮箱次要油泵故障"
    midAlarm210 is "发电机碳刷磨损"
    midAlarm211 is "发电机风扇2故障"
    midAlarm212 is "发电机风扇故障"
    midAlarm213 is "发电机定子绕组PTC"
    midAlarm214 is "碳刷磨损过滤器堵塞时间过长"
    midAlarm215 is "发电机避雷器故障"
    midAlarm216 is "定子绕组温度功率减小"
    midAlarm217 is "碳刷磨损过滤器堵塞"
    midAlarm218 is "发电机加热器故障"
    midAlarm219 is "变桨电机1.2.3 PTC"
    midAlarm220 is "变桨自主运行"
    midAlarm221 is "轮毂温度过高"
    midAlarm222 is "塔筒监测"
    midAlarm223 is "航标灯失效"
    midAlarm224 is "航标灯维护警报"
    midAlarm225 is "发电失败"
    midAlarm226 is "风场管理功率减小"
    midAlarm227 is "风场通讯错误"
    midAlarm228 is "风机关机"
    midAlarm229 is "机舱位置错误"
    midAlarm230 is "检查机舱位置"
    midAlarm231 is "金属温度低"
    midAlarm232 is "力矩功率不匹配"
    midAlarm233 is "力矩滤波器1复位"
    midAlarm234 is "力矩滤波器2复位"
    midAlarm235 is "力矩信号不匹配"
    midAlarm236 is "启动超时"
    midAlarm237 is "刹车非正常抱闸"
    midAlarm238 is "温度传感器故障"
    midAlarm239 is "电能控制冲突"
    midAlarm240 is "变压器电压低"
    midAlarm241 is "变压器电压高"
    midAlarm242 is "变压器轻瓦斯保护降功率运行"
    midAlarm243 is "变压器重瓦斯保护关断变压器"
    midAlarm244 is "顺时针偏航限位开关"
    midAlarm245 is "逆时针偏航限位开关"
    midAlarm246 is "算法调用失败"
    midAlarm247 is "变浆AB编码器同时故障"
    midAlarm248 is "变桨驱动器错误"
    midAlarm249 is "变桨电机超速"
    midAlarm250 is "叶片交流收桨速度过慢"
    midAlarm251 is "相电压瞬时过高"
    midAlarm252 is "BP180叶片1直流收桨速度过慢"
    midAlarm253 is "BP180叶片2直流收桨速度过慢"
    midAlarm254 is "BP180叶片3直流收桨速度过慢"
    midAlarm255 is "快速停机1超时"
    midAlarm256 is "快速停机3超时"
    midAlarm257 is "BP75交流收桨速度过慢"
    midAlarm258 is "变桨电池测试"
    midAlarm259 is "测试停机超时"
    midAlarm260 is "变桨测试失败"
    midAlarm261 is "偏航电机加热温度开关"
    midAlarm262 is "发电机风扇3热继保护"
    midAlarm263 is "变桨看门狗故障"
    midAlarm264 is "机舱加热供电漏电"
    midAlarm265 is "机舱加热1热继保护"
    midAlarm266 is "机舱加热2热继保护"
    midAlarm267 is "机舱加热3热继保护"
    midAlarm268 is "机舱加热4热继保护"
    midAlarm269 is "机舱加热5热继保护"
    midAlarm270 is "偏航电机加热热继保护"
    midAlarm271 is "变桨润滑泵故障"
    midAlarm272 is "空转切出"
    midAlarm273 is "保留"
    midAlarm274 is "保留"
    midAlarm275 is "保留"
    midAlarm276 is "保留"
    midAlarm277 is "保留"
    midAlarm278 is "保留"
    midAlarm279 is "保留"
    midAlarm280 is "保留"
    midAlarm281 is "保留"
    midAlarm282 is "保留"
    midAlarm283 is "保留"
    midAlarm284 is "保留"
    midAlarm285 is "保留"
    midAlarm286 is "保留"
    midAlarm287 is "保留"
    midAlarm288 is "保留"
}
onInit {
    midAlarm0 = null
    midAlarm1 = null
    midAlarm2 = null
    midAlarm3 = null
    midAlarm4 = null
    midAlarm5 = null
    midAlarm6 = null
    midAlarm7 = null
    midAlarm8 = null
    midAlarm9 = null
    midAlarm10 = null
    midAlarm11 = null
    midAlarm12 = null
    midAlarm13 = null
    midAlarm14 = null
    midAlarm15 = null
    midAlarm16 = null
    midAlarm17 = null
    midAlarm18 = null
    midAlarm19 = null
    midAlarm20 = null
    midAlarm21 = null
    midAlarm22 = null
    midAlarm23 = null
    midAlarm24 = null
    midAlarm25 = null
    midAlarm26 = null
    midAlarm27 = null
    midAlarm28 = null
    midAlarm29 = null
    midAlarm30 = null
    midAlarm31 = null
    midAlarm32 = null
    midAlarm33 = null
    midAlarm34 = null
    midAlarm35 = null
    midAlarm36 = null
    midAlarm37 = null
    midAlarm38 = null
    midAlarm39 = null
    midAlarm40 = null
    midAlarm41 = null
    midAlarm42 = null
    midAlarm43 = null
    midAlarm44 = null
    midAlarm45 = null
    midAlarm46 = null
    midAlarm47 = null
    midAlarm48 = null
    midAlarm49 = null
    midAlarm50 = null
    midAlarm51 = null
    midAlarm52 = null
    midAlarm53 = null
    midAlarm54 = null
    midAlarm55 = null
    midAlarm56 = null
    midAlarm57 = null
    midAlarm58 = null
    midAlarm59 = null
    midAlarm60 = null
    midAlarm61 = null
    midAlarm62 = null
    midAlarm63 = null
    midAlarm64 = null
    midAlarm65 = null
    midAlarm66 = null
    midAlarm67 = null
    midAlarm68 = null
    midAlarm69 = null
    midAlarm70 = null
    midAlarm71 = null
    midAlarm72 = null
    midAlarm73 = null
    midAlarm74 = null
    midAlarm75 = null
    midAlarm76 = null
    midAlarm77 = null
    midAlarm78 = null
    midAlarm79 = null
    midAlarm80 = null
    midAlarm81 = null
    midAlarm82 = null
    midAlarm83 = null
    midAlarm84 = null
    midAlarm85 = null
    midAlarm86 = null
    midAlarm87 = null
    midAlarm88 = null
    midAlarm89 = null
    midAlarm90 = null
    midAlarm91 = null
    midAlarm92 = null
    midAlarm93 = null
    midAlarm94 = null
    midAlarm95 = null
    midAlarm96 = null
    midAlarm97 = null
    midAlarm98 = null
    midAlarm99 = null
    midAlarm100 = null
    midAlarm101 = null
    midAlarm102 = null
    midAlarm103 = null
    midAlarm104 = null
    midAlarm105 = null
    midAlarm106 = null
    midAlarm107 = null
    midAlarm108 = null
    midAlarm109 = null
    midAlarm110 = null
    midAlarm111 = null
    midAlarm112 = null
    midAlarm113 = null
    midAlarm114 = null
    midAlarm115 = null
    midAlarm116 = null
    midAlarm117 = null
    midAlarm118 = null
    midAlarm119 = null
    midAlarm120 = null
    midAlarm121 = null
    midAlarm122 = null
    midAlarm123 = null
    midAlarm124 = null
    midAlarm125 = null
    midAlarm126 = null
    midAlarm127 = null
    midAlarm128 = null
    midAlarm129 = null
    midAlarm130 = null
    midAlarm131 = null
    midAlarm132 = null
    midAlarm133 = null
    midAlarm134 = null
    midAlarm135 = null
    midAlarm136 = null
    midAlarm137 = null
    midAlarm138 = null
    midAlarm139 = null
    midAlarm140 = null
    midAlarm141 = null
    midAlarm142 = null
    midAlarm143 = null
    midAlarm144 = null
    midAlarm145 = null
    midAlarm146 = null
    midAlarm147 = null
    midAlarm148 = null
    midAlarm149 = null
    midAlarm150 = null
    midAlarm151 = null
    midAlarm152 = null
    midAlarm153 = null
    midAlarm154 = null
    midAlarm155 = null
    midAlarm156 = null
    midAlarm157 = null
    midAlarm158 = null
    midAlarm159 = null
    midAlarm160 = null
    midAlarm161 = null
    midAlarm162 = null
    midAlarm163 = null
    midAlarm164 = null
    midAlarm165 = null
    midAlarm166 = null
    midAlarm167 = null
    midAlarm168 = null
    midAlarm169 = null
    midAlarm170 = null
    midAlarm171 = null
    midAlarm172 = null
    midAlarm173 = null
    midAlarm174 = null
    midAlarm175 = null
    midAlarm176 = null
    midAlarm177 = null
    midAlarm178 = null
    midAlarm179 = null
    midAlarm180 = null
    midAlarm181 = null
    midAlarm182 = null
    midAlarm183 = null
    midAlarm184 = null
    midAlarm185 = null
    midAlarm186 = null
    midAlarm187 = null
    midAlarm188 = null
    midAlarm189 = null
    midAlarm190 = null
    midAlarm191 = null
    midAlarm192 = null
    midAlarm193 = null
    midAlarm194 = null
    midAlarm195 = null
    midAlarm196 = null
    midAlarm197 = null
    midAlarm198 = null
    midAlarm199 = null
    midAlarm200 = null
    midAlarm201 = null
    midAlarm202 = null
    midAlarm203 = null
    midAlarm204 = null
    midAlarm205 = null
    midAlarm206 = null
    midAlarm207 = null
    midAlarm208 = null
    midAlarm209 = null
    midAlarm210 = null
    midAlarm211 = null
    midAlarm212 = null
    midAlarm213 = null
    midAlarm214 = null
    midAlarm215 = null
    midAlarm216 = null
    midAlarm217 = null
    midAlarm218 = null
    midAlarm219 = null
    midAlarm220 = null
    midAlarm221 = null
    midAlarm222 = null
    midAlarm223 = null
    midAlarm224 = null
    midAlarm225 = null
    midAlarm226 = null
    midAlarm227 = null
    midAlarm228 = null
    midAlarm229 = null
    midAlarm230 = null
    midAlarm231 = null
    midAlarm232 = null
    midAlarm233 = null
    midAlarm234 = null
    midAlarm235 = null
    midAlarm236 = null
    midAlarm237 = null
    midAlarm238 = null
    midAlarm239 = null
    midAlarm240 = null
    midAlarm241 = null
    midAlarm242 = null
    midAlarm243 = null
    midAlarm244 = null
    midAlarm245 = null
    midAlarm246 = null
    midAlarm247 = null
    midAlarm248 = null
    midAlarm249 = null
    midAlarm250 = null
    midAlarm251 = null
    midAlarm252 = null
    midAlarm253 = null
    midAlarm254 = null
    midAlarm255 = null
    midAlarm256 = null
    midAlarm257 = null
    midAlarm258 = null
    midAlarm259 = null
    midAlarm260 = null
    midAlarm261 = null
    midAlarm262 = null
    midAlarm263 = null
    midAlarm264 = null
    midAlarm265 = null
    midAlarm266 = null
    midAlarm267 = null
    midAlarm268 = null
    midAlarm269 = null
    midAlarm270 = null
    midAlarm271 = null
    midAlarm272 = null
    midAlarm273 = null
    midAlarm274 = null
    midAlarm275 = null
    midAlarm276 = null
    midAlarm277 = null
    midAlarm278 = null
    midAlarm279 = null
    midAlarm280 = null
    midAlarm281 = null
    midAlarm282 = null
    midAlarm283 = null
    midAlarm284 = null
    midAlarm285 = null
    midAlarm286 = null
    midAlarm287 = null
    midAlarm288 = null
}
time_align { nullable }
task {
    //循环为1~288
    def isAlarm = false

    for (int i = 1; i <= 288; i++) {
        def alarm = delegate["alarm" + i]
        def midAlarm = delegate["midAlarm" + i]
        def ovalue = null
        if (alarm != null) {
            ovalue = alarm.value
            if (midAlarm == null || midAlarm.value != ovalue) {
                delegate["mergeAlarm" + i] = ovalue
            }
            delegate["midAlarm" + i] = ovalue
            midAlarm = ovalue
        }
        if (!isAlarm && midAlarm == 1) {
            isAlarm = true
        }
    }

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