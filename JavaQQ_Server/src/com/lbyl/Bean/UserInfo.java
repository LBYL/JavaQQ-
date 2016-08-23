package com.lbyl.Bean;

/**
 * 用户特征类
 * 
 * 
 * @author LBYL
 *
 */
public class UserInfo {

	private String name;
	private String password;
	/**
	 * 创建时必须传入姓名
	 * @param name
	 */
	public UserInfo(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
