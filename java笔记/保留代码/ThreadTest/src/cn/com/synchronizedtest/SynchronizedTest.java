package cn.com.synchronizedtest;

public class SynchronizedTest {

	static void testSyn(){
		MyClass c1 = new MyClass();
		new Thread(c1, "t1").start();
		new Thread(c1, "t2").start();
	}
	
	//测试Synchronized参考对象(this参考与常量"abd"参考)
	static void testSynReference(){
		MyClass c1 = new MyClass();
		MyClass c2 = new MyClass();

		new Thread(c1, "t1").start();
		new Thread(c2, "t2").start();
	}
	
	
	//测试Synchronized参考对象(参考同一个对象的所有synchronized对应同一把锁))
	static void testSynReference2() {
		MyClass c1 = new MyClass();

		new Thread(c1, "t1").start();
		c1.f4();
	}
	
	public static void main(String[] args) {
		testSynReference2();
	}

}

class MyClass implements Runnable {
	private int i = 0;

	// 不锁
	public void f0() {
		i++;
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + ":" + i);
	}

	// 锁整个方法体
	public synchronized void f1() {
		i++;
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + ":" + i);
	}

	// 以this为参考锁整个方法体--这种方法可以只锁某个作用域
	public void f2() {
		synchronized (this) {
			i++;
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}

	// 以常量为参考锁
	public void f3() {
		synchronized ("") {
			i++;
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}


	public void f4() {
		System.out.println(Thread.currentThread().getName() + ":f4:" + i);
		synchronized ("abc") {//
			i++;
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + ":" + i);
		}
	}
	
	
	@Override
	public void run() {
		// (1) --不加锁时
		//f0();

		// (2) --加锁时(以this为参考加锁)
		//f1(); // f2()

		// (3) --测试Synchronized参考对象(参考同一个对象的所有synchronized对应同一把锁.)
		// 三组测试。
		//f1(); f2(); //都参考this
		//f3(); f4(); //都参考"abc"
		//f2(); f4(); //一个参考this，一个参考"abc"

		// 分别运行f2()与f3()，体会区别--测试Synchronized参考对象(this参考与常量"abd"参考)
		// f3();
		
		
	}

}