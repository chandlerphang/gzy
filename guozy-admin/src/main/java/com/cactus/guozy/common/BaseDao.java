package com.cactus.guozy.common;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import tk.mybatis.mapper.common.BaseMapper;

public interface BaseDao<T> extends BaseMapper<T> {

	List<T> getAllByPage(RowBounds rowBounds);

}
