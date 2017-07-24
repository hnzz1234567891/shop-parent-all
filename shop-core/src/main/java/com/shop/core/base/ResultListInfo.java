package com.shop.core.base;

import com.github.miemiedev.mybatis.paginator.domain.Paginator;

import java.util.List;

public class ResultListInfo {
	
	private int resultCode; // 返回码
	private String resultMessage; // 返回消息
	private List result; // 返回内容
	private Paginator paginator;

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
}
