package cn.com.server.servlet.http;

import java.io.BufferedReader;
import java.io.IOException;

class HttpServletRequestImpl implements HttpServletRequest {
	
	private BufferedReader br;
	private String head;
	
	public HttpServletRequestImpl(BufferedReader br) {
		this.br = br;
		try {
			this.head = br.readLine();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String getRequestURI() {
		// TODO Auto-generated method stub
		return this.head.split(" ")[1];
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return this.head.split(" ")[0];
	}

	@Override
	public String getProtocol() {
		// TODO Auto-generated method stub
		return this.head.split(" ")[2];
	}

	@Override
	public String getHead() {
		// TODO Auto-generated method stub
		return head;
	}
	
}
