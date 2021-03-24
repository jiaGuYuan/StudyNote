package cn.com.server.servlet.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public class WebService {
	private ServerSocket ss;
	private Socket s;
	private static Map<String,String> config;
	
	static {
		config = LoadConfig.getConfig();
	}
	
	public WebService() {
		try {
			ss = new ServerSocket(9001);
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void init() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				s = ss.accept();
				new SessionThread(s,config).start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
