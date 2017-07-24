package com.shop.core.exception;

import com.shop.core.constant.Constant;

@SuppressWarnings("serial")
public class ParamException extends RuntimeException {

	private int resultCode;
	
	public ParamException(String resultMessage) {
		super(resultMessage);
		this.resultCode = Constant.ERROR_CODE;
	}
	
	public ParamException(int resultCode, String resultMessage) {
		super(resultMessage);
		this.resultCode = resultCode;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
}
