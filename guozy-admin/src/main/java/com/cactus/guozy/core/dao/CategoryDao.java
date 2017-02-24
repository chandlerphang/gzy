package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.Category;

import tk.mybatis.mapper.common.Mapper;

public interface CategoryDao extends Mapper<Category> {
	
    int insert(Category record);

    int insertSelective(Category record);
    
    List<Category> readCategories(Long shopId);
    
    Category readCategoryWithGoods(Long cid);
    
}