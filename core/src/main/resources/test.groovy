
/**
 * Created by root on 17-4-10.
 */

require{
    a is "jia su du"
    v0 is "chu su du"
    t  is "time for run"
}

onInit{
    a=0
    v0=0
    t=0
}
output{
    s is "wei yi"
}
task{
    s=v0*t +a*t*t/2
}
