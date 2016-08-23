package com.lbyl.Dao;

import java.util.HashMap;
import java.util.Map;

import com.lbyl.Bean.UserInfo;

/**
 * 用户数据库类
 * 
 * @author LBYL
 *
 */
public final class ServerDao {

	public static Map<String, UserInfo> UserDB = new HashMap();

	// 静态块模拟用户数据
	static {

		for (int i = 0; i < 10; i++) {

			UserInfo user = new UserInfo("user" + i);
			user.setPassword(String.valueOf(i));
			UserDB.put(user.getName(), user);
		}
	}
	/**
	 * 查看数据库中是否存在此用户
	 * @param name
	 * @return
	 */
	public static boolean hasUser(String name){
		return UserDB.containsKey(name);
	}
	/**
	 * 存储数据
	 * @param userName
	 * @param uif
	 */
	public static void putData(String userName,UserInfo uif){
		UserDB.put(userName, uif);
	}
	/**
	 * 获取用户数据
	 * @param name
	 * @return
	 */
	public static UserInfo getUser(String name) {
		return UserDB.get(name);
	}

}
