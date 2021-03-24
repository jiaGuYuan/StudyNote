package cn.com.threadChat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//演示: 与客户进行聊天(字符流传输)
public class ThreadChatServers {

	private static final int SERVER_PORT = 9001; // 指定侦听端口

	public ThreadChatServers() {
		ServerSocket ss = null; // 服务器套接字
		Socket s = null; // 通信套接字
		PrintWriter pw = null;

		try {
			ss = new ServerSocket(SERVER_PORT); // 创建服务器套接字
			System.out.println("服务端已启动，正在等待客户端...");

			while (true) {
				s = ss.accept(); // 侦听来自客户端的请求

				pw = new PrintWriter(s.getOutputStream(), true); //自动刷新输出流
				String toClient = "欢迎你^.^";
				pw.println(toClient);
				new Thread(new ReceiveMsg(s)).start();
				new Thread(new SendMsg(s)).start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (ss != null) {// 关闭通服务器套接字
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		new ThreadChatServers();
	}

}
