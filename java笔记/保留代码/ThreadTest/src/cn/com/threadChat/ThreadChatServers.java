package cn.com.threadChat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//��ʾ: ��ͻ���������(�ַ�������)
public class ThreadChatServers {

	private static final int SERVER_PORT = 9001; // ָ�������˿�

	public ThreadChatServers() {
		ServerSocket ss = null; // �������׽���
		Socket s = null; // ͨ���׽���
		PrintWriter pw = null;

		try {
			ss = new ServerSocket(SERVER_PORT); // �����������׽���
			System.out.println("����������������ڵȴ��ͻ���...");

			while (true) {
				s = ss.accept(); // �������Կͻ��˵�����

				pw = new PrintWriter(s.getOutputStream(), true); //�Զ�ˢ�������
				String toClient = "��ӭ��^.^";
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
			if (ss != null) {// �ر�ͨ�������׽���
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
