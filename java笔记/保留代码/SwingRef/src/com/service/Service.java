package com.service;

import java.awt.AWTEvent;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

public class Service {
	private String url;
	private Object o;
	private AWTEvent e;
	private static Map<String,ConfigBean> config;
	
	static {
		config = Config.getConfig();
	}
	public Service(String url,Object o,AWTEvent e) {
		this.url = url;
		this.o = o;
		this.e = e;
		service();
	}
	
	public void service() {
		String actionPath = config.get(url).getActionPath();
		Class c = null;
		Action action = null;
		try {
			c = Class.forName(actionPath);
			action = (Action)c.newInstance();
			//≈–∂œ «∑Ò”–bean
			action.execute(getData(o),o, e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ActionForm getData(Object o) {
		String beanPath = config.get(url).getBeanPath();
		ActionForm obj = null;
		Class c = null;
		Map<String,Object> map = Config.getViewData(o); 
		try {
			c = Class.forName(beanPath);
			obj = (ActionForm)c.newInstance();
			Field[] fs = c.getDeclaredFields();
			for(Field f : fs) {
				f.setAccessible(true);
				Object val = map.get(f.getName());
				if(val == null) {
					Set<String> keys = map.keySet();
					for(String k : keys) {
						if(k.startsWith(f.getName())) {
							val = map.get(k);
						}
					}
				}
				if(val != null) {
					Class type = f.getType();
					if(type == int.class) {
						f.set(obj, new Integer(val.toString()));
					}else if(type == float.class) {
						f.set(obj, new Float(val.toString()));
					}else if(type == double.class) {
						f.set(obj, new Double(val.toString()));
					}else if(type == long.class) {
						f.set(obj, new Long(val.toString()));
					}else if(type == short.class) {
						f.set(obj, new Short(val.toString()));
					}else if(type == boolean.class) {
						f.set(obj, new Boolean(val.toString()));
					}else if(type == char.class) {
						f.set(obj, new Character(val.toString().charAt(0)));
					}else if(type == String.class) {
						f.set(obj, val.toString());
					}else {
						f.set(obj, val);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
}
