package com.lbyl.Utils;

import java.io.IOException;
import java.util.ArrayList;

import com.lbyl.Biz.ProcessThread;

/**
 * 聊天工具类，只需调用
 * 
 * @author LBYL
 *
 */
public class ChatUtil {

	// 无需外部构造
	private ChatUtil() {
	}

	// 线程队列维护
	private static ArrayList<Thread> stList = new ArrayList();
	private static ProcessThread stThread;

	/**
	 * @return the stList
	 */
	public static ArrayList<Thread> getStList() {
		return stList;
	}

	/**
	 * @param stList
	 *            the stList to set
	 */
	public static void setStList(ArrayList<Thread> stList) {
		ChatUtil.stList = stList;
	}

	/**
	 * 群发方法
	 * 
	 * @throws IOException
	 */
	public static void sendMsg2All(String Content) throws IOException {
		for (int i = 0; i < stList.size(); i++) {
			stThread = (ProcessThread) stList.get(i);
			stThread.sendMsg_S2C(Content);
		}
	}

}
