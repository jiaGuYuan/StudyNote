package com.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import java.util.Properties;

public class Config {
	
	public static Map<String,ConfigBean> getConfig() {
		Map<String,ConfigBean> map = new HashMap<String, ConfigBean>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File("config.properties")));
			String data = null;
			ConfigBean cb = null;
			while((data = br.readLine()) != null) {
				cb = new ConfigBean();
				String[] temp = data.split("=");
				if(temp[1].indexOf(";") != -1) {
					String[] paths = temp[1].split(";");
					cb.setBeanPath(paths[0]);
					cb.setActionPath(paths[1]);
				}else {
					cb.setActionPath(temp[1]);
				}
				map.put(temp[0], cb);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return map;
	}
	
	public static Map<String,Object> getViewData(Object o) {
		Map<String,Object> map = new HashMap<String, Object>();
		Class c = o.getClass();
		try {
			//c = Class.forName(viewPath);
//			Object o = c.newInstance();
			Field[] fs = c.getDeclaredFields();
			for(Field f : fs) {
				f.setAccessible(true);
				Class type = f.getType();
				if(type == JTextField.class || type == JPasswordField.class || type == JTextArea.class) {
					JTextComponent tc = (JTextComponent)f.get(o);
					map.put(f.getName(), tc.getText().trim());
				}else if(type == JComboBox.class) {
					JComboBox cmb = (JComboBox)f.get(o);
					map.put(f.getName(), cmb.getSelectedItem());
				}else if(type == JRadioButton.class) {
					JRadioButton rab = (JRadioButton)f.get(o);
					if(rab.isSelected()) {
						map.put(f.getName(), rab.getText());
					}
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}
