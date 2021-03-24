package cn.com.file_transfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private static final int SERVER_PORT = 9001; // 服务器的侦听端口

	public Client() {
		Socket s = null;
		InputStream in = null;
		OutputStream out = null;
		try { // 由于服务端也是运行在本机，所以创建本机的InetAddress对象
			InetAddress address = InetAddress.getByName("localhost");
			s = new Socket(address, SERVER_PORT); // 向服务器侦听端口发出请求
			System.out.println("客户端已启动。");
			in = s.getInputStream(); // 获得输入流，用来接收数据
			out = s.getOutputStream(); // 获得输出流，用来发送数据
			
			//发送数据
			String strToServer = "你好！";
			out.write(strToServer.getBytes()); // 往输出流中发送数据
			
			//接收数据
			byte[] buf = new byte[1024];
			int len = in.read(buf); // 从输入流中读取数据
			String strFromServer = new String(buf, 0, len);
			System.out.print("来自服务端的回答>>");
			System.out.println(strFromServer);

			System.out.println("客户端已关闭。");
		} catch (UnknownHostException nhe) {
			System.out.println("未找到指定主机...");
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
			
		}
	}

	public static void main(String[] args) {
		new Client();
	}
}
