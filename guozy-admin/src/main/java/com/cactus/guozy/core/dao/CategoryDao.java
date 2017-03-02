package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.Goods;

import tk.mybatis.mapper.common.Mapper;

public interface CategoryDao extends Mapper<Category> {
	
    int insert(Category record);

    int insertSelective(Category record);
	
    List<Category> readCategories(Long shopId);
    
    Category readCategoryWithGoods(Long cid);
    
    void addGoodsToCategory(Goods goods, Category category);
    
}