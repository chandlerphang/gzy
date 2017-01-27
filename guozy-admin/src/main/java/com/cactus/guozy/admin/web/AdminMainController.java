package com.cactus.guozy.admin.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.cactus.guozy.common.json.JsonResponse;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.service.AppSettingService;
import com.cactus.guozy.core.service.CatalogService;

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
		
		setModelAttributes(model, "dianyuanguanli");
		return "saler";
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
