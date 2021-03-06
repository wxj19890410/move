package com.move.service.impl;

import com.move.dao.OrgDepartmentDao;
import com.google.common.collect.Lists;
import com.move.dao.DataOriginalDao;
import com.move.dao.DataResultDao;
import com.move.dao.MsgHistoryDao;
import com.move.dao.OrgGroupDao;
import com.move.dao.OrgRelationDao;
import com.move.dao.SysFileDao;
import com.move.dao.UseDataDao;
import com.move.model.DataOriginal;
import com.move.model.DataResult;
import com.move.model.OrgDepartment;
import com.move.model.OrgGroup;
import com.move.model.UserData;
import com.move.service.DataService;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.QueryUtils;
import com.move.utils.UserInfo;
import com.move.utils.Utilities;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DataServiceImpl implements DataService {
	@Autowired
	private DataOriginalDao dataOriginalDao;

	@Autowired
	private DataResultDao dataResultDao;

	@Autowired
	private OrgRelationDao orgRelationDao;

	@Autowired
	private SysFileDao sysFileDao;

	@Autowired
	private OrgGroupDao orgGroupDao;

	@Autowired
	private OrgDepartmentDao orgDepartmentDao;

	@Autowired
	private MsgHistoryDao msgHistoryDao;

	@Autowired
	private UseDataDao useDataDao;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map<String, Object>> resultMapList(QueryBuilder qb) {
		return dataResultDao.listMap(qb);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Datagrid msgHistoryDatagrid(QueryBuilder qb) {
		return msgHistoryDao.datagrid(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Datagrid DataResultGrid(QueryBuilder qb) {
		return dataResultDao.datagrid(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<DataResult> resultList(QueryBuilder qb) {
		return dataResultDao.find(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Map<String, Object> originalMap(QueryBuilder qb) {
		return dataOriginalDao.getMap(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Datagrid sysFlieGrid(QueryBuilder qb) {
		return sysFileDao.datagrid(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map<String, Object>> originalMapList(QueryBuilder qb) {
		return dataOriginalDao.listMap(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Datagrid DataGrid(QueryBuilder qb) {
		return dataOriginalDao.datagrid(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public DataResult getDataResult(QueryBuilder qb) {
		return dataResultDao.get(qb);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer setAverageData(String time, Integer fileId, UserInfo userInfo) {
		// 升级源数据
		QueryBuilder qb = new QueryBuilder();
		QueryUtils.addSetColumn1(qb, "t.total = t.value1+t.value2+t.value3+t.value4+t.value5+t.value6");
		QueryUtils.addSetColumn1(qb, "t.userid = (select t1.userid from UserData t1 where t1.mobile = t.mobile)");
		QueryUtils.addSetColumn1(qb, "t.deptId = (select t1.deptId from UserData t1 where t1.mobile = t.mobile)");
		QueryUtils.addWhereIfNotNull(qb, "and t.month={0}", time);
		QueryUtils.addWhereIfNotNull(qb, "and t.fileId={0}", fileId);
		dataOriginalDao.update(qb);
		// 删除
		if (StringUtils.isNotBlank(time)) {
			qb = new QueryBuilder();
			QueryUtils.addWhere(qb, "and t.month={0}", time);
			QueryUtils.addWhereIfNotNull(qb, "and t.fileId={0}", fileId);
			dataResultDao.delete(qb);
		}

		Date now = new Date();
		// 插入公司数据
		StringBuilder sb = new StringBuilder();
		sb.append("insert into data_result (");
		sb.append(" relation_type");
		sb.append(",file_id");
		sb.append(",month");
		sb.append(",person_nub");
		sb.append(",value1");
		sb.append(",value2");
		sb.append(",value3");
		sb.append(",value4");
		sb.append(",value5");
		sb.append(",value6");
		sb.append(",total");
		sb.append(",del_flag");
		sb.append(",edit_date");
		sb.append(",create_date");
		sb.append(",create_user ");
		sb.append(")");
		sb.append("select");
		sb.append(" 'company'");
		sb.append("," + fileId);
		sb.append(",'" + time + "'");
		sb.append(",count(A.id)");
		sb.append(",avg(cast(A.value1 as decimal(18,2)))");
		sb.append(",avg(cast(A.value2 as decimal(18,2)))");
		sb.append(",avg(cast(A.value3 as decimal(18,2)))");
		sb.append(",avg(cast(A.value4 as decimal(18,2)))");
		sb.append(",avg(cast(A.value5 as decimal(18,2)))");
		sb.append(",avg(cast(A.value6 as decimal(18,2)))");
		sb.append(",avg(cast(A.total as decimal(18,2)))");
		sb.append(",'0'");
		sb.append(",:p1");
		sb.append(",:p1");
		sb.append(",count(A.create_user)");
		sb.append(" from data_original A where ");
		sb.append(" A.del_flag='0'");
		sb.append(" and A.month='" + time + "'");
		dataResultDao.sqlUpdate(sb.toString(), now);
		qb = new QueryBuilder();
		QueryUtils.addWhere(qb,
				"and exists(from OrgRelation t1 where t1.relationId = t.id and t1.relationType='dept')");
		List<OrgDepartment> orgDepartments = orgDepartmentDao.find(qb);

		// 插入部门数据
		for (OrgDepartment orgDepartment : orgDepartments) {
			System.out.println(orgDepartment.getId());
			sb = new StringBuilder();
			sb.append("insert into data_result (");
			sb.append(" relation_type");
			sb.append(",relation_id");
			sb.append(",file_id");
			sb.append(",month");
			sb.append(",person_nub");
			sb.append(",value1");
			sb.append(",value2");
			sb.append(",value3");
			sb.append(",value4");
			sb.append(",value5");
			sb.append(",value6");
			sb.append(",total");
			sb.append(",del_flag");
			sb.append(",edit_date");
			sb.append(",create_date");
			sb.append(",create_user");
			sb.append(") ");
			sb.append("select");
			sb.append(" 'dept'");
			sb.append(",:p2");
			sb.append("," + fileId);
			sb.append(",'" + time + "'");
			sb.append(",count(A.id)");
			sb.append(",avg(cast(A.value1 as decimal(18,2)))");
			sb.append(",avg(cast(A.value2 as decimal(18,2)))");
			sb.append(",avg(cast(A.value3 as decimal(18,2)))");
			sb.append(",avg(cast(A.value4 as decimal(18,2)))");
			sb.append(",avg(cast(A.value5 as decimal(18,2)))");
			sb.append(",avg(cast(A.value6 as decimal(18,2)))");
			sb.append(",avg(cast(A.total as decimal(18,2)))");
			sb.append(",'0'");
			sb.append(",:p1");
			sb.append(",:p1");
			sb.append(",count(A.create_user)");
			sb.append(" from org_relation B ,data_original A where ");
			sb.append(" B.relation_type='dept'");
			sb.append(" and A.del_flag='0'");
			sb.append(" and B.userid=A.userid");
			sb.append(" and B.relation_id=" + orgDepartment.getId());
			sb.append(" and A.month='" + time + "';");
			dataResultDao.sqlUpdate(sb.toString(), now, orgDepartment.getId());
		}

		qb = new QueryBuilder();
		QueryUtils.addWhere(qb,
				"and exists(from OrgRelation t1 where t1.relationId = t.tagid and t1.relationType = 'tag')");
		List<OrgGroup> orgGroups = orgGroupDao.find(qb);
		// 插入部门数据
		for (OrgGroup orgGroup : orgGroups) {
			sb = new StringBuilder();
			sb.append("insert into data_result (");
			sb.append(" relation_type");
			sb.append(",relation_id");
			sb.append(",file_id");
			sb.append(",month");
			sb.append(",person_nub");
			sb.append(",value1");
			sb.append(",value2");
			sb.append(",value3");
			sb.append(",value4");
			sb.append(",value5");
			sb.append(",value6");
			sb.append(",total");
			sb.append(",del_flag");
			sb.append(",edit_date");
			sb.append(",create_date");
			sb.append(",create_user");
			sb.append(") ");
			sb.append("select");
			sb.append(" 'tag'");
			sb.append(",:p2");
			sb.append("," + fileId);
			sb.append(",'" + time + "'");
			sb.append(",count(A.id)");
			sb.append(",avg(cast(A.value1 as decimal(18,2)))");
			sb.append(",avg(cast(A.value2 as decimal(18,2)))");
			sb.append(",avg(cast(A.value3 as decimal(18,2)))");
			sb.append(",avg(cast(A.value4 as decimal(18,2)))");
			sb.append(",avg(cast(A.value5 as decimal(18,2)))");
			sb.append(",avg(cast(A.value6 as decimal(18,2)))");
			sb.append(",avg(cast(A.total as decimal(18,2)))");
			sb.append(",'0'");
			sb.append(",:p1");
			sb.append(",:p1");
			sb.append(",count(A.create_user)");
			sb.append(" from org_relation B ,data_original A where ");
			sb.append(" B.relation_type='tag'");
			sb.append(" and A.del_flag='0'");
			sb.append(" and B.userid=A.userid");
			sb.append(" and B.relation_id=" + orgGroup.getTagid());
			sb.append(" and A.month='" + time + "'");
			sb.append(";");
			dataResultDao.sqlUpdate(sb.toString(), now, orgGroup.getTagid());
		}

		// 插入个人平均值
		qb = new QueryBuilder();
		QueryUtils.addWhere(qb, "and t.account is null");
		List<UserData> userDatas = useDataDao.find(qb);
		if (null != userDatas && userDatas.size() > 0) {
			List<String> userids = Lists.newArrayList();
			for (UserData userData : userDatas) {
				userids.add(userData.getUserid());
			}
			// 删除
			qb = new QueryBuilder();
			QueryUtils.addWhere(qb, "and t.relationType = 'preson'");
			QueryUtils.addWhere(qb, "and t.userid in {0}", userids);
			dataResultDao.delete(qb);

			for (UserData userData : userDatas) {
				sb = new StringBuilder();
				sb.append("insert into data_result (");
				sb.append(" relation_type");
				sb.append(",userid");
				sb.append(",value1");
				sb.append(",value2");
				sb.append(",value3");
				sb.append(",value4");
				sb.append(",value5");
				sb.append(",value6");
				sb.append(",total");
				sb.append(",del_flag");
				sb.append(",edit_date");
				sb.append(",create_date");
				sb.append(",create_user");
				sb.append(") ");
				sb.append("select");
				sb.append(" 'preson'");
				sb.append(",:p2");
				sb.append(",avg(cast(A.value1 as decimal(18,2)))");
				sb.append(",avg(cast(A.value2 as decimal(18,2)))");
				sb.append(",avg(cast(A.value3 as decimal(18,2)))");
				sb.append(",avg(cast(A.value4 as decimal(18,2)))");
				sb.append(",avg(cast(A.value5 as decimal(18,2)))");
				sb.append(",avg(cast(A.value6 as decimal(18,2)))");
				sb.append(",avg(cast(A.total as decimal(18,2)))");
				sb.append(",'0'");
				sb.append(",:p1");
				sb.append(",:p1");
				sb.append(",count(A.create_user)");
				sb.append(" from data_original A where ");
				sb.append(" A.del_flag='0'");
				sb.append(" and A.userid='" + userData.getUserid() + "'");
				sb.append(";");
				dataResultDao.sqlUpdate(sb.toString(), now, userData.getUserid());
			}
		}

		return orgDepartments.size() + orgGroups.size() + 1;
	}

	@Override
	public DataOriginal originalUpdate(Integer id, Integer value1, Integer value2, Integer value3, Integer value4,
			Integer value5, Integer value6, UserInfo userInfo) {

		DataOriginal data = dataOriginalDao.get(id);
		data.setValue1(value1);
		data.setValue2(value2);
		data.setValue3(value3);
		data.setValue4(value4);
		data.setValue5(value5);
		data.setValue6(value6);
		Utilities.setUserInfo(data, userInfo);
		return dataOriginalDao.update(data);
	}

	

}
