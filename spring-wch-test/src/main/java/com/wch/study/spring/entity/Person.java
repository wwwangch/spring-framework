package com.wch.study.spring.entity;

/**
 * @author ch w
 * @version 1.0
 * @since 2022/8/18 15:27
 */
public class Person {
	private Integer id;

	private String name;

	private String address;

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

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

	@Override
	public String toString() {
		return "Person{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}
