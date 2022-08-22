package com.wch.study.spring.entity;

/**
 * @author ch w
 * @version 1.0
 * @since 2022/8/18 15:26
 */
public class User {
	public User() {
	}

	public User(String name, String address) {
		this.name = name;
		this.address = address;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", address='" + address + '\'' +
				'}';
	}

	private String name;
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
