package com.lbyl.Bean;

/**
 * �û�������
 * 
 * 
 * @author LBYL
 *
 */
public class UserInfo {

	private String name;
	private String password;
	/**
	 * ����ʱ���봫������
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
