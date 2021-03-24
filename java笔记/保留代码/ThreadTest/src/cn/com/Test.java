package cn.com;

public class Test {
	public static void main(String[] args) {
		Car4S c4 = new Car4S();
		Product p = new Product(c4);
		Consumer c = new Consumer(c4);
		p.start();
		c.start();
	}
}

class EatThread implements Runnable {
	
	private static Object o1 = new Object();
	private static Object o2 = new Object();
	
	private boolean bool;
	
	public void setBool(boolean bool) {
		this.bool = bool;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(bool) {
			synchronized (o1) {
				System.out.println("���������˵�һ������");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (o2) {
					System.out.println("���������˵ڶ�������");
					System.out.println("���ܿ��ԳԷ���");
				}
			}
		}else {
			synchronized (o2) {
				System.out.println("���������˵ڶ�������");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				synchronized (o1) {
					System.out.println("���������˵�һ������");
					System.out.println("���ܿ��ԳԷ���");
				}
			}
		}
	}
	
}