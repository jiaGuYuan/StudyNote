package cn.com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import cn.com.beans.StuInfoBean;
import cn.com.daos.StuInfoDAOImpl;
import cn.com.server.servlet.ServletException;
import cn.com.server.servlet.http.HttpServlet;
import cn.com.server.servlet.http.HttpServletRequest;
import cn.com.server.servlet.http.HttpServletResponse;

public class ListServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
		
		PrintWriter out = response.getWrite();
		List<StuInfoBean> list = new StuInfoDAOImpl().getAllStuInfo("");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Hello</title>");
		out.println("</head>");
		out.println("<body>");
		
		out.println("<table border='1'>");
		out.println("<tr>");
		out.println("<th>学号</th>");
		out.println("<th>姓名</th>");
		out.println("<th>入学时间</th>");
		out.println("<th>年龄</th>");
		out.println("</tr>");
		for(StuInfoBean s : list) {
			out.println("<tr>");
			out.println("<td>"+s.getStuNumber()+"</td>");
			out.println("<td>"+s.getStuName()+"</td>");
			out.println("<td>"+s.getStuJoinTime()+"</td>");
			out.println("<td>"+s.getStuAge()+"</td>");
			out.println("</tr>");
		}
		out.println("</table>");
		
		out.println("</body>");
		out.println("</html>");
	}
}
