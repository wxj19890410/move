package com.move.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.move.model.DataResult;
import com.move.service.DataService;
import com.move.service.UserService;
import com.move.utils.DictUtils;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;

@CrossOrigin
@RestController
@RequestMapping(value = "htChart")
public class HtChart {

	@Autowired
	private DataService dataService;
	@Autowired
	private UserService userService;

	@GetMapping(value = "dataChart12")
	public Object dataChart12(String month, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		List<BigDecimal> chart1 = Lists.newArrayList();
		List<Map<String, Object>> chart2 = Lists.newArrayList();
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.relationType = {0}" , "company");
		QueryUtils.addWhere(qb, "and t.month = {0}", month);
		QueryUtils.addOrder(qb, "t.createDate desc");
		DataResult dataResult = dataService.getDataResult(qb);
		if (null == dataResult) {
			dataResult = new DataResult();
			dataResult.setValue1(new BigDecimal(0));
			dataResult.setValue2(new BigDecimal(0));
			dataResult.setValue3(new BigDecimal(0));
			dataResult.setValue4(new BigDecimal(0));
			dataResult.setValue5(new BigDecimal(0));
			dataResult.setValue6(new BigDecimal(0));
		} else {
			

			if (null == dataResult.getValue1()) {
				dataResult.setValue1(new BigDecimal(0));
			}
			if (null == dataResult.getValue2()) {
				dataResult.setValue2(new BigDecimal(0));
			}
			if (null == dataResult.getValue3()) {
				dataResult.setValue3(new BigDecimal(0));
			}
			if (null == dataResult.getValue4()) {
				dataResult.setValue4(new BigDecimal(0));
			}
			if (null == dataResult.getValue5()) {
				dataResult.setValue5(new BigDecimal(0));
			}
			if (null == dataResult.getValue6()) {
				dataResult.setValue6(new BigDecimal(0));
			}
		}
		System.out.println(dataResult.getValue1());
		chart1.add(dataResult.getValue1());
		chart1.add(dataResult.getValue2());
		chart1.add(dataResult.getValue3());
		chart1.add(dataResult.getValue4());
		chart1.add(dataResult.getValue5());
		chart1.add(dataResult.getValue6());

		Map<String, Object> chart2Map = new HashMap<>();
		chart2Map.put("value", dataResult.getValue1());
		chart2Map.put("name", DictUtils.DATA_VALUE1);
		chart2.add(chart2Map);
		chart2Map = new HashMap<>();
		chart2Map.put("value", dataResult.getValue2());
		chart2Map.put("name", DictUtils.DATA_VALUE2);
		chart2.add(chart2Map);
		chart2Map = new HashMap<>();
		chart2Map.put("value", dataResult.getValue3());
		chart2Map.put("name", DictUtils.DATA_VALUE3);
		chart2.add(chart2Map);
		chart2Map = new HashMap<>();
		chart2Map.put("value", dataResult.getValue4());
		chart2Map.put("name", DictUtils.DATA_VALUE4);
		chart2.add(chart2Map);
		chart2Map = new HashMap<>();
		chart2Map.put("value", dataResult.getValue5());
		chart2Map.put("name", DictUtils.DATA_VALUE5);
		chart2.add(chart2Map);
		chart2Map = new HashMap<>();
		chart2Map.put("value", dataResult.getValue6());
		chart2Map.put("name", DictUtils.DATA_VALUE6);
		chart2.add(chart2Map);
		data.put("chart1", chart1);
		data.put("chart2", chart2);
		return data;
	}

