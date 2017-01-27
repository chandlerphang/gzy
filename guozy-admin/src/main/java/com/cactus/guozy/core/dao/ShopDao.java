package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.Shop;

import tk.mybatis.mapper.common.base.BaseSelectMapper;

public interface ShopDao extends BaseSelectMapper<Shop> {

	int insert(Shop record);

    int insertSelective(Shop record);
    
    List<Shop> readAllShops();
    
    Shop readShopWithCategory(Long id);
}