package cn.com;

public class Car4S {
	private Car[] c4;
	private int index;
	public Car4S() {
		c4 = new Car[5];
	}
	
	public synchronized void push(Car c) {
		while(index >= c4.length) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("生产者生产了："+c.carId);
		c4[index] = c;
		index++;
		this.notifyAll();
	}
	
	public synchronized Car pop() {
		while(index <= 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		index--;
		Car c = c4[index];
		System.out.println("消费者消费了："+c.carId);
		this.notifyAll();
		return c;
	}
}
