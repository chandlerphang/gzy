package com.cactus.guozy.core.service;

import java.util.List;

import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.domain.Shop;

public interface CatalogService {
	
	List<Shop> findAllShops();
	
	List<Category> findCategories(Shop shop);
	
	Shop findShopById(Long id);
	
	List<Category> findCategories(Long shopId);
	
	Category findCategory(Long cid);
	
	List<Goods> findAllGoods(Long cid);
	
	Goods findGoodsById(Long id);
	
	void saveGoods(Goods goods);
	
	void addGoodsToCategory(Goods goods, Category category);
	
	void removeGoods(Goods goods);
	
	List<Saler> findSalersByShopId(Long sid);
	
}
