package cn.com.server.servlet.http;

import java.io.IOException;

import cn.com.server.servlet.ServletException;

public abstract class HttpServlet {
	//���û���get��ʽ����ʱ��ҵ���߼�����
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		
	}
	
	//���û���post��ʽ����ʱ��ҵ���߼�����
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		
	}
	
	//��ʼ������
	protected void init() {
		
	}
	//����ǰִ�е����ķ���
	protected void destory() {
		
	}
	
	//��������ķ�ʽ�ĺ��ĵ��ȷ���(�������������������д�÷���)
	protected void service(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		if(request.getMethod().equalsIgnoreCase("get")) {
			doGet(request,response);
		}else if(request.getMethod().equalsIgnoreCase("post")) {
			doPost(request,response);
		}
	}
}
