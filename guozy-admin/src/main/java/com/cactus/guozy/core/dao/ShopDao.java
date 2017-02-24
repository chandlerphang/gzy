package com.cactus.guozy.core.dao;

import com.cactus.guozy.core.domain.Shop;

import tk.mybatis.mapper.common.base.BaseSelectMapper;

public interface ShopDao extends BaseSelectMapper<Shop> {

	int insert(Shop record);

    int insertSelective(Shop record);
    
    Shop readShopWithCategory(Long id);
}