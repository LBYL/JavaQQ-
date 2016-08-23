package com.lbyl.Biz;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.lbyl.Utils.ChatUtil;
import com.lbyl.Utils.LogUtil;

/**
 * ����ʵ�������ҹ��� �������ࣺ ������������ѭ�����ܿͻ���
 * 
 * @author LBYL
 *
 */
public class ChatServer {

	private static boolean runningState;
	private int port;
	private ServerSocket sc;
	private MainThread mThread;
	private Socket client;

	// ����ʱ����˿ں�
	public ChatServer(int port) {
		this.port = port;
		runningState = false;
		LogUtil.log("chatServer�Ĺ��췽��");
	}

	/**
	 * @param �ⲿ���ÿ���״̬
	 */
	public void setRunningState(boolean runningState) {
		LogUtil.log("���������÷������ر�");
		ChatServer.runningState = runningState;
	}

	/**
	 * ����������
	 */
	public void setUpServer() {
		try {
			// ����������

			if (runningState) {// ����Ѿ����ˣ��Ͳ���Ӧ
				return;
			}
			sc = new ServerSocket(port);
			runningState = true;
			System.out.println("------�����������ɹ���-------");

			mThread = new MainThread();
			mThread.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ���ط���������״̬
	 * 
	 * @return
	 */
	public static boolean getRunningState() {
		return runningState;
	}

	/**
	 * �رշ�����
	 * 
	 * @throws IOException
	 */
	public void closeServer() throws IOException {
		mThread.interrupt();
		sc.close();
		System.out.println("----------�������ѹرգ�---------");
	}

	/**
	 * ѭ�����տͻ����߳�
	 * 
	 * @author LBYL
	 *
	 */
	class MainThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				// �������տͻ���

				try {
					client = sc.accept();
					System.out.println("�������յ��µ�socket��");
					// �����߳�
					ProcessThread st = new ProcessThread(client);
					// ά����������
					ChatUtil.getStList().add(st);
					System.out.println("-------������" + ChatUtil.getStList().size() + "��ProcessThread--------");

					st.start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("ֹͣ���տͻ���");
				}
			}
		}
	}
}
