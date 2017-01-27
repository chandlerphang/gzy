package com.cactus.guozy.core.service;

import java.util.List;

import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.profile.domain.User;

public interface CatalogService {
	
	List<Shop> findAllShops();
	
	Shop findShopById(Long id);
	
	List<Category> findCategories(Long shopId);
	
	Category findCategory(Long cid);
	
	List<Goods> findAllGoods(Long cid);
	
	List<User> findSalersByShopId(Long sid);
	
}
