package cn.com.synchronizedtest;

public class SynchronizedTest {

	static void testSyn(){
		MyClass c1 = new MyClass();
		new Thread(c1, "t1").start();
		new Thread(c1, "t2").start();
	}
	
	//����Synchronized�ο�����(this�ο��볣��"abd"�ο�)
	static void testSynReference(){
		MyClass c1 = new MyClass();
		MyClass c2 = new MyClass();

		new Thread(c1, "t1").start();
		new Thread(c2, "t2").start();
	}
	
	
	//����Synchronized�ο�����(�ο�ͬһ�����������synchronized��Ӧͬһ����))
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

	// ����
	public void f0() {
		i++;
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + ":" + i);
	}

	// ������������
	public synchronized void f1() {
		i++;
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + ":" + i);
	}

	// ��thisΪ�ο�������������--���ַ�������ֻ��ĳ��������
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

	// �Գ���Ϊ�ο���
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
		// (1) --������ʱ
		//f0();

		// (2) --����ʱ(��thisΪ�ο�����)
		//f1(); // f2()

		// (3) --����Synchronized�ο�����(�ο�ͬһ�����������synchronized��Ӧͬһ����.)
		// ������ԡ�
		//f1(); f2(); //���ο�this
		//f3(); f4(); //���ο�"abc"
		//f2(); f4(); //һ���ο�this��һ���ο�"abc"

		// �ֱ�����f2()��f3()���������--����Synchronized�ο�����(this�ο��볣��"abd"�ο�)
		// f3();
		
		
	}

}