package cn.com.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import cn.com.server.servlet.ServletException;
import cn.com.server.servlet.http.HttpServlet;
import cn.com.server.servlet.http.HttpServletRequest;
import cn.com.server.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {
	public void init() {
		System.out.println("我初始化了");
	}
	
	public void destory() {
		System.out.println("我要被销毁了");
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		System.out.println("我准备做业务逻辑了");
		PrintWriter out = response.getWrite();
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Hello</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Hello world</h1>");
		out.println("</body>");
		out.println("</html>");
	}
}
