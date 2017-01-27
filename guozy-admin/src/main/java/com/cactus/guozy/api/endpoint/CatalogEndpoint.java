package com.cactus.guozy.api.endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cactus.guozy.api.wrapper.CategoryWrapper;
import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.api.wrapper.GoodsWrapper;
import com.cactus.guozy.api.wrapper.ShopWrapper;
import com.cactus.guozy.api.wrapper.UserWrapper;
import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.service.CatalogService;
import com.cactus.guozy.profile.domain.User;

@RestController
@RequestMapping("catalog")
public class CatalogEndpoint {
	
	@Resource(name="catalogService")
	protected CatalogService catalogService;
	
	@RequestMapping(value = { "/shops"}, method = RequestMethod.GET)
	public GenericWebResult getAllShops() {
		List<Shop> shops = catalogService.findAllShops();
		List<ShopWrapper> wrappers = new ArrayList<>();
		for(Shop shop : shops) {
			ShopWrapper wrapper = new ShopWrapper();
			wrapper.wrapSummary(shop);
			wrappers.add(wrapper);
		}
		
		return GenericWebResult.success(wrappers);
	}
	
	@RequestMapping(value = { "/shop/{id}"}, method = RequestMethod.GET)
	public GenericWebResult getShop(@PathVariable("id") Long id) {
		Shop shop = catalogService.findShopById(id);
		ShopWrapper wrapper = new ShopWrapper();
		wrapper.wrapDetails(shop);
		return GenericWebResult.success(wrapper);
	}
	
	@RequestMapping(value = { "/categories"}, method = RequestMethod.GET)
	public GenericWebResult getCategories(
			@RequestParam("sid") Long sid) {
		if (sid == null) {
			return GenericWebResult.error("000101").withData(ErrorMsgWrapper.error("paramsError").withMsg("参数错误"));
		}
		
		List<Category> cats = catalogService.findCategories(sid);
		List<CategoryWrapper> categories = new ArrayList<>();
		for (Category category : cats) {
			CategoryWrapper wrapper = new CategoryWrapper();
			wrapper.wrapSummary(category);
			categories.add(wrapper);
		}
		return GenericWebResult.success(categories);	
	}
	
	@RequestMapping(value = { "/category/{cid}"}, method = RequestMethod.GET)
	public GenericWebResult getCategory(
			@PathVariable("cid") Long cid) {
		
		Category cat = catalogService.findCategory(cid);
		CategoryWrapper category = new CategoryWrapper();
		category.wrapDetails(cat);
		return GenericWebResult.success(category);	
	}
	
	@RequestMapping(value = { "/goods"}, method = RequestMethod.GET)
	public GenericWebResult getAllGoods(
			@RequestParam("cid") Long cid) {
		List<Goods> goods = catalogService.findAllGoods(cid);
		List<GoodsWrapper> gs = new ArrayList<>();
		for (Goods g : goods) {
			GoodsWrapper wrapper = new GoodsWrapper();
			wrapper.wrapDetails(g);
			gs.add(wrapper);
		}
		return GenericWebResult.success(gs);	
	}
	
	@RequestMapping(value = { "/salers"}, method = RequestMethod.GET)
	public GenericWebResult getAllSalers(@RequestParam("sid") Long sid) {
		List<User> salers = catalogService.findSalersByShopId(sid);
		List<UserWrapper> gs = new ArrayList<>();
		for (User g : salers) {
			UserWrapper wrapper = new UserWrapper();
			wrapper.wrapSummary(g);
			gs.add(wrapper);
		}
		return GenericWebResult.success(gs);
	}
	
}
