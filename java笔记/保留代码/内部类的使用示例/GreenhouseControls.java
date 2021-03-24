//: GreenhouseControls.java
//在一个类中产生了控制系统的一个特定的应用。 内部类允许为每种类型的事件封装不同的功能。
//展示了内部类的作用

package c07.controller;

public class GreenhouseControls extends Controller {
	private boolean light = false;
	private boolean water = false;
	private String thermostat = "Day";
	
	//内部类实现开灯事件控制
	private class LightOn extends Event {
		public LightOn(long eventTime) {
			super(eventTime);
		}
		public void action() {
			// 把硬件控制代码放在这里来物理开灯。
			light = true;
		}
		public String description() {
			return "Light is on";
		}
	}

	//内部类实现关灯事件控制
	private class LightOff extends Event {
		public LightOff(long eventTime) {
			super(eventTime);
		}
		public void action() {
			// 把硬件控制代码放在这里来物理关灯。
			light = false;
		}
		public String description() {
			return "Light is off";
		}
	}

	//内部类实现打开供水事件控制
	private class WaterOn extends Event {
		public WaterOn(long eventTime) {
			super(eventTime);
		}
		public void action() {
			// Put hardware control code here
			water = true;
		}
		public String description() {
			return "Greenhouse water is on";
		}
	}

	private class WaterOff extends Event {
		public WaterOff(long eventTime) {
			super(eventTime);
		}
		public void action() {
			// Put hardware control code here
			water = false;
		}
		public String description() {
			return "Greenhouse water is off";
		}
	}

	private class ThermostatNight extends Event {
		public ThermostatNight(long eventTime) {
			super(eventTime);
		}
		public void action() {
			// Put hardware control code here
			thermostat = "Night";
		}
		public String description() {
			return "Thermostat on night setting";
		}
	}

	private class ThermostatDay extends Event {
		public ThermostatDay(long eventTime) {
			super(eventTime);
		}
		public void action() {
			// Put hardware control code here
			thermostat = "Day";
		}
		public String description() {
			return "Thermostat on day setting";
		}
	}

	// 一个action()函数的例子，它将下一个事件加入事件列表中:
	private int rings; //用于指定响铃的次数
	private class Bell extends Event {
		public Bell(long eventTime) {
			super(eventTime);
		}
		public void action() {
			// 响铃每隔2秒
			System.out.println("Bing!");
			if(--rings > 0)
				addEvent(new Bell(System.currentTimeMillis() + 2000));
		}
		public String description() {
			return "Ring bell";
		}
	}

	//重启事件控制
	private class Restart extends Event {
		public Restart(long eventTime) {
			super(eventTime);
		}

		public void action() {
				long tm = System.currentTimeMillis();
				// 在这可以不进行硬编码，在这里从文本文件解析配置信息
				rings = 5;
				addEvent(new ThermostatNight(tm));

				addEvent(new LightOn(tm + 1000));
				addEvent(new LightOff(tm + 2000));

				addEvent(new WaterOn(tm + 3000));
				addEvent(new WaterOff(tm + 8000));

				addEvent(new Bell(tm + 9000));

				addEvent(new ThermostatDay(tm + 10000));

				// 添加一个重新启动对象！
				addEvent(new Restart(tm + 20000));
		}

		public String description() {
				return "Restarting system";
		}
	}

	public static void main(String[] args) {
		GreenhouseControls gc = new GreenhouseControls();
		long tm = System.currentTimeMillis();

		//内部对象只能通过外部类的实例来new.因为内部类的对象需要同创建它的外部类的对象连接到一起。
		gc.addEvent(gc.new Restart(tm)); 
		gc.run();
	}
} ///:~