package org.sojourner.message.core.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.sojourner.message.core.page.PageBean;
import org.sojourner.message.core.page.PageParam;
import org.apache.ibatis.session.SqlSession;

public interface BaseDao<T> {

	int insert(T entity);

	int insert(List<T> list);

	int update(T entity);

	int update(List<T> list);

	int update(Map<String, Object> paramMap);

	T getById(String id);

	public T getByColumn(Map<String, Object> paramMap);

	public T getBy(Map<String, Object> paramMap);

	public List<T> listBy(Map<String, Object> paramMap);

	public List<T> listByColumn(Map<String, Object> paramMap);

	Long getCountByColumn(Map<String, Object> paramMap);

	int delete(String id);

	int delete(List<T> list);

	int delete(Map<String, Object> paramMap);

	public PageBean listPage(PageParam pageParam, Map<String, Object> paramMap);

	public SqlSessionTemplate getSessionTemplate();

	public SqlSession getSqlSession();
}
