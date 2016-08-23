package com.lbyl.Biz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.lbyl.Bean.UserInfo;
import com.lbyl.Constant.CanstantValue_protocol;
import com.lbyl.Dao.ServerDao;
import com.lbyl.Utils.ChatUtil;
import com.lbyl.Utils.LogUtil;

public class ProcessThread extends Thread {

	private Socket client;
	private OutputStream ous;
	private InputStream ins;
	// private BufferedReader bfReader;
	private String readStr;
	private String type;

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
			// ��ʼ��������
			initSream();

			// ѭ���ȴ��Ի�
			while (!interrupted()) {
				readStr = readString();// ��ȡ�����Ϣ
				type = getReqType(readStr);// ��������
				actionType(type);// ��������������Ӧ
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��������������Ӧ
	 * 
	 * @throws IOException
	 */
	private void actionType(String type) throws IOException {

		if (type.equals(CanstantValue_protocol.REGISTER_TYPE)) {// ע������
			doReg();
		} else if (type.equals(CanstantValue_protocol.LOGIN_TYPE)) {// ��¼����
			doLogin();
		} else if (type.equals(CanstantValue_protocol.CHAT_TYPE_CTOS)) {// ��������
			doChat();
		}

	}

	/**
	 * ���졣����Ϣת���������ˡ�
	 */
	private void doChat() {
		try {

			String xmlStr = getXmlStr(CanstantValue_protocol.CHAT_MESSAGE, readStr);
			String[][] s = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.CHAT_TYPE_STOC },
					{ CanstantValue_protocol.CHAT_MESSAGE, xmlStr } };
			String buildStr = buildStr(s);
			ChatUtil.castToAll(buildStr);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��¼�¼� 1.��������Ƿ���ڣ��������¼������������
	 * 
	 * @throws IOException
	 */
	private void doLogin() throws IOException {
		String readName = getXmlStr(CanstantValue_protocol.LOGIN_NAME, readStr);
		if (!ServerDao.hasUser(readName)) {// ���ݿ���û�д�����Ϣ
			LogUtil.log("���ݿ����޴�����Ϣ");
			String logResStr[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.LOGIN_RESPOND_TYPE },
					{ CanstantValue_protocol.LOGIN_RESPOND_VALUE, CanstantValue_protocol.LOGIN_RESPOND_NODATA } };
			sendXmlMsg(logResStr, ous);// ���ͷ���
			return;
		}
		String readPwd = getXmlStr(CanstantValue_protocol.LOGIN_PASSWORD, readStr);
		if (!readPwd.equals(ServerDao.getUser(readName).getPassword())) {// ����������
			String logResStr[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.LOGIN_RESPOND_TYPE }, {
					CanstantValue_protocol.LOGIN_RESPOND_VALUE, CanstantValue_protocol.LOGIN_RESPOND_WRONGPASSWORD } };
			sendXmlMsg(logResStr, ous);// ���ͷ���
			return;
		}
		String logResStr[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.LOGIN_RESPOND_TYPE },
				{ CanstantValue_protocol.LOGIN_RESPOND_VALUE, CanstantValue_protocol.LOGIN_RESPOND_SUCCESS } };
		LogUtil.log("dologin�ɹ�");
		sendXmlMsg(logResStr, ous);// ���ͷ���
	}

	/**
	 * �����ݿ���ע�� 1.������ݿ����Ƿ��д��û���������У����������� 2.���û�У���ʵ�����û��࣬�����ݿ�����ӡ�
	 * 
	 * @throws IOException
	 */
	private void doReg() throws IOException {
		String readName = getXmlStr(CanstantValue_protocol.REGISTER_NAME, readStr);
		if (ServerDao.hasUser(readName)) {
			LogUtil.log("���ݿ����Ѵ��ڴ��û�����");
			String regStr[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.REGISTER_RESPOND_TYPE },
					{ CanstantValue_protocol.REGISTER_RESPOND_VALUE,
							CanstantValue_protocol.REGISTER_RESPOND_EXISTINGDATA } };
			sendXmlMsg(regStr, ous);
			return;
		}
		// ���û�У��򴴽��û��࣬�洢���ݡ�
		String readPwd = getXmlStr(CanstantValue_protocol.REGISTER_PASSWORD, readStr);
		UserInfo user = new UserInfo(readName);
		user.setPassword(readPwd);
		LogUtil.log("ע�����");
		String regRespon[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.REGISTER_RESPOND_TYPE },
				{ CanstantValue_protocol.REGISTER_RESPOND_VALUE, CanstantValue_protocol.REGISTER_RESPOND_SUCCESS } };
		sendXmlMsg(regRespon, ous);
	}

	/**
	 * ���������ж�ȡ��Ϣ
	 * 
	 * @return
	 * @throws IOException
	 */
	private String readString() throws IOException {
		char b;
		StringBuffer sBuffer = new StringBuffer();
		while ((b = (char) ins.read()) != -1) {
			sBuffer.append(b);

			if (sBuffer.toString().endsWith("</msg>")) {
				break;
			}
		}
		String result = new String(sBuffer.toString().getBytes("ISO-8859-1"), "GBK");
		LogUtil.log("��������ȡ����������Ϣ��" + result);
		return result;
	}

	/**
	 * ��ʼ����
	 * 
	 * @throws IOException
	 */
	private void initSream() throws IOException {
		ous = client.getOutputStream();
		ins = client.getInputStream();
		// bfReader = new BufferedReader(new InputStreamReader(ins));
	}

	/**
	 * �Ự���� 0.ֹͣѭ�������������� 1.�رտͻ���socket 2.���Լ����̹߳��������Ƴ� 3.���ٴ��߳�
	 * 
	 * @throws IOException
	 */
	private void endClient() throws IOException {
		LogUtil.log("�˿ͻ��˼�������");
		this.interrupt();// ֹͣѭ������
		client.close();
		ChatUtil.getStList().remove(this);
		this.stop();// ������
	}

	/**
	 * ��Ϣ��װ��
	 * 
	 * @param reqString
	 *            ��Ҫ��װ�Ķ�ά�ַ�������
	 * @return ��װ��ϵ��ַ���
	 */
	public String buildStr(String[][] reqString) {

		StringBuilder sb = new StringBuilder();

		sb.append("<msg>");

		for (int i = 0; i < reqString.length; i++) {
			sb.append("<" + reqString[i][0] + ">" + reqString[i][1] + "</" + reqString[i][0] + ">");
		}
		sb.append("</msg>");

		return sb.toString();
	}

	/**
	 * ��Ϣ������
	 * 
	 * @param reqString
	 *            ��Ҫ��װ�Ķ�ά�ַ����飬���磺{{"name","kibin"},{"age","12"},{"sex","��"}}
	 * @param ous
	 * @throws IOException
	 */
	public void sendXmlMsg(String[][] reqString, OutputStream ous) throws IOException {
		String s = buildStr(reqString);

		ous.write(s.getBytes());

	}

	/**
	 * ��Ϣ�����������ݾ������ͽ���ֵ
	 * 
	 * @param typeStr
	 *            ��ͼ����������
	 * @param ResPonStr
	 *            ��Ҫ�������ַ���
	 * @return
	 */

	public String getXmlStr(String typeStr, String ResPonStr) {

		int startFlag = ResPonStr.indexOf("<" + typeStr + ">");
		int endFlag = ResPonStr.indexOf("</" + typeStr + ">");
		System.out.println(startFlag);
		System.out.println(endFlag);
		String realString = ResPonStr.substring(startFlag + typeStr.length() + 2, endFlag);

		return realString;

	}

	/**
	 * ȡ��xml���ĵ�����
	 * 
	 * @param ReqString
	 * @return
	 */
	public String getReqType(String ReqString) {
		return getXmlStr("type", ReqString);
	}

	/**
	 * ��ȡous
	 * 
	 * @return
	 */
	public OutputStream getOus() {
		return ous;
	}

}
