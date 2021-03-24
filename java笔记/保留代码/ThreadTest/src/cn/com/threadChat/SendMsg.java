package cn.com.threadChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SendMsg implements Runnable {
	private Socket s;
	private PrintWriter pw = null;
	private BufferedReader inReader = null; // ��ȡ����̨����
	private boolean running;
	
	SendMsg(Socket s) {
		try {
			pw = new PrintWriter(s.getOutputStream(), true); // �Զ�ˢ�������
			inReader = new BufferedReader(new InputStreamReader(System.in));
			running = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.s = s;
	}

	@Override
	public void run() {
		while (running) {
			String toServer = null;
			try {
				toServer = inReader.readLine();
				pw = new PrintWriter(s.getOutputStream(), true); // �Զ�ˢ�������
				pw.println(toServer);
			} catch (IOException e) {
				System.out.println("�Է��뿪��2");
				e.printStackTrace();
				break;
			}
		}
		
		if (pw != null) {
			pw.close();
		}

		if (inReader != null) {
			try {
				inReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (this.s != null) {// �ر�ͨ���׽���
			try {
				this.s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
