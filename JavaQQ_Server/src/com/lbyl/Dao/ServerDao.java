package com.lbyl.Dao;

import java.util.HashMap;
import java.util.Map;

import com.lbyl.Bean.UserInfo;

/**
 * �û����ݿ���
 * 
 * @author LBYL
 *
 */
public final class ServerDao {

	public static Map<String, UserInfo> UserDB = new HashMap();

	// ��̬��ģ���û�����
	static {

		for (int i = 0; i < 10; i++) {

			UserInfo user = new UserInfo("user" + i);
			user.setPassword(String.valueOf(i));
			UserDB.put(user.getName(), user);
		}
	}
	/**
	 * �鿴���ݿ����Ƿ���ڴ��û�
	 * @param name
	 * @return
	 */
	public static boolean hasUser(String name){
		return UserDB.containsKey(name);
	}
	/**
	 * �洢����
	 * @param userName
	 * @param uif
	 */
	public static void putData(String userName,UserInfo uif){
		UserDB.put(userName, uif);
	}
	/**
	 * ��ȡ�û�����
	 * @param name
	 * @return
	 */
	public static UserInfo getUser(String name) {
		return UserDB.get(name);
	}

}