	@GetMapping(value = "yearData")
	public Object yearData(String timeYear, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> data1 = Lists.newArrayList();
		List<Map<String, Object>> data2 = Lists.newArrayList();
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.relationType = 'company'");
		QueryUtils.addWhere(qb, "and t.month like {0}", timeYear + "%");
		QueryUtils.addOrder(qb, "t.createDate desc");
		List<DataResult> dataResults = dataService.resultList(qb);
		List<BigDecimal> value11s = this.setListData();
		List<BigDecimal> value12s = this.setListData();
		List<BigDecimal> value13s = this.setListData();
		List<BigDecimal> value14s = this.setListData();
		List<BigDecimal> value15s = this.setListData();
		List<BigDecimal> value16s = this.setListData();
		if (null != dataResults && dataResults.size() > 0) {
			for (DataResult dataResult : dataResults) {
				if (null == dataResult.getValue1()) {
					dataResult.setValue1(new BigDecimal(0));
				}
				if (null == dataResult.getValue2()) {
					dataResult.setValue2(new BigDecimal(0));
				}
				if (null == dataResult.getValue3()) {
					dataResult.setValue3(new BigDecimal(0));
				}
				if (null == dataResult.getValue4()) {
					dataResult.setValue4(new BigDecimal(0));
				}
				if (null == dataResult.getValue5()) {
					dataResult.setValue5(new BigDecimal(0));
				}
				if (null == dataResult.getValue6()) {
					dataResult.setValue6(new BigDecimal(0));
				}
				int t = 0;
				if (dataResult.getMonth().endsWith("-1")) {
					t = 0;
				} else if (dataResult.getMonth().endsWith("-2")) {
					t = 1;
				} else if (dataResult.getMonth().endsWith("-3")) {
					t = 2;
				} else if (dataResult.getMonth().endsWith("-4")) {
					t = 3;
				} else if (dataResult.getMonth().endsWith("-5")) {
					t = 4;
				} else if (dataResult.getMonth().endsWith("-6")) {
					t = 5;
				} else if (dataResult.getMonth().endsWith("-7")) {
					t = 6;
				} else if (dataResult.getMonth().endsWith("-8")) {
					t = 7;
				} else if (dataResult.getMonth().endsWith("-9")) {
					t = 8;
				} else if (dataResult.getMonth().endsWith("-10")) {
					t = 9;
				} else if (dataResult.getMonth().endsWith("-11")) {
					t = 10;
				} else if (dataResult.getMonth().endsWith("-12")) {
					t = 11;
				}
				value11s.set(t, dataResult.getValue1());
				// value11s.add(dataResult.getValue2());
				value12s.set(t, dataResult.getValue2());
				value13s.set(t, dataResult.getValue3());
				value14s.set(t, dataResult.getValue4());
				value15s.set(t, dataResult.getValue5());
				value16s.set(t, dataResult.getValue6());
			}
		}
		Map<String, Object> chartMap = new HashMap<>();
		chartMap = new HashMap<>();
		chartMap.put("data", value11s);
		chartMap.put("name", DictUtils.DATA_VALUE1);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value12s);
		chartMap.put("name", DictUtils.DATA_VALUE2);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value13s);
		chartMap.put("name", DictUtils.DATA_VALUE3);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value14s);
		chartMap.put("name", DictUtils.DATA_VALUE4);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value15s);
		chartMap.put("name", DictUtils.DATA_VALUE5);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value16s);
		chartMap.put("name", DictUtils.DATA_VALUE6);
		chartMap.put("type", "line");
		data1.add(chartMap);
		data.put("series", data1);
		qb = new QueryBuilder();
		qb.setStart(0);
		qb.setLength(10);
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addColumn(qb,
				"(select sum(u.total) from DataOriginal u where u.userid = t.userid and u.month like {0})", "sumData",
				"%" + timeYear + "%");
		QueryUtils.addWhere(qb, "and t.account is null");
		QueryUtils.addWhere(qb, "and exists(from DataOriginal t1 where t1.month like {0} and t1.userid = t.userid )",
				"%" + timeYear + "%");
		QueryUtils.addOrder(qb, "sumData desc");
		data2 = userService.userDataGrid(qb).getRows();
		data.put("rankData", data2);
		return data;
	}

	private List<BigDecimal> setListData() {
		List<BigDecimal> values = Lists.newArrayList();
		for (int t = 0; t < 12; t++) {
			values.add(new BigDecimal(0));
		}
		return values;
	}

	@GetMapping(value = "findDeptRank")
	public Object findDeptRank(String month, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> data1 = Lists.newArrayList();
		List<Map<String, Object>> data2 = Lists.newArrayList();
		// 生产部门
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.month =t.month and t1.total > t.total and not exists(from DeptRelation u where u.deptId = t1.deptId and u.deptType = '02'))", "rank");
		QueryUtils.addColumn(qb, "t.name", "name");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total/6", "avg");
		QueryUtils.addColumn(qb, "t.deptId", "deptId");
		QueryUtils.addWhere(qb, "and (select count(t1.id) from DataOriginal t1 where t1.month =t.month and t1.total > t.total and not exists(from DeptRelation u where u.deptId = t1.deptId and u.deptType = '02')) < 10");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb,
				"and not exists(from DeptRelation t1 where t1.deptId = t.deptId and t1.deptType = '02')");
		if (StringUtils.isNotBlank(month)) {
			QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		}
		QueryUtils.addOrder(qb, "t.total desc");
		data1 = dataService.originalMapList(qb);

		// 非生产部门
		qb = new QueryBuilder();
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "t.id");
		QueryUtils.addColumn(qb, "(select count(t1.id)+1 from DataOriginal t1 where t1.month =t.month and t1.total > t.total and exists(from DeptRelation u where u.deptId = t1.deptId and u.deptType = '02'))", "rank");
		QueryUtils.addColumn(qb, "t.value1", "study");
		QueryUtils.addColumn(qb, "t.value2", "read");
		QueryUtils.addColumn(qb, "t.value3", "culture");
		QueryUtils.addColumn(qb, "t.value4", "attendance");
		QueryUtils.addColumn(qb, "t.value5", "hse");
		QueryUtils.addColumn(qb, "t.value6", "improve");
		QueryUtils.addColumn(qb, "t.total/6", "avg");
		QueryUtils.addWhere(qb, "and t.delFlag = {0}", DictUtils.NO);
		QueryUtils.addWhere(qb, "and exists(from DeptRelation t1 where t1.deptId = t.deptId and t1.deptType = '02')");
		QueryUtils.addWhere(qb, "and (select count(t1.id) from DataOriginal t1 where t1.month =t.month and t1.total > t.total and exists(from DeptRelation u where u.deptId = t1.deptId and u.deptType = '02')) < 10");
		if (StringUtils.isNotBlank(month)) {
			QueryUtils.addWhereIfNotNull(qb, "and t.month = {0}", month);
		}
		QueryUtils.addOrder(qb, "t.total desc");
		data2 = dataService.originalMapList(qb);
		data.put("data1", data1);
		data.put("data2", data2);
		return data;

	}

	@GetMapping(value = "getTagData")
	public Object getTagData(String timeYear, Integer tagid, UserInfo userInfo) {
		Map<String, Object> data = new HashMap<>();

		List<Map<String, Object>> data1 = Lists.newArrayList();
		List<Map<String, Object>> data2 = Lists.newArrayList();
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.relationType = 'tag'");
		QueryUtils.addWhere(qb, "and t.relationId = {0}", tagid);
		QueryUtils.addWhere(qb, "and t.month like {0}", "%" + timeYear + "%");
		QueryUtils.addOrder(qb, "t.createDate desc");
		List<DataResult> dataResults = dataService.resultList(qb);
		BigDecimal vaule1 = new BigDecimal(0);
		BigDecimal vaule2 = new BigDecimal(0);
		BigDecimal vaule3 = new BigDecimal(0);
		BigDecimal vaule4 = new BigDecimal(0);
		BigDecimal vaule5 = new BigDecimal(0);
		BigDecimal vaule6 = new BigDecimal(0);
		List<BigDecimal> value11s = this.setListData();
		List<BigDecimal> value12s = this.setListData();
		List<BigDecimal> value13s = this.setListData();
		List<BigDecimal> value14s = this.setListData();
		List<BigDecimal> value15s = this.setListData();
		List<BigDecimal> value16s = this.setListData();
		if (null != dataResults && dataResults.size() > 0) {
			for (DataResult dataResult : dataResults) {
				if (null == dataResult.getValue1()) {
					dataResult.setValue1(new BigDecimal(0));
				}
				if (null == dataResult.getValue2()) {
					dataResult.setValue2(new BigDecimal(0));
				}
				if (null == dataResult.getValue3()) {
					dataResult.setValue3(new BigDecimal(0));
				}
				if (null == dataResult.getValue4()) {
					dataResult.setValue4(new BigDecimal(0));
				}
				if (null == dataResult.getValue5()) {
					dataResult.setValue5(new BigDecimal(0));
				}
				if (null == dataResult.getValue6()) {
					dataResult.setValue6(new BigDecimal(0));
				}

				vaule1 = vaule1.add(dataResult.getValue1());
				vaule2 = vaule2.add(dataResult.getValue2());
				vaule3 = vaule3.add(dataResult.getValue3());
				vaule4 = vaule4.add(dataResult.getValue4());
				vaule5 = vaule5.add(dataResult.getValue5());
				vaule6 = vaule6.add(dataResult.getValue6());
				int t = 0;
				if (dataResult.getMonth().endsWith("-1")) {
					t = 0;
				} else if (dataResult.getMonth().endsWith("-2")) {
					t = 1;
				} else if (dataResult.getMonth().endsWith("-3")) {
					t = 2;
				} else if (dataResult.getMonth().endsWith("-4")) {
					t = 3;
				} else if (dataResult.getMonth().endsWith("-5")) {
					t = 4;
				} else if (dataResult.getMonth().endsWith("-6")) {
					t = 5;
				} else if (dataResult.getMonth().endsWith("-7")) {
					t = 6;
				} else if (dataResult.getMonth().endsWith("-8")) {
					t = 7;
				} else if (dataResult.getMonth().endsWith("-9")) {
					t = 8;
				} else if (dataResult.getMonth().endsWith("-10")) {
					t = 9;
				} else if (dataResult.getMonth().endsWith("-11")) {
					t = 10;
				} else if (dataResult.getMonth().endsWith("-12")) {
					t = 11;
				}
				value11s.set(t, dataResult.getValue1());
				// value11s.add(dataResult.getValue2());
				value12s.set(t, dataResult.getValue2());
				value13s.set(t, dataResult.getValue3());
				value14s.set(t, dataResult.getValue4());
				value15s.set(t, dataResult.getValue5());
				value16s.set(t, dataResult.getValue6());
			}
		}
		Map<String, Object> chartMap = new HashMap<>();
		chartMap = new HashMap<>();
		chartMap.put("data", value11s);
		chartMap.put("name", DictUtils.DATA_VALUE1);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value12s);
		chartMap.put("name", DictUtils.DATA_VALUE2);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value13s);
		chartMap.put("name", DictUtils.DATA_VALUE3);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value14s);
		chartMap.put("name", DictUtils.DATA_VALUE4);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value15s);
		chartMap.put("name", DictUtils.DATA_VALUE5);
		chartMap.put("type", "line");
		data1.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("data", value16s);
		chartMap.put("name", DictUtils.DATA_VALUE6);
		chartMap.put("type", "line");
		data1.add(chartMap);
		data.put("series", data1);

		chartMap = new HashMap<>();
		chartMap = new HashMap<>();
		chartMap.put("value", vaule1);
		chartMap.put("name", DictUtils.DATA_VALUE1);
		data2.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("value", vaule2);
		chartMap.put("name", DictUtils.DATA_VALUE2);
		data2.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("value", vaule3);
		chartMap.put("name", DictUtils.DATA_VALUE3);
		data2.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("value", vaule4);
		chartMap.put("name", DictUtils.DATA_VALUE4);
		data2.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("value", vaule5);
		chartMap.put("name", DictUtils.DATA_VALUE5);
		data2.add(chartMap);
		chartMap = new HashMap<>();
		chartMap.put("value", vaule6);
		chartMap.put("name", DictUtils.DATA_VALUE6);
		data2.add(chartMap);
		data.put("data2", data2);
		return data;
	}

}
