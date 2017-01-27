package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.Category;

public interface CategoryDao {
	
    int insert(Category record);

    int insertSelective(Category record);
    
    List<Category> readCategories(Long shopId);
    
    Category readCategoryWithGoods(Long cid);
    
}