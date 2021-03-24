package cn.com.server.servlet.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import cn.com.server.servlet.ServletException;

public class SessionThread extends Thread {
	private Socket s;
	private BufferedReader br;
	private PrintWriter out;
	private BufferedReader brRead;
	private Map<String,String> config;
	public SessionThread(Socket s,Map<String,String> config) {
		this.s = s;
		this.config = config;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		HttpServletResponse response = null;
		HttpServletRequest request = null;
		try {
			request = new HttpServletRequestImpl(br);
			
			if(request.getHead().split(" ").length != 3) {
				return;
			}
			if(!(request.getMethod().equalsIgnoreCase("get") || request.getMethod().equalsIgnoreCase("post"))) {
				return;
			}
			if(!request.getRequestURI().startsWith("/")) {
				return;
			}
			if(!(request.getProtocol().equalsIgnoreCase("http/1.0") || request.getProtocol().equalsIgnoreCase("http/1.1"))) {
				return;
			}
			File f = new File(request.getRequestURI().substring(1));
			String fileName = f.getName().toLowerCase();
			if(fileName.endsWith(".html") || fileName.endsWith(".htm")) {
				if(f.exists()) {
					response = new HttpServletResponseImpl(out, "200");
					brRead = new BufferedReader(new FileReader(f));
					String data = null;
					while((data = brRead.readLine()) != null) {
						out.println(data);
					}
				}else {
					response = new HttpServletResponseImpl(out, "404");
				}
			}else if(fileName.endsWith(".jsp")) {
				
			}else {
				String classPath = config.get(request.getRequestURI());
				HttpServlet servlet = null;
				if(classPath == null) {
					response = new HttpServletResponseImpl(out, "404");
					
				}else {
					
					try {
						Class c = Class.forName(classPath);
						servlet = (HttpServlet)c.newInstance();
						response = new HttpServletResponseImpl(out, "200");
						servlet.init();
						servlet.service(request, response);
						servlet.destory();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ServletException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}catch(IOException ex) {
			ex.printStackTrace();
			response = new HttpServletResponseImpl(out, "500");
			out.println(response);
		} finally {
			if(brRead != null) {
				try {
					brRead.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(out != null) {
				out.close();
			}
			
			if(s != null) {
				try {
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
	}
}
