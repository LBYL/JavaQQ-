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
	 * 线程主要执行方法
	 */
	@Override
	public void run() {
		super.run();
		
		// 获取输入输出流
		try {
			//初始化流对象
			initSream();
//
//			// 一旦上线，则问候
//			sendMsg_S2C("欢迎上线!");
//			// 尝试登录
//			if (!attempLogin()) {//如果登录验证不成功
//				endClient();
//				this.stop();
//			}
//			sendMsg_S2C("登录成功！");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/**
	 * 初始化
	 * @throws IOException 
	 */
	private void initSream() throws IOException {
		ous = client.getOutputStream();
		ins = client.getInputStream();
		bfReader = new BufferedReader(new InputStreamReader(ins));
	}

//	/**
//	 * 尝试登录
//	 * 
//	 * @throws IOException
//	 */
//	private Boolean attempLogin() throws IOException {
//		sendMsg_S2C("请输入用户名:");
//		String inputName =  bfReader.readLine();
//
//		// 如果用户名不存在，提示重新输入
//		while (!UserDao.UserDB.containsKey(inputName)) {
//			sendMsg_S2C("用户不存在!请重新输入用户名：");
//			inputName =  bfReader.readLine();
//		}
//		// 用户存在则询问密码
//		sendMsg_S2C("请输入密码:");
//		String inputPsd = bfReader.readLine();
//
//		// 如果密码不对，则重新输入密码
//		while (!inputPsd.equals(UserDao.UserDB.get(inputName).getPassword())) {
//			sendMsg_S2C("密码错误，请重新输入密码：");
//			inputPsd = bfReader.readLine();
//		}
//		// 密码正确，则返回验证成功
//		return true;
//
//	}

	/**
	 * 会话结束
	 * 
	 * @throws IOException
	 */
	private void endClient() throws IOException {
		client.close();
	}

	/**
	 * 消息发送——服务器->客户端
	 * 
	 * @param sth
	 * @throws IOException
	 */
	public void sendMsg_S2C(String sth) throws IOException {
		String content = sth + "\r\n";
		ous.write(content.getBytes());
	}

}
