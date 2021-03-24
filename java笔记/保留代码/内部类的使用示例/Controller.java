//: Controller.java
// 控制框架：

package c07.controller;

// 这只是一个容纳Event对象的方法。
class EventSet {
	private Event[] events = new Event[100];
	private int index = 0;
	private int next = 0;

	public void add(Event e) {
		if(index >= events.length)
			return; // (在现实中，抛出异常)
		events[index++] = e;
	}

	public Event getNext() {

		boolean looped = false;
		int start = next;
		do {
			next = (next + 1) % events.length;

			// 判断它是否已经循环到开始位置：
			if(start == next) looped = true;

			// 判断列表是否为空
			if((next == (start + 1) % events.length) && looped)
				return null;
		} while(events[next] == null);
		return events[next];
	}

	public void removeCurrent() {
		events[next] = null;
	}
}


//事件控制器，负责管理事件
public class Controller {
	private EventSet es = new EventSet();
	
	public void addEvent(Event c) { es.add(c); }
	
	public void run() {
		Event e;
		while((e = es.getNext()) != null) { //循环检查事件列表中的事件，并处理就绪的事件
			if(e.ready()) {
				e.action();
				System.out.println(e.description());
				es.removeCurrent();
			}
		}
	}
} ///:~