package com.move.service;

import com.move.model.DataOriginal;
import com.move.model.DataResult;
import com.move.model.UserData;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.UserInfo;

import java.util.List;
import java.util.Map;

public interface DataService  {
	public List<Map<String, Object>> resultMapList(QueryBuilder qb);
	public List<Map<String, Object>> originalMapList(QueryBuilder qb);
	public Datagrid DataGrid(QueryBuilder qb);
	public Integer setAverageData(String month, Integer fileId, UserInfo userInfo);
	public Map<String, Object> originalMap(QueryBuilder qb);
	public Datagrid sysFlieGrid(QueryBuilder qb);
	public DataResult getDataResult(QueryBuilder qb);
	public List<DataResult> resultList(QueryBuilder qb);
	public Datagrid msgHistoryDatagrid(QueryBuilder qb);
	public DataOriginal originalUpdate(Integer id, Integer value1, Integer value2, Integer value3, Integer value4, Integer value5,
			Integer value6, UserInfo userInfo);
	public Datagrid DataResultGrid(QueryBuilder qb);
}
