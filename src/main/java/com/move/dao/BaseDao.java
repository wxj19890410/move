package com.move.dao;

import com.move.utils.Datagrid;
import com.move.utils.QueryBuilder;

import java.util.List;
import java.util.Map;


public interface BaseDao<T> {
	public T load(Integer id);

	public T get(Integer id);

	public T get(QueryBuilder qb);

	public T save(T obj);

	public T update(T obj);

	public T saveOrUpdate(T obj);

	public Integer update(QueryBuilder qb);

	public void delete(T obj);

	public void delete(Integer id);

	public Integer delete(QueryBuilder qb);

	public List<T> find(QueryBuilder qb);

	public List<Map<String, Object>> listMap(QueryBuilder qb);

	public Integer count(QueryBuilder qb);

	public Datagrid datagrid(QueryBuilder qb);

	public List<T> hqlFind(String hql, Object... params);

	public List<Map<String, Object>> hqlListMap(String hql, Object... params);

	public <U> U hqlUnique(String hql, Object... params);

	public Integer hqlUpdate(String hql, Object... params);

	public List<T> sqlFind(String sql, Object... params);

	public List<Map<String, Object>> sqlListMap(String sql, Object... params);

	public <U> U sqlUnique(String sql, Object... params);

	public Integer sqlUpdate(String sql, Object... params);
	/**
	 * 加载Map对象
	 * 
	 * @param qb
	 *            查询构建类
	 * @return
	 */
	public Map<String, Object> getMap(QueryBuilder qb);
}
