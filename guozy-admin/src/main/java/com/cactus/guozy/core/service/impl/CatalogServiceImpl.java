package com.cactus.guozy.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.core.dao.CategoryDao;
import com.cactus.guozy.core.dao.GoodsDao;
import com.cactus.guozy.core.dao.ShopDao;
import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.service.CatalogService;
import com.cactus.guozy.core.service.SalerService;
import com.cactus.guozy.profile.dao.UserDao;

import tk.mybatis.mapper.entity.Example;

@Service("catalogService")
public class CatalogServiceImpl implements CatalogService {
	
	@Autowired
	protected ShopDao shopDao;
	
	@Autowired
	protected CategoryDao categoryDao;
	
	@Autowired 
	protected GoodsDao goodsDao;
	
	@Autowired
	protected UserDao userDao;
	
	@Autowired
	protected SalerService salerService;
	
	public List<Shop> findAllShops() {
		return shopDao.selectAll();
	}
	
	@Override
	public List<Category> findCategories(Shop shop) {
		Example example = new Example(Category.class);
		example.createCriteria().andCondition("sid = ", shop.getId());
		
		return categoryDao.selectByExample(example);
	}

	@Override
	public Shop findShopById(Long id) {
		return shopDao.readShopWithCategory(id);
	}

	@Override
	public List<Category> findCategories(Long shopId) {
		return categoryDao.readCategories(shopId);
	}
	
	@Override
	public Category findCategory(Long cid) {
		return categoryDao.readCategoryWithGoods(cid);
	}

	@Override
	public List<Goods> findAllGoods(Long cid) {
		return goodsDao.readAllGoods(cid);
	}
	
	@Override
	public List<Saler> findSalersByShopId(Long sid) {
		return salerService.getByEntity(Saler.builder().shopId(sid).build());
	}

	@Override
	@Transactional
	public void saveGoods(Goods goods) {
		goodsDao.updateByPrimaryKey(goods);
	}

	@Override
	public Goods findGoodsById(Long id) {
		return goodsDao.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void addGoodsToCategory(Goods goods, Category category) {
		if(goods.getId() == null) {
			int ret = goodsDao.insert(goods);
			if(ret < 1) {
				throw new BizException("500", "");
			}
		}
		
		categoryDao.addGoodsToCategory(goods, category);
	}

}
