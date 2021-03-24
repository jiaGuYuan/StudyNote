import java.util.*;

/*
  三种类型的注释文档，它们对应于位于注释后面的元素：类、变量或者方法.
  javadoc 只能为 public（公共）和 protected（受保护）成员处理注释文档.
 */

/** 示例程序。列出当前机器上的系统信息。
* @author XXX
* @author YYY
* @version 1.0
* @see 用于引用其他类中的文档
        //@see 类名
	    //@see 完整类名
		//@see 完整类名#方法名
*/
public class Property{

	/** 类和应用程序的唯一入口
	* @param args 控制台字符串数组
	* @return 无返回值
	* @exception 无异常抛出 
	*/
	public static void main(String[] args){
		System.out.println(new Date());
		Properties p = System.getProperties();
		p.list(System.out);
		System.out.println("--Memory Usage:");
		Runtime rt = Runtime.getRuntime();
		System.out.println("Total Memory ="
							+ rt.totalMemory()
							+ " Free Memory ="
							+ rt.freeMemory());

		try{
			Thread.currentThread().sleep(5*1000);
		}catch(InterruptedException e){	}
	}//end main()

	
}//end class
