package com.lbyl.Biz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ProcessThread extends Thread {

	private Socket client;
	private OutputStream ous;
	private InputStream ins;
	private BufferedReader bfReader;

	public ProcessThread(Socket client) {
		this.client = client;
	}

	/**
	 * �߳���Ҫִ�з���
	 */
	@Override
	public void run() {
		super.run();
		
		// ��ȡ���������
		try {
			//��ʼ��������
			initSream();
//
//			// һ�����ߣ����ʺ�
//			sendMsg_S2C("��ӭ����!");
//			// ���Ե�¼
//			if (!attempLogin()) {//�����¼��֤���ɹ�
//				endClient();
//				this.stop();
//			}
//			sendMsg_S2C("��¼�ɹ���");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/**
	 * ��ʼ��
	 * @throws IOException 
	 */
	private void initSream() throws IOException {
		ous = client.getOutputStream();
		ins = client.getInputStream();
		bfReader = new BufferedReader(new InputStreamReader(ins));
	}

//	/**
//	 * ���Ե�¼
//	 * 
//	 * @throws IOException
//	 */
//	private Boolean attempLogin() throws IOException {
//		sendMsg_S2C("�������û���:");
//		String inputName =  bfReader.readLine();
//
//		// ����û��������ڣ���ʾ��������
//		while (!UserDao.UserDB.containsKey(inputName)) {
//			sendMsg_S2C("�û�������!�����������û�����");
//			inputName =  bfReader.readLine();
//		}
//		// �û�������ѯ������
//		sendMsg_S2C("����������:");
//		String inputPsd = bfReader.readLine();
//
//		// ������벻�ԣ���������������
//		while (!inputPsd.equals(UserDao.UserDB.get(inputName).getPassword())) {
//			sendMsg_S2C("��������������������룺");
//			inputPsd = bfReader.readLine();
//		}
//		// ������ȷ���򷵻���֤�ɹ�
//		return true;
//
//	}

	/**
	 * �Ự����
	 * 
	 * @throws IOException
	 */
	private void endClient() throws IOException {
		client.close();
	}

	/**
	 * ��Ϣ���͡���������->�ͻ���
	 * 
	 * @param sth
	 * @throws IOException
	 */
	public void sendMsg_S2C(String sth) throws IOException {
		String content = sth + "\r\n";
		ous.write(content.getBytes());
	}

}
