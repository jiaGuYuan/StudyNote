/**
  java [options] -jar jarfile [args...]
  Java传递参数测试
   java -jar -DKeyTestD=valueTestD app.jar aa bb cc --KeyTest=valueTest
   系统属性以 -Dkey=value的方式给出.
              .jar后面的是main函数的参数
 */

import java.util.*;
 
public class JavaTransferParaTest{

    /** 类和应用程序的唯一入口
    * @param args 控制台字符串数组
    * @return 无返回值
    * @exception 无异常抛出 
    */
    public static void main(String[] args){
        System.out.println(new Date());
        
        System.out.println("system args:");
        //Properties p = System.getProperties();
        //p.list(System.out);
        //System.out.println("");
        String KeyTest = System.getProperty("KeyTest");
        if (KeyTest != null){
            System.out.println("\tKeyTest=" + KeyTest);
        }
        String KeyTestD = System.getProperty("KeyTestD");
        if (KeyTestD != null){
            System.out.println("\tKeyTestD=" + KeyTestD);
        }
        System.out.println("");
       
        System.out.println("main args:");
        for(int i=0; i<args.length; i++){
            System.out.println("\t" + args[i]);
        }
        System.out.println("");
        
        

        //try{
        //    Thread.currentThread().sleep(5*1000);
        //}catch(InterruptedException e){    }
    }
}
