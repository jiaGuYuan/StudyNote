//: Event.java
// 自定义控制事件的公用方法

package c07.controller;

//指定一个与时间有关的事件
abstract public class Event {
	private long evtTime;
	public Event(long eventTime) {
		evtTime = eventTime;
	}
	public boolean ready() {
		return System.currentTimeMillis() >= evtTime;
	}
	abstract public void action();
	abstract public String description();
} ///:~