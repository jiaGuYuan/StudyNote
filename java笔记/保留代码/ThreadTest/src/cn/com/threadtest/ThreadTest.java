package cn.com.threadtest;

/**
 * 演示线程的创建与启动
 */

/**
 * 通过继承Thread创建线程, 需要通过start()方法来启动
 */
class ThreadA extends Thread {
	public ThreadA() {
	}

	public ThreadA(String name) { // 指定线程的别名
		super(name);
	}

	/**
	 * 需要注意的是: 当要通过传Runnable对象指定线程要运行的对象,不要在继承的类中再重写run.
	 * 这里我希望通过传递target对象指定线程要运行target的run方法. 但是因为我在ThreadA中重写了run方法,
	 * 所以实际上执行的是ThreadA中的run方法.
	 */
	public ThreadA(Runnable target, String name) { // 指定线程要运行的对象
		super(target, name);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(Thread.currentThread());
	}
}

// 作为演示: --有些画蛇添足的感觉.. Thread不是抽象类, 可以直接将Runnable对象作为参数来构造Thread对象
class ThreadB extends Thread {

	/** 当要通过传Runnable对象指定线程要运行的对象,不要在继承的类中再重写run. */
	public ThreadB(Runnable target) { // 指定线程要运行的对象
		super(target);
	}

	public ThreadB(Runnable target, String name) { // 指定线程要运行的对象和线程别名
		super(target, name);
	}
}

/**
 * 通过实现Runnable接口创建线程, 需要作为参数传入Thread的构造来启动 当对象要继承其他类不能继承Thread时,
 * 通过实现Runnable来创建线程执行--弥补了java不能多继承
 */
class RunnableX implements Runnable {
	@Override
	public void run() {
		System.out.println("RunnableA\t" + Thread.currentThread());
	}

}

public class ThreadTest {

	private static void test() {
		// main线程
		Thread curThread = Thread.currentThread();
		curThread.setName("curThread");// 设置线程的别名
		System.out.println(curThread);

		// a线程
		ThreadA a = new ThreadA();
		a.setName("a");
		a.start(); // 启动线程

		// a1线程
		ThreadA a1 = new ThreadA("a1");
		a1.setName("a1");
		a1.start();

		// a2线程
		ThreadA a2 = new ThreadA(new RunnableX(), "a2"); // 虽然传递了Runnable[X]对象,但执行的还是ThreadA的run方法
		a2.start();

		// b线程
		ThreadB b = new ThreadB(new RunnableX(), "b"); // 执行Runnable[X]的run方法
		b.start();

		// r线程
		Thread r = new Thread(new RunnableX(), "r");
		r.start();

		System.out.println("test end");
	}

	public static void main(String[] args) {
		test();
	}

}
