package com.move.utils;

import java.util.List;
import java.util.Map;

public class Datagrid {
	private List<Map<String, Object>> rows;

	private Integer count;

	public List<Map<String, Object>> getRows() {
		return rows;
	}

	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
}
