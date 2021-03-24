//friendlyimport c05.PermissionsTest;
import java.util.*;

public class T{
	

	public static void main(String[] args) {
	   	
	   	Integer i = new Integer(1);
	   	f(i);
	   	System.out.println(i);

	   	String str = "aaa";
	   	fstr(str);
	   	System.out.println(str);

		int[] a = {0,1};
		farr(a);
		System.out.println(a[0]);

		StringBuffer strbuf = new StringBuffer();
		fstrbuf(strbuf);
		System.out.println(strbuf);
	}

	public static void f(Integer i){
		i++;
	}

	public static void fstr(String str){
		str = "ssss";
	}

	public static void farr(int[] arr){
		arr[0] = 2;
	}

	public static void fstrbuf(StringBuffer strbuf){
		strbuf.append("ssss");
	}


}

