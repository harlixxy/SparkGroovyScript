package org.apache.spark.repl;

import java.net.*;

/**
 * Created by root on 17-1-22.
 */
public class test {

    public static void main(String [] args){

     String str1 = "a";

     String str2 = new String("a");

     System.out.println(str1.intern()==str2);


     String str3 = "a";


     String str4 = new String("a");

     System.out.println(str2==str4);
     System.out.println(str1==str3);

    }
}
