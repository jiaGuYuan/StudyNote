import java.util.*;
interface InfA {
	void fa();
	void f();
}
interface InfB {
	void fb();
	void f();
}

class C {
	public void fa() {} 
	private int f(){return 0;}
}

/**
 * MyClass继承C并实现接口InfA和InfB
 * 注意：
 */
class MyClass extends C implements InfA, InfB {
	/* 因为父类C中已经实现了InfA中的 fa()方法, 所以这里可以不实现fa。
	   1. 尝试改变类C中fa()的访问权限
	   2. 尝试改变类C中的fa()的返回值，使之与InfA中fa()方法的返回值不同，看会发生什么。
	*/
	// public void fa() {} 

	public void fb() {}

	/* 接口InfA、InfB中都有 f()方法,且其方法原型完全一致, 所以这里只要实现一次即可。
	   当接口InfA、InfB中 f()方法的原型不一致时(参数列表不一致)，相当于重载。
	   1. 尝试改变InfB中f()的返回值，看会发生什么－－将导致MyClass无法相同实现InfA和InfB.(因为这样即不符合重载也不符合重写)
	   2. 尝试改变类C中f()的访问权限为public，看会发生什么
	*/
	public void f(){}
	//public int f(){return 0;}

}

public class Test {
	static void t(InfA x) { x.fa(); }
	static void u(InfB x) { x.fb(); }
	static void w(C x) { x.fa(); }
	public static void main(String[] args) {
		MyClass i = new MyClass();
		t(i); 
		u(i); 
		w(i); 
	}
}