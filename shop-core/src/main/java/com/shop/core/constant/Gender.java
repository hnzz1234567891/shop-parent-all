package com.shop.core.constant;

public enum Gender {
	
	male(1, "男"),
	
	female(0, "女");
	
	private int type;
	private String typeString;

	private Gender() {
		
	}
	private Gender(int type, String typeString) {
		this.type = type;
		this.typeString = typeString;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeString() {
		return typeString;
	}
	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}
}
