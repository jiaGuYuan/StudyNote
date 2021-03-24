package cn.com.server.servlet.http;

import java.io.IOException;

import cn.com.server.servlet.ServletException;

public abstract class HttpServlet {
	//当用户以get方式请求时的业务逻辑方法
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		
	}
	
	//当用户以post方式请求时的业务逻辑方法
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		
	}
	
	//初始化方法
	protected void init() {
		
	}
	//销毁前执行的最后的方法
	protected void destory() {
		
	}
	
	//对于请求的方式的核心调度方法(如非有特殊需求，请勿重写该方法)
	protected void service(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		if(request.getMethod().equalsIgnoreCase("get")) {
			doGet(request,response);
		}else if(request.getMethod().equalsIgnoreCase("post")) {
			doPost(request,response);
		}
	}
}
