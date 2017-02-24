package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.Goods;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

public interface GoodsDao extends Mapper<Goods>, IdsMapper<Goods>{

    /**
     * 查找分类下的所有商品
     * 
     * @param cid 分类Id
     */
    List<Goods> readAllGoods(Long cid);
    
}