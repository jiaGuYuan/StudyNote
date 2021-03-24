import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import java.lang.CloneNotSupportedException;

//对于引用类型传参是传对象的引用(相当于传指针)
class PassHandles {
	static void f(PassHandles h) {
		System.out.println("h inside f(): " + h);
	}
	public static void main(String[] args) {
		PassHandles p = new PassHandles();
		System.out.println("p inside main(): " + p);
		f(p);
	}
} 


class Int{
	private int i;
	public Int(int ii) { i = ii; }
	public void increment() { i++; }
	public String toString() {
		return Integer.toString(i);
	}
}



class IntDeepCopy implements Cloneable{
	private int i;
	public IntDeepCopy(int ii) { i = ii; }
	public void increment() { i++; }
	public String toString() {
		return Integer.toString(i);
	}

	public Object clone() throws CloneNotSupportedException{
		IntDeepCopy intDeep = new IntDeepCopy(this.i);
		return intDeep;
	}
}

//演示深拷贝的clone() --这里只对元素类型IntDeepCopy进行了处理
class MyVector extends Vector implements Cloneable{
	public Object clone() {
		//法一
		// Vector v = new Vector();
		// for(int i=0; i<this.size(); i++){
		// 	try{
		// 		v.add(((IntDeepCopy)this.get(i)).clone());
		// 	}catch (CloneNotSupportedException e){
		// 		e.printStackTrace();
		// 	}
		// }

		//法二
		Vector v = (Vector)super.clone();
		for(int i=0; i<this.size(); i++){
			try{
				v.set(i, ((IntDeepCopy)this.get(i)).clone());
			}catch (CloneNotSupportedException e){
				e.printStackTrace();
			}
		}

		return v;
	}
}

//默认的clone成员进行的是浅拷贝
class Cloning {
	public static void main(String[] args) throws Exception{

		//Vector的clone成员进行的是浅拷贝--只是简单的拷贝其成员--并未跟踪拷贝其成员的指向(比如序列化进行的就是深拷贝)
		Vector v = new Vector();
		for(int i = 0; i < 10; i++ )
			v.addElement(new Int(i));
		System.out.println("v: " + v);
		Vector v2 = (Vector)v.clone();
		for(Enumeration e = v2.elements();	e.hasMoreElements(); )
			((Int)e.nextElement()).increment();
		System.out.println("v: " + v);


		//MyVector演示了深拷贝版的clone()
		System.out.println("------------------------");
		MyVector vd = new MyVector();
		for(int i = 0; i < 10; i++ )
			vd.addElement(new IntDeepCopy(i));
		System.out.println("vd: " + vd);
		Vector vd2 = (Vector)(vd.clone());
		for(Enumeration e = vd2.elements();	e.hasMoreElements(); )
			((IntDeepCopy)e.nextElement()).increment();
		System.out.println("vd: " + vd);
		System.out.println("vd2: " + vd2);
	}
} 



public class TransmitArg{
	public static void main(String[] args) throws Exception{

		//PassHandles.main(args);

		Cloning.main(args);
	}
}


