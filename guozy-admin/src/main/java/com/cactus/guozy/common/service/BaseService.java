package com.cactus.guozy.common.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.cactus.guozy.common.exception.DBException;
import com.github.pagehelper.PageInfo;

public interface BaseService<T> {

	/**
	 * 根据主键查询指定实体
	 * 
	 * @param id
	 * @return
	 */
	T getById(Object id);

	/**
	 * 列表查询
	 * 
	 * @param entity
	 * @return
	 */
	List<T> getByEntity(T entity);

	/**
	 * 获取分页数据
	 * 
	 * @param rowBounds
	 * @return
	 */
	PageInfo<T> getByPage(RowBounds rowBounds);

	/**
	 * 保存对象，保存所有属性
	 * 
	 * @param entity
	 * @return
	 */
	int save(T entity);

	/**
	 * 更新对象中所有属性,主键不能为NULL
	 * 
	 * @param entity
	 * @return
	 */
	int update(T entity);

	/**
	 * 删除指定数据
	 * 
	 * @param id
	 * @return
	 */
	int delete(Object id);

	/**
	 * 保存对象，只保存对象中不为NULL的属性
	 * 
	 * @param entity
	 * @return
	 */
	int saveSelective(T entity) throws DBException;

	/**
	 * 更新对象，只更新对象中不为NULL的属性，主键不能为NULL
	 * 
	 * @param entity
	 * @return
	 */
	int updateSelective(T entity);

}
