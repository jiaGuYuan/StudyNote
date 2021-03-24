package cn.com.server.servlet.http;

public interface HttpServletRequest {
	String getRequestURI();
	String getMethod();
	String getProtocol();
	String getHead();
}
