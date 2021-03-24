package cn.com.server.servlet.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

class LoadConfig {
	private LoadConfig() {
		
	}
	
	public static Map<String,String> getConfig() {
		Map<String,String> map = new HashMap<String, String>();
		SAXBuilder buil = new SAXBuilder();
		try {
			Document doc = buil.build(new FileReader(new File("WEB-INF/web.xml")));
			Element root = doc.getRootElement();
			List<Element> servlets = root.getChildren("servlet", root.getNamespace());
			List<Element> servletMappings = root.getChildren("servlet-mapping", root.getNamespace());
			for(Element s : servlets) {
				String sn = s.getChildText("servlet-name", root.getNamespace());
				String sc = s.getChildText("servlet-class",root.getNamespace());
				for(Element ss : servletMappings) {
					String ssn = ss.getChildText("servlet-name", root.getNamespace());
					String up = ss.getChildText("url-pattern",root.getNamespace());
					if(ssn.equals(sn)) {
						map.put(up, sc);
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
}
