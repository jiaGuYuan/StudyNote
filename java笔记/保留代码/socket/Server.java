package cn.com.file_transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Services {
	private static final int SERVER_PORT = 9001; // 指定侦听端口

	public FileTransterServices() {
		ServerSocket ss = null; // 服务器套接字
		Socket s = null; // 通信套接字
		InputStream in = null; // 套接字输入流
		OutputStream out = null;// 套接字输出流
		try {
			ss = new ServerSocket(SERVER_PORT); // 创建服务器套接字
			System.out.println("服务端已启动，正在等待客户端...");
			s = ss.accept(); // 侦听来自客户端的请求
			in = s.getInputStream(); // 获得输入流，用来接收数据
			out = s.getOutputStream(); // 获得输出流，用来发送数据

			byte[] buf = new byte[1024];
			int len = in.read(buf); // 从输入流中读取数据
			String strFromClient = new String(buf, 0, len);
			System.out.println("来自客户端的信息>>" + strFromClient);

			String strToClient = "我也很好！";
			out.write(strToClient.getBytes()); // 往输出流中发送数据

			System.out.println("服务端已关闭。");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if(in != null){ // 关闭输入流
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(out != null) { // 关闭输出流
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
			
			if(s != null){// 关闭通信套接字
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(ss != null){// 关闭通服务器套接字
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}

	public static void main(String[] args) {
		new Services();
	}
}
