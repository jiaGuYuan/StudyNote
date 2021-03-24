package cn.com.threadtest;

public class ThreadTest2 {
	public static void main(String[] args) {
		
	}
}




class MyThread extends Thread {
	public MyThread() {
	}

	public MyThread(String name) { // 指定线程的别名
		super(name);
	}

	
	@Override
	public void run() {
		
		System.out.println(Thread.currentThread());
	}
}