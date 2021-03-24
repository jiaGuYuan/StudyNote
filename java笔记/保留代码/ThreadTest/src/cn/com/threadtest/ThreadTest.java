package cn.com.threadtest;

/**
 * ��ʾ�̵߳Ĵ���������
 */

/**
 * ͨ���̳�Thread�����߳�, ��Ҫͨ��start()����������
 */
class ThreadA extends Thread {
	public ThreadA() {
	}

	public ThreadA(String name) { // ָ���̵߳ı���
		super(name);
	}

	/**
	 * ��Ҫע�����: ��Ҫͨ����Runnable����ָ���߳�Ҫ���еĶ���,��Ҫ�ڼ̳е���������дrun.
	 * ������ϣ��ͨ������target����ָ���߳�Ҫ����target��run����. ������Ϊ����ThreadA����д��run����,
	 * ����ʵ����ִ�е���ThreadA�е�run����.
	 */
	public ThreadA(Runnable target, String name) { // ָ���߳�Ҫ���еĶ���
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

// ��Ϊ��ʾ: --��Щ��������ĸо�.. Thread���ǳ�����, ����ֱ�ӽ�Runnable������Ϊ����������Thread����
class ThreadB extends Thread {

	/** ��Ҫͨ����Runnable����ָ���߳�Ҫ���еĶ���,��Ҫ�ڼ̳е���������дrun. */
	public ThreadB(Runnable target) { // ָ���߳�Ҫ���еĶ���
		super(target);
	}

	public ThreadB(Runnable target, String name) { // ָ���߳�Ҫ���еĶ�����̱߳���
		super(target, name);
	}
}

/**
 * ͨ��ʵ��Runnable�ӿڴ����߳�, ��Ҫ��Ϊ��������Thread�Ĺ��������� ������Ҫ�̳������಻�ܼ̳�Threadʱ,
 * ͨ��ʵ��Runnable�������߳�ִ��--�ֲ���java���ܶ�̳�
 */
class RunnableX implements Runnable {
	@Override
	public void run() {
		System.out.println("RunnableA\t" + Thread.currentThread());
	}

}

public class ThreadTest {

	private static void test() {
		// main�߳�
		Thread curThread = Thread.currentThread();
		curThread.setName("curThread");// �����̵߳ı���
		System.out.println(curThread);

		// a�߳�
		ThreadA a = new ThreadA();
		a.setName("a");
		a.start(); // �����߳�

		// a1�߳�
		ThreadA a1 = new ThreadA("a1");
		a1.setName("a1");
		a1.start();

		// a2�߳�
		ThreadA a2 = new ThreadA(new RunnableX(), "a2"); // ��Ȼ������Runnable[X]����,��ִ�еĻ���ThreadA��run����
		a2.start();

		// b�߳�
		ThreadB b = new ThreadB(new RunnableX(), "b"); // ִ��Runnable[X]��run����
		b.start();

		// r�߳�
		Thread r = new Thread(new RunnableX(), "r");
		r.start();

		System.out.println("test end");
	}

	public static void main(String[] args) {
		test();
	}

}
