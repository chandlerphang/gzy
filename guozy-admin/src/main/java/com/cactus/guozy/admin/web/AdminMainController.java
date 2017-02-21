package com.cactus.guozy.admin.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import com.cactus.guozy.api.endpoint.AppEndpoint;
import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.api.wrapper.FruitCSWrapper;
import com.cactus.guozy.common.cms.Asset;
import com.cactus.guozy.common.cms.AssetService;
import com.cactus.guozy.common.cms.AssetStorageService;
import com.cactus.guozy.common.file.FileService;
import com.cactus.guozy.common.json.JsonResponse;
import com.cactus.guozy.common.utils.Strings;
import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.FruitCommonSense;
import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.service.AppSettingService;
import com.cactus.guozy.core.service.CatalogService;
import com.cactus.guozy.core.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AdminMainController extends AbstractAdminController{
	protected static final Logger LOG = LoggerFactory.getLogger(AppEndpoint.class);
	
	@Resource(name = "fileService")
	protected FileService fileService;

	@Resource(name = "assetStorageService")
	protected AssetStorageService ass;
	
	@Resource(name = "assetService")
	protected AssetService assetService;
	
	@Resource(name="appSettingService")
	protected AppSettingService appSettingService;
	
	@Resource(name="catalogService")
	protected CatalogService catalogService;

	@Resource(name="orderService")
	protected OrderService orderService;
	
	@RequestMapping(value = { "/appnotification"}, method = RequestMethod.GET)
	public String appnotification(HttpServletResponse resp,Model model) {
		resp.setHeader("x-frame-options", "sameorigin");
		setModelAttributes(model, "tuisong");
		return "appnotification";
	}
	
	@RequestMapping(value = {"/appsettings"}, method = RequestMethod.GET)
	public String appsettings(Model model) {
		model.addAttribute("aboutus", appSettingService.findAboutusUrl());
		List<FruitCSWrapper> wrappers = new ArrayList<>();
		List<FruitCommonSense> fcs = appSettingService.findAllFruitCommonSense();
		for(FruitCommonSense tmp : fcs) {
			wrappers.add(FruitCSWrapper.wrapDetail(tmp));
		}
		model.addAttribute("fruits", wrappers);
		
		setModelAttributes(model, "appsettings");
		return "appsettings";
	}
	
	@RequestMapping(value = {"/appsettings/aboutus"}, method = RequestMethod.POST)
	public void aboutus(@RequestParam("url") String url, HttpServletResponse resp) {
		appSettingService.saveAboutusUrl(url);
		new JsonResponse(resp).with("status", "200").with("data", "ok").done();
	}
	
	@RequestMapping(value = {"/appsettings/fruitcs"}, method = RequestMethod.POST)
	public void fruitcs(
			@RequestParam("file") MultipartFile file,
			@Valid FruitCSWrapper fruitcs,
			HttpServletResponse resp) {
		
		Map<String, String> properties = new HashMap<>();
		properties.put("module", "fruitcs");
		properties.put("resourceId", RandomStringUtils.randomNumeric(6));
		
		Asset asset=assetService.createAssetFromFile(file, properties);
		if(asset == null) {
			new JsonResponse(resp)
				.with("error", "服务器内部错误")
				.done();
		}
		
		try {
			ass.storeAssetFile(file, asset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		fruitcs.setPicurl(asset.getUrl());
		appSettingService.saveFruitCommonSense(fruitcs.unwrap());
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
		List<Saler> salers=catalogService.findSalersByShopId(1l);
		model.addAttribute("salers", salers);
		
		setModelAttributes(model, "dianyuanguanli");
		return "saler";
	}
	
	
	@RequestMapping(value = {"/ordershop"}, method = RequestMethod.GET)
	public String ordershop(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);
		List<Saler> salers=catalogService.findSalersByShopId(1l);
		model.addAttribute("salers", salers);
		setModelAttributes(model, "dingdanguanli");
		return "ordershop";
	}
	
	
	@RequestMapping(value = {"/salerlist"}, method = RequestMethod.GET)
	public String salerlist(@RequestParam("sid")String sid,HttpServletResponse resp,Model model) {
		resp.setHeader("x-frame-options", "sameorigin");
		List<Saler> salers=catalogService.findSalersByShopId(Long.parseLong(sid));
		model.addAttribute("salers", salers);
		model.addAttribute("sid", sid);
		return "salerlist";
	}
	
	@RequestMapping(value = {"/orderlist"}, method = RequestMethod.GET)
	public String orderlist(@RequestParam("sid")Long sid,HttpServletResponse resp,Model model) {
		resp.setHeader("x-frame-options", "sameorigin");
		List<Order> orders=orderService.findOrdersCompleted(1001l);
		model.addAttribute("orders", orders);
		model.addAttribute("sid", sid);
		System.out.println("订单数量");
		return "orderlist";
	}
	
	@RequestMapping(value = {"/order"}, method = RequestMethod.GET)
	public String order(@RequestParam("orderId")Long orderId,HttpServletResponse resp,Model model) {
		resp.setHeader("x-frame-options", "sameorigin");
		Order order=orderService.findOrderById(orderId);
		model.addAttribute("orders", order);
		return "order";
	}
	
	@RequestMapping(value = {"/goods"}, method = RequestMethod.GET)
	public String goods(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);
		
		setModelAttributes(model, "shangpinguanli");
		return "goods";
	}
	
	@RequestMapping(value = {"/goodlist"}, method = RequestMethod.GET)
	public String goodlist(HttpServletResponse resp,Model model,@RequestParam("cateId") Long categoryId) {
		resp.setHeader("x-frame-options", "sameorigin");
		List<Goods> goods = catalogService.findAllGoods(categoryId);
		model.addAttribute("goods", goods);
		for (Goods goodsItem : goods) {
			goodsItem.setPic("http://101.200.134.112:8080/guozy/cmsasset"+goodsItem.getPic());
		}
		setModelAttributes(model, "shangpinguanli");
		return "goodlist";
	}
	
	@RequestMapping(value = {"/category"}, method = RequestMethod.POST)
	public void categorys(@RequestParam("shopId") Long shopId,
			HttpServletResponse resp) {
		List<Category> categories = catalogService.findCategories(shopId);
		
		new JsonResponse(resp).with("status", "200").with("data", categories).done();
	}
	
	@RequestMapping(value = {"/shops"}, method = RequestMethod.POST)
	public void shops(
			HttpServletResponse resp) {
		List<Shop> shops = catalogService.findAllShops();		
		new JsonResponse(resp).with("status", "200").with("data", shops).done();
	}
	
	
	
	@RequestMapping(value = { "/user"}, method = RequestMethod.GET)
	public String user(Model model) {
		model.addAttribute("users", userService.findAllUsers());
		
		setModelAttributes(model, "yonghuguanli");
		return "user";
	}
	
	
	@RequestMapping(value = "/{userId}/update", method = RequestMethod.GET)
	public GenericWebResult updateUserInfo(HttpServletRequest request,
			@RequestParam("nickname") String name, @PathVariable("userId") Long userId) {

//		// 1、頭像
//		if (file != null) {
//			Map<String, String> properties = new HashMap<>();
//			properties.put("module", "profile");
//			properties.put("resourceId", userId.toString());
//
//			LOG.debug("开始头像资源存储.");
//			Asset asset = assetService.createAssetFromFile(file, properties);
//
//			if (asset == null) {
//				return GenericWebResult.error("500").withData(ErrorMsgWrapper.error("unknownError").withMsg("服务器内部错误"));
//			}
//
//			try {
//				ass.storeAssetFile(file, asset);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			userService.updateAvatarUrl(userId, asset.getUrl());
//		}
//		LOG.debug("结束头像资源存储.");
		//2.昵称
		if(Strings.isNullOrEmpty(name)){
			return GenericWebResult.error("-1").withData(ErrorMsgWrapper.error("datanull").withMsg("昵称不能为空"));
		}
		
		if (userService.updateNickname(userId, name)) {
			return GenericWebResult.success("ok");
		}
		return GenericWebResult.error("200");
	}
	
	@RequestMapping(value = "/{userId}/update2", method = RequestMethod.GET)
	public GenericWebResult updateUserInfo1(HttpServletRequest request,
			@RequestParam("nickname") String name, @PathVariable("userId") Long userId) {
		//2.昵称
		if(Strings.isNullOrEmpty(name)){
			return GenericWebResult.error("-1").withData(ErrorMsgWrapper.error("datanull").withMsg("昵称不能为空"));
		}
		
		if (userService.updateNickname(userId, name)) {
			return GenericWebResult.success("ok");
		}
		return GenericWebResult.error("200");
	}
	
	@ExceptionHandler(BindException.class)
    public String validExceptionHandler(BindException e, WebRequest request, HttpServletResponse response) {
        List<FieldError> fieldErrors=e.getBindingResult().getFieldErrors();
        for (FieldError error:fieldErrors){
            log.error(error.getField()+":"+error.getDefaultMessage());
        }
        request.setAttribute("fieldErrors", fieldErrors, WebRequest.SCOPE_REQUEST);
        if(AjaxUtils.isAjaxRequest(request)){
        	Map<String, Object> vmap = new HashMap<>(); 
            String[] atrrNames=request.getAttributeNames(WebRequest.SCOPE_REQUEST);
            for(String attr:atrrNames){
                Object value=request.getAttribute(attr,WebRequest.SCOPE_REQUEST);
                if(value instanceof Serializable){
                	vmap.put(attr, value);
                }
            }
            new JsonResponse(response).with("status", "500").with("data", vmap);
            return null;
        }

        return "/validError";
    }
}
