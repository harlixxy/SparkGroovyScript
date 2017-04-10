
/**
 * Created by root on 17-4-10.
 */

require{
    a is "jia su du"
    v0 is "chu su du"
    t  is "time for run"
}
status{
   pian is "pian cha"
}

onInit{
   pian = 200
}
output{
    s is "wei yi"
    s1 is "wei yi 2"
}
task{


    s=v0*t +a*t*t + pian
    s1 = s + 200
}
