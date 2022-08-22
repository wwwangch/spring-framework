package com.wch.study.spring.entity;

import java.util.Objects;

/**
 * @author ch w
 * @version 1.0
 * @since 2022/8/18 15:26
 */
public class User {
	private Integer id;
	private String address;

	private String name;

	public void init() {
		System.out.println("init方法");
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", address='" + address + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
