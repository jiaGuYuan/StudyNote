package cn.com.threadChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public 
class ReceiveMsg implements Runnable {
	private Socket s;
	private BufferedReader br = null;
	private boolean running;

	ReceiveMsg(Socket s) {
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.s = s;
		running = true;
	}

	@Override
	public void run() {
		while (running) {
			String formClient;
			try {
				formClient = br.readLine();
				System.out.println(">>>>> " + formClient);
			} catch (IOException e) {
				System.out.println("�Է��뿪��");
				break;
			}
		}

		if (br != null) {
			try {
				br.close();
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

	public void setRuning(boolean running) {
		this.running = running;
	}
}
