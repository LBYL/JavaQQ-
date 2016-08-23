package com.lbyl.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.lbyl.Biz.ProcessThread;

/**
 * ���칤���ֻ࣬�����
 * 
 * @author LBYL
 *
 */
public class ChatUtil {

	// �����ⲿ����
	private ChatUtil() {
	}

	// �̶߳���ά��
	private static ArrayList<ProcessThread> stList = new ArrayList();

	/**
	 * @return the stList
	 */
	public static ArrayList<ProcessThread> getStList() {
		return stList;
	}
	/**
	 * Ⱥ����Ϣ
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
