package cn.com.threadChat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//��ʾ: ����(�ַ�������)
public class ThreadChat {

	private static final int SERVER_PORT = 9001;

	public ThreadChat() {
		Socket s = null; // ͨ���׽���
		try {
			s = new Socket(InetAddress.getByName("localhost"), SERVER_PORT);
			System.out.println("�����ӵ�������...");

			new Thread(new ReceiveMsg(s)).start();
			new Thread(new SendMsg(s)).start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//���ﲻ�ر��׽��֣����߳����ر�
		}
	}

	public static void main(String[] args) {
		new ThreadChat();
	}

}


