package com.shop.core.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MemberDto implements Serializable {
	private String username;
	private String password; 
	private String rePassword; 
	private String email; 
	private String name; 
	private Integer gender;
	private String mobile;
	private String phone;
	private String verifyCode;
	private String phoneVerifyCode;
	
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getRePassword() {
		return rePassword;
	}
	public String getEmail() {
		return email;
	}
	public String getName() {
		return name;
	}
	public Integer getGender() {
		return gender;
	}
	public String getMobile() {
		return mobile;
	}
	public String getPhone() {
		return phone;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getPhoneVerifyCode() {
		return phoneVerifyCode;
	}
	public void setPhoneVerifyCode(String phoneVerifyCode) {
		this.phoneVerifyCode = phoneVerifyCode;
	}
}
