package com.cactus.guozy.admin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.common.cms.Asset;
import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.common.json.JsonResponse;
import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.service.AppSettingService;
import com.cactus.guozy.core.service.CatalogService;
import com.cactus.guozy.profile.domain.User;

@Controller
public class AdminMainController extends AbstractAdminController{
	
	@Resource(name="appSettingService")
	protected AppSettingService appSettingService;
	
	@Resource(name="catalogService")
	protected CatalogService catalogService;
	
	@RequestMapping(value = { "/appnotification"}, method = RequestMethod.GET)
	public String appnotification(Model model) {
		setModelAttributes(model, "tuisong");
		return "appnotification";
	}
	
	@RequestMapping(value = {"/appsettings"}, method = RequestMethod.GET)
	public String appsettings(Model model) {
		model.addAttribute("aboutus", appSettingService.findAboutusUrl());
		
		setModelAttributes(model, "appsettings");
		return "appsettings";
	}
	
	@RequestMapping(value = {"/appsettings/aboutus"}, method = RequestMethod.POST)
	public void aboutus(@RequestParam("url") String url, HttpServletResponse resp) {
		appSettingService.saveAboutusUrl(url);
		new JsonResponse(resp).with("status", "200").with("data", "ok").done();
	}
	
	@RequestMapping(value = {"/category"}, method = RequestMethod.GET)
	public String category(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		List<List<Category>> categories=new ArrayList<>();
		for(int i=0;i<shops.size();i++){
			categories.add(catalogService.findCategories(shops.get(i).getId()));
		}
		model.addAttribute("shops", shops);
		model.addAttribute("categories", categories);
		setModelAttributes(model, "leimuguanli");
		return "category";
	}
	
	@RequestMapping(value = {"/shop"}, method = RequestMethod.GET)
	public String shop(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);
		
		setModelAttributes(model, "dianpuguanli");
		return "shop";
	}
	
	
	@RequestMapping(value = {"/saler"}, method = RequestMethod.GET)
	public String saler(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);
		List<User> salers=catalogService.findSalersByShopId(1l);
		model.addAttribute("salers", salers);
		
		setModelAttributes(model, "dianyuanguanli");
		return "saler";
	}
	
	@RequestMapping(value = {"/salerlist"}, method = RequestMethod.GET)
	public String salerlist(@RequestParam("sid")String sid,HttpServletResponse resp,Model model) {
		resp.setHeader("x-frame-options", "sameorigin");
		List<User> salers=catalogService.findSalersByShopId(Long.parseLong(sid));
		model.addAttribute("salers", salers);
		model.addAttribute("sid", sid);
		return "salerlist";
	}
	
	@RequestMapping(value = {"/goods"}, method = RequestMethod.GET)
	public String goods(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);
		
		setModelAttributes(model, "shangpinguanli");
		return "goods";
	}
	
	
	@RequestMapping(value = { "/user"}, method = RequestMethod.GET)
	public String user(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		
		setModelAttributes(model, "yonghuguanli");
		return "user";
	}
	
}
