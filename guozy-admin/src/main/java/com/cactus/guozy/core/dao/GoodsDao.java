package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.Goods;

import tk.mybatis.mapper.common.base.BaseSelectMapper;

public interface GoodsDao extends BaseSelectMapper<Goods> {

    int insert(Goods record);

    int insertSelective(Goods record);
    
    /**
     * 查找分类下的所有商品
     * 
     * @param cid 分类Id
     */
    List<Goods> readAllGoods(Long cid);
    
}