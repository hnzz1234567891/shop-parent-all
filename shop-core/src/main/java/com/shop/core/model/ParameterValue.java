package com.shop.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParameterValue implements Serializable {

	private static final long serialVersionUID = 6528715812286491634L;

	/** 参数组 */
    private String group;

	/** 条目 */
	private List<Entry> entries = new ArrayList<Entry>();

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
}
