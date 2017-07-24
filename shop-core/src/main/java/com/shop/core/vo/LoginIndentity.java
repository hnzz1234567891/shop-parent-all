package com.shop.core.vo;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LoginIndentity implements Serializable {
	
	private Integer id;
	private String username;
	private String email;
	private String phone;

	public Integer getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
