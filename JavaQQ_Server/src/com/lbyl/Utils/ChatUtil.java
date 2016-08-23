package com.lbyl.Utils;

import java.io.IOException;
import java.io.OutputStream;
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
	private static ArrayList<ProcessThread> stList = new ArrayList();

	/**
	 * @return the stList
	 */
	public static ArrayList<ProcessThread> getStList() {
		return stList;
	}
	/**
	 * 群发消息
	 * @param xmlMsg
	 * @throws IOException
	 */
	public static void castToAll(String xmlMsg) throws IOException {
		for (int i = 0; i < stList.size(); i++) {
			OutputStream ous = stList.get(i).getOus();
				ous.write(xmlMsg.getBytes());
		}
	}

}
