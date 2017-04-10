
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
}
task{

    System.out.println("aaaaaaaaaaa:"+a)
    System.out.println("v0v0v0v0v0v0:"+v0)
    System.out.println("tttttttttttt:"+t)
    s=v0*t +a*t*t/2 + pian
}
