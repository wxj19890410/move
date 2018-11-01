package com.move.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.move.dao.BaseDao;
import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;
import com.move.utils.Utilities;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import javax.persistence.EntityManagerFactory;

@NoRepositoryBean
public abstract class BaseDaoImpl<T> implements BaseDao<T>  {




	public final Class<T> modelClass;

	public final String HQL_UPDATE;

	public final String HQL_DELETE;

	public final String HQL_FIND;

	public final String HQL_COUNT;

	public final String HQL_LIST_MAP;

	public final String PREFIX_PARAMS = "p";


	@Autowired
	private EntityManagerFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		modelClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

		HQL_UPDATE = " update " + modelClass.getSimpleName() + " t set {0} where 1 = 1 {1} ";

		HQL_DELETE = " delete " + modelClass.getSimpleName() + " t where 1 = 1 {0} ";

		HQL_FIND = " select t from " + modelClass.getSimpleName() + " t {2} where 1 = 1 {0} {1} ";

		HQL_COUNT = " select cast(count(*) as int) from " + modelClass.getSimpleName() + " t {1} where 1 = 1 {0} ";

		HQL_LIST_MAP = " select {0} from " + modelClass.getSimpleName() + " t {5} where 1 = 1 {1} {3} {4} {2} ";
	}

	protected Session getSession() {
		return sessionFactory.createEntityManager().unwrap(org.hibernate.Session.class);
	}

	protected Query setParams(Query query, List<Object> params, String prefix) {
		if (params != null) {
			for (int i = 0; i < params.size(); ++i) {
				if (params.get(i) instanceof Collection) {
					Collection<?> collection = (Collection<?>) params.get(i);

					if (collection.size() == 0) {
						collection.add(null);
					}
					query.setParameter(prefix + (i + 1), collection);
				} else {
					query.setParameter(prefix + (i + 1), params.get(i));
				}
			}
		}

		return query;
	}

	protected Query setColumnParams(Query query, QueryBuilder qb) {
		return setParams(query, qb.getColumnParams(), qb.PREFIX_COLUMN_PARAMS);
	}

	protected Query setWhereParams(Query query, QueryBuilder qb) {
		return setParams(query, qb.getWhereParams(), qb.PREFIX_WHERE_PARAMS);
	}

	protected Query setFirstResult(Query query, QueryBuilder qb) {
		if (qb.getStart() > 0) {
			query.setFirstResult(qb.getStart());
		}

		return query;
	}

	protected Query setMaxResults(Query query, QueryBuilder qb) {
		if (qb.getLength() > 0) {
			query.setMaxResults(qb.getLength());
		}

		return query;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public T load(Integer id) {
		return (T) getSession().load(modelClass, id);
	}


	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public T get(Integer id) {
		return (T) getSession().get(modelClass, id);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public T get(QueryBuilder qb) {
		List<T> objs = find(qb);
		return objs.size() == 1 ? objs.get(0) : null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public T save(T obj) {
		getSession().save(obj);
		return obj;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public T update(T obj) {
		getSession().update(obj);
		return obj;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public T saveOrUpdate(T obj) {
		getSession().saveOrUpdate(obj);
		return obj;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Integer update(QueryBuilder qb) {
		if (!qb.hasColumn() || !qb.hasWhere()) {
			throw new RuntimeException("No column or where condition.");
		}

		String hql = Utilities.format(HQL_UPDATE, qb.getColumn(), qb.getWhere());
		Query query = getSession().createQuery(hql);
		setColumnParams(query, qb);
		setWhereParams(query, qb);
		return query.executeUpdate();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(T obj) {
		getSession().delete(obj);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer id) {
		delete(load(id));
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Integer delete(QueryBuilder qb) {
		if (!qb.hasWhere()) {
			throw new RuntimeException("No where condition.");
		}

		String hql = Utilities.format(HQL_DELETE, qb.getWhere());
		Query query = getSession().createQuery(hql);
		setWhereParams(query, qb);
		return query.executeUpdate();
	}


	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<T> find(QueryBuilder qb) {
		String hql = Utilities.format(HQL_FIND, qb.getWhere(), qb.getOrder(), qb.getJoin());
		Query query = getSession().createQuery(hql);
		setWhereParams(query, qb);
		setFirstResult(query, qb);
		setMaxResults(query, qb);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map<String, Object>> listMap(QueryBuilder qb) {
		String hql = Utilities.format(HQL_LIST_MAP, qb.getColumn(), qb.getWhere(), qb.getOrder(), qb.getGroup(), qb.getHaving(), qb.getJoin());
		Query query = getSession().createQuery(hql);
		setColumnParams(query, qb);
		setWhereParams(query, qb);
		setFirstResult(query, qb);
		setMaxResults(query, qb);

		return query.getResultList();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Integer count(QueryBuilder qb) {
		String hql = Utilities.format(HQL_COUNT, qb.getWhere(), qb.getJoin());
		Query query = getSession().createQuery(hql);
		setWhereParams(query, qb);
		return (Integer) query.getFirstResult();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Datagrid datagrid(QueryBuilder qb) {
		Datagrid dg = new Datagrid();
		dg.setRows(listMap(qb));

		if (qb.getLength() > 0) {
			dg.setCount(count(qb));
		}

		return dg;
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<T> hqlFind(String hql, Object... params) {
		Query query = getSession().createQuery(hql);
		setParams(query, Lists.newArrayList(params), PREFIX_PARAMS);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map<String, Object>> hqlListMap(String hql, Object... params) {
		Query query = getSession().createQuery(hql);
		setParams(query, Lists.newArrayList(params), PREFIX_PARAMS);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public <U> U hqlUnique(String hql, Object... params) {
		Query query = getSession().createQuery(hql);
		setParams(query, Lists.newArrayList(params), PREFIX_PARAMS);
		return (U) query.getSingleResult();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Integer hqlUpdate(String hql, Object... params) {
		Query query = getSession().createQuery(hql);
		setParams(query, Lists.newArrayList(params), PREFIX_PARAMS);
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<T> sqlFind(String sql, Object... params) {
		Query query = getSession().createSQLQuery(sql);
		setParams(query, Lists.newArrayList(params), PREFIX_PARAMS);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Map<String, Object>> sqlListMap(String sql, Object... params) {
		Query query = getSession().createSQLQuery(sql);
		setParams(query, Lists.newArrayList(params), PREFIX_PARAMS);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public <U> U sqlUnique(String sql, Object... params) {
		Query query = getSession().createSQLQuery(sql);
		setParams(query, Lists.newArrayList(params), PREFIX_PARAMS);
		return (U) query.getSingleResult();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Integer sqlUpdate(String sql, Object... params) {
		Query query = getSession().createSQLQuery(sql);
		setParams(query, Lists.newArrayList(params), PREFIX_PARAMS);
		return query.executeUpdate();
	}
}