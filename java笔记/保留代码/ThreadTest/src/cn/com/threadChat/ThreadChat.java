package cn.com.threadChat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//演示: 聊天(字符流传输)
public class ThreadChat {

	private static final int SERVER_PORT = 9001;

	public ThreadChat() {
		Socket s = null; // 通信套接字
		try {
			s = new Socket(InetAddress.getByName("localhost"), SERVER_PORT);
			System.out.println("已连接到服务器...");

			new Thread(new ReceiveMsg(s)).start();
			new Thread(new SendMsg(s)).start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//这里不关闭套接字，由线程来关闭
		}
	}

	public static void main(String[] args) {
		new ThreadChat();
	}

}


