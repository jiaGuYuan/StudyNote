package cn.com;

import java.util.Random;

public class Product extends Thread {
	
	private Car4S c4;
	private Random r;
	
	public Product(Car4S c4) {
		this.c4 = c4;
		r = new Random();
	}
	
	public void run() {
		for(int i = 1;i <= 20;i++) {
			try {
				Thread.sleep(r.nextInt(300)+1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Car c = new Car(i);
			c4.push(c);
		}
	}
}
