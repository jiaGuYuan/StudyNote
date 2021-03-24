package cn.com.threadtest;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 演示三个分别以1s, 3s, 5s 间隔变化的时钟
 */
public class TimeView extends JFrame{
	private static final long serialVersionUID = 1L;
	private JPanel pnlMain;
	private MyLabel lbl1;
	private MyLabel lbl2;
	private MyLabel lbl3;
	
	
	public TimeView() {
		pnlMain = new JPanel(null);
		lbl1 = new MyLabel(1000);
		lbl2 = new MyLabel(3000);
		lbl3 = new MyLabel(5000);

		init();
	}

	private void init() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(300, 300);
		lbl1.setBounds(10, 10, 200, 30);
		lbl2.setBounds(10, 50, 200, 30);
		lbl3.setBounds(10, 90, 200, 30);
		
		new Thread(lbl1).start(); //****
		new Thread(lbl2).start();
		new Thread(lbl3).start();
		
		pnlMain.add(lbl1);
		pnlMain.add(lbl2);
		pnlMain.add(lbl3);
		this.add(pnlMain);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new TimeView();
	}
}


/** MyLabel通过继承JLabel使其拥有JLabel的属性, 同时实现Runnable接口使其可以在另一个线程中执行 */
class MyLabel extends JLabel implements Runnable{
	private static final long serialVersionUID = 1L;
	private boolean boolrun;
	private long interval;
	private SimpleDateFormat sdf;
	public MyLabel(long interval_ms) {
		this.setBoolrun(true);
		this.interval = interval_ms;
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public void run() {
		Date date = null;
		
		while(boolrun){
			date = new Date();
			String str = sdf.format(date);
			this.setText(str);
			try {
				Thread.sleep(this.interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setBoolrun(boolean boolrun) {
		this.boolrun = boolrun;
	}

}

