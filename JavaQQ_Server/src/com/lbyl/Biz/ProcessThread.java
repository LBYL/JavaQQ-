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
	 * 线程主要执行方法
	 */
	@Override
	public void run() {
		super.run();

		// 获取输入输出流
		try {
			// 初始化流对象
			initSream();

			// 循环等待对话
			while (!interrupted()) {
				readStr = readString();// 读取完成消息
				type = getReqType(readStr);// 解析类型
				actionType(type);// 根据类型做出反应
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 根据类型做出反应
	 * 
	 * @throws IOException
	 */
	private void actionType(String type) throws IOException {

		if (type.equals(CanstantValue_protocol.REGISTER_TYPE)) {// 注册请求
			doReg();
		} else if (type.equals(CanstantValue_protocol.LOGIN_TYPE)) {// 登录请求
			doLogin();
		} else if (type.equals(CanstantValue_protocol.CHAT_TYPE_CTOS)) {// 聊天请求
			doChat();
		}

	}

	/**
	 * 聊天。将消息转发给所有人。
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
	 * 登录事件 1.检查数据是否存在，存在则登录，不存在则反馈
	 * 
	 * @throws IOException
	 */
	private void doLogin() throws IOException {
		String readName = getXmlStr(CanstantValue_protocol.LOGIN_NAME, readStr);
		if (!ServerDao.hasUser(readName)) {// 数据库中没有此人信息
			LogUtil.log("数据库中无此人信息");
			String logResStr[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.LOGIN_RESPOND_TYPE },
					{ CanstantValue_protocol.LOGIN_RESPOND_VALUE, CanstantValue_protocol.LOGIN_RESPOND_NODATA } };
			sendXmlMsg(logResStr, ous);// 发送反馈
			return;
		}
		String readPwd = getXmlStr(CanstantValue_protocol.LOGIN_PASSWORD, readStr);
		if (!readPwd.equals(ServerDao.getUser(readName).getPassword())) {// 如果密码错误
			String logResStr[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.LOGIN_RESPOND_TYPE }, {
					CanstantValue_protocol.LOGIN_RESPOND_VALUE, CanstantValue_protocol.LOGIN_RESPOND_WRONGPASSWORD } };
			sendXmlMsg(logResStr, ous);// 发送反馈
			return;
		}
		String logResStr[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.LOGIN_RESPOND_TYPE },
				{ CanstantValue_protocol.LOGIN_RESPOND_VALUE, CanstantValue_protocol.LOGIN_RESPOND_SUCCESS } };
		LogUtil.log("dologin成功");
		sendXmlMsg(logResStr, ous);// 发送反馈
	}

	/**
	 * 在数据库中注册 1.检查数据库中是否有此用户名，如果有，则做出反馈 2.如果没有，则实例化用户类，在数据库中添加。
	 * 
	 * @throws IOException
	 */
	private void doReg() throws IOException {
		String readName = getXmlStr(CanstantValue_protocol.REGISTER_NAME, readStr);
		if (ServerDao.hasUser(readName)) {
			LogUtil.log("数据库中已存在此用户名。");
			String regStr[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.REGISTER_RESPOND_TYPE },
					{ CanstantValue_protocol.REGISTER_RESPOND_VALUE,
							CanstantValue_protocol.REGISTER_RESPOND_EXISTINGDATA } };
			sendXmlMsg(regStr, ous);
			return;
		}
		// 如果没有，则创建用户类，存储数据。
		String readPwd = getXmlStr(CanstantValue_protocol.REGISTER_PASSWORD, readStr);
		UserInfo user = new UserInfo(readName);
		user.setPassword(readPwd);
		LogUtil.log("注册完毕");
		String regRespon[][] = { { CanstantValue_protocol.TYPE, CanstantValue_protocol.REGISTER_RESPOND_TYPE },
				{ CanstantValue_protocol.REGISTER_RESPOND_VALUE, CanstantValue_protocol.REGISTER_RESPOND_SUCCESS } };
		sendXmlMsg(regRespon, ous);
	}

	/**
	 * 从输入流中读取信息
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
		LogUtil.log("服务器获取到的完整信息是" + result);
		return result;
	}

	/**
	 * 初始化流
	 * 
	 * @throws IOException
	 */
	private void initSream() throws IOException {
		ous = client.getOutputStream();
		ins = client.getInputStream();
		// bfReader = new BufferedReader(new InputStreamReader(ins));
	}

	/**
	 * 会话结束 0.停止循环阻塞接收内容 1.关闭客户单socket 2.把自己从线程管理类中移除 3.销毁此线程
	 * 
	 * @throws IOException
	 */
	private void endClient() throws IOException {
		LogUtil.log("此客户端即将结束");
		this.interrupt();// 停止循环接收
		client.close();
		ChatUtil.getStList().remove(this);
		this.stop();// 待改善
	}

	/**
	 * 消息组装器
	 * 
	 * @param reqString
	 *            将要组装的二维字符串数组
	 * @return 组装完毕的字符串
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
	 * 消息发送器
	 * 
	 * @param reqString
	 *            将要组装的二维字符数组，例如：{{"name","kibin"},{"age","12"},{"sex","男"}}
	 * @param ous
	 * @throws IOException
	 */
	public void sendXmlMsg(String[][] reqString, OutputStream ous) throws IOException {
		String s = buildStr(reqString);

		ous.write(s.getBytes());

	}

	/**
	 * 消息解析器，根据具体类型解析值
	 * 
	 * @param typeStr
	 *            试图解析的类型
	 * @param ResPonStr
	 *            需要解析的字符串
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
	 * 取得xml语句的的类型
	 * 
	 * @param ReqString
	 * @return
	 */
	public String getReqType(String ReqString) {
		return getXmlStr("type", ReqString);
	}

	/**
	 * 获取ous
	 * 
	 * @return
	 */
	public OutputStream getOus() {
		return ous;
	}

}
