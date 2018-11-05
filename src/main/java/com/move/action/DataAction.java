package com.move.action;

import com.move.service.DataService;
import com.move.service.OrgService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="data")
public class DataAction {


    @Autowired
    private DataService dataService;

    @GetMapping(value = "originalMap")
    public Object originalMap(String openId,String month,Integer fileId,UserInfo userInfo){
    	QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.month","month");
		QueryUtils.addColumn(qb, "t.value1","value1");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhereIfNotNull(qb, "and t.openId = {0}", openId);
		QueryUtils.addWhereIfNotNull(qb, "and t.fileId = {0}", fileId);
		QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
        return dataService.originalMapList(qb);
    }

	@GetMapping(value = "dataDataGrid")
	public Object dataDataGrid(UserInfo userInfo,Integer start,Integer length,String userid,String month,Integer fileId) {
		QueryBuilder qb = new QueryBuilder();
		qb.setStart(start);
		qb.setLength(length);
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "u.mobile","mobile");
		QueryUtils.addColumn(qb, "u.name","userName");
		QueryUtils.addColumn(qb, "t.month","month");
		QueryUtils.addColumn(qb, "t.value1","study");
		QueryUtils.addColumn(qb, "t.value2","read");
		QueryUtils.addColumn(qb, "t.value3","culture");
		QueryUtils.addColumn(qb, "t.value4","attendance");
		QueryUtils.addColumn(qb, "t.value5","hse");
		QueryUtils.addColumn(qb, "t.value6","improve");
		QueryUtils.addColumn(qb, "t.total","total");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhereIfNotNull(qb, "and t.userid = {0}", userid);
		QueryUtils.addWhereIfNotNull(qb, "and t.fileId = {0}", fileId);
		QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		QueryUtils.addJoin(qb, "left join UserData u on u.userid = t.openId and u.delFlag = '0'");
		return dataService.DataGrid(qb);
	}
	
	

    @PostMapping(value = "setAverageData")
    public Object setAverageData(String month,Integer fileId,UserInfo userInfo){
        return dataService.setAverageData(month,fileId,userInfo);
    }

}
