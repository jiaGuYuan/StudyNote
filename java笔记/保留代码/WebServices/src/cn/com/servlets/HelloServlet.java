package cn.com.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import cn.com.server.servlet.ServletException;
import cn.com.server.servlet.http.HttpServlet;
import cn.com.server.servlet.http.HttpServletRequest;
import cn.com.server.servlet.http.HttpServletResponse;

public class HelloServlet extends HttpServlet {
	public void init() {
		System.out.println("�ҳ�ʼ����");
	}
	
	public void destory() {
		System.out.println("��Ҫ��������");
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		System.out.println("��׼����ҵ���߼���");
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
