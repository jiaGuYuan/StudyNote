package cn.com.server.servlet.http;

import java.io.PrintWriter;

class HttpServletResponseImpl implements HttpServletResponse {
	
	private PrintWriter out;
	
	public HttpServletResponseImpl(PrintWriter out,String stat) {
		this.out = out;
		out.println("HTTP/1.0 "+stat+" OK\r\nCONTENT-TYPE: text/html; charset=GBK\r\n\r\n");
	}
	
	@Override
	public PrintWriter getWrite() {
		// TODO Auto-generated method stub
		return this.out;
	}

}
