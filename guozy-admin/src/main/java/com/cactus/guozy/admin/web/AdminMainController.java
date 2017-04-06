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

import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.api.wrapper.FruitCSWrapper;
import com.cactus.guozy.api.wrapper.GoodsWrapper;
import com.cactus.guozy.common.PrintOrder;
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
import com.cactus.guozy.core.service.offer.OfferService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AdminMainController extends AbstractAdminController {

	@Resource(name = "fileService")
	protected FileService fileService;

	@Resource(name = "assetStorageService")
	protected AssetStorageService ass;

	@Resource(name = "assetService")
	protected AssetService assetService;

	@Resource(name = "appSettingService")
	protected AppSettingService appSettingService;

	@Resource(name = "catalogService")
	protected CatalogService catalogService;

	@Resource(name = "orderService")
	protected OrderService orderService;

	@Resource(name = "offerService")
	protected OfferService offerService;

	@RequestMapping(value = { "/appnotification" }, method = RequestMethod.GET)
	public String appnotification(HttpServletResponse resp, Model model) {
		resp.setHeader("x-frame-options", "sameorigin");
		setModelAttributes(model, "tuisong");
		return "appnotification";
	}

	@RequestMapping(value = { "/appsettings" }, method = RequestMethod.GET)
	public String appsettings(Model model) {
		model.addAttribute("aboutus", appSettingService.findAboutusUrl());
		List<FruitCSWrapper> wrappers = new ArrayList<>();
		List<FruitCommonSense> fcs = appSettingService.findAllFruitCommonSense();
		for (FruitCommonSense tmp : fcs) {
			wrappers.add(FruitCSWrapper.wrapDetail(tmp));
		}
		model.addAttribute("fruits", wrappers);

		setModelAttributes(model, "appsettings");
		return "appsettings";
	}

	@RequestMapping(value = { "/appsettings/aboutus" }, method = RequestMethod.POST)
	public void aboutus(@RequestParam("url") String url, HttpServletResponse resp) {
		appSettingService.saveAboutusUrl(url);
		new JsonResponse(resp).with("status", "200").with("data", "ok").done();
	}

	@RequestMapping(value = { "/appsettings/fruitcs" }, method = RequestMethod.POST)
	public void fruitcs(@RequestParam("file") MultipartFile file, @Valid FruitCSWrapper fruitcs,
			HttpServletResponse resp) {

		Map<String, String> properties = new HashMap<>();
		properties.put("module", "fruitcs");
		properties.put("resourceId", RandomStringUtils.randomNumeric(6));

		Asset asset = assetService.createAssetFromFile(file, properties);
		if (asset == null) {
			new JsonResponse(resp).with("error", "服务器内部错误").done();
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

	@RequestMapping(value = { "/category" }, method = RequestMethod.GET)
	public String category(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		List<List<Category>> categories = new ArrayList<>();
		for (int i = 0; i < shops.size(); i++) {
			categories.add(catalogService.findCategories(shops.get(i).getId()));
		}
		model.addAttribute("shops", shops);
		model.addAttribute("categories", categories);
		setModelAttributes(model, "leimuguanli");
		return "category";
	}

	@RequestMapping(value = { "/shop" }, method = RequestMethod.GET)
	public String shop(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);

		setModelAttributes(model, "dianpuguanli");
		return "shop";
	}

	@RequestMapping(value = "/shopAdd", method = RequestMethod.POST)
	public void shopAddOrUpdate(HttpServletResponse response, @RequestParam("id") String id,
			@RequestParam("name") String name, @RequestParam("manager") String manager,
			@RequestParam("address") String address, @RequestParam("opentime") String opentime,
			@RequestParam("closetime") String closetime, @RequestParam("distance") String distance,
			@RequestParam("shipprice") String shipprice, @RequestParam("password") String password,
			@RequestParam("password2") String password2,@RequestParam("action") String action) {

		if ( Strings.isNullOrEmpty(name) || Strings.isNullOrEmpty(manager) || Strings.isNullOrEmpty(address)
				|| Strings.isNullOrEmpty(opentime) || Strings.isNullOrEmpty(closetime) || Strings.isNullOrEmpty(distance)
				|| Strings.isNullOrEmpty(shipprice) || Strings.isNullOrEmpty(password)|| Strings.isNullOrEmpty(password2)|| Strings.isNullOrEmpty(action)) {
			new JsonResponse(response).with("status", 500).with("data", "输入信息不完善，请完善后提交").done();
		}
		
		//判断id是否存在选择插入或更新
		new JsonResponse(response).with("status", 200).with("data", "ok").done();
	}
	
	@RequestMapping(value = "/shopDelete", method = RequestMethod.POST)
	public void shopDelete(HttpServletResponse response, @RequestParam("id") String id) {

		if (Strings.isNullOrEmpty(id)) {
			new JsonResponse(response).with("status", 500).with("data", "输入信息不完善，请完善后提交").done();
		}
		//删除
		new JsonResponse(response).with("status", 200).with("data", "ok").done();
	}

	@RequestMapping(value = { "/saler" }, method = RequestMethod.GET)
	public String saler(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);
		List<Saler> salers = catalogService.findSalersByShopId(1l);
		model.addAttribute("salers", salers);

		setModelAttributes(model, "dianyuanguanli");
		return "saler";
	}

	@RequestMapping(value = { "/ordershop" }, method = RequestMethod.GET)
	public String ordershop(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);
		List<Saler> salers = catalogService.findSalersByShopId(1l);
		model.addAttribute("salers", salers);
		setModelAttributes(model, "dingdanguanli");
		return "ordershop";
	}

	@RequestMapping(value = { "/salerlist" }, method = RequestMethod.GET)
	public String salerlist(@RequestParam("sid") String sid, HttpServletResponse resp, Model model) {
		resp.setHeader("x-frame-options", "sameorigin");
		List<Saler> salers = catalogService.findSalersByShopId(Long.parseLong(sid));
		model.addAttribute("salers", salers);
		model.addAttribute("sid", sid);
		return "salerlist";
	}

	@RequestMapping(value = { "/orderlist" }, method = RequestMethod.GET)
	public String orderlist(
			@RequestParam("shopId") Long shopId, 
			@RequestParam(name="perNum", defaultValue="8") int perNum,
			@RequestParam(name="pageNum", defaultValue="1") int pageNum, 
			HttpServletResponse resp, Model model) {
		List<Order> orders = orderService.readOrdersForShopNotPROCESS(shopId, perNum, pageNum);
		model.addAttribute("orders", orders);
		model.addAttribute("sid", shopId);
		model.addAttribute("index", (pageNum - 1) * perNum);
		return "orderlist";
	}

	@RequestMapping(value = { "/order" }, method = RequestMethod.GET)
	public String order(@RequestParam("ordId") String orderId, HttpServletResponse resp, Model model) {
		resp.setHeader("x-frame-options", "sameorigin");
		Order order = orderService.findOrderById(Long.parseLong(orderId));
		model.addAttribute("order", order);
		return "order";
	}

	@RequestMapping(value = { "/goods" }, method = RequestMethod.GET)
	public String goods(Model model) {
		List<Shop> shops = catalogService.findAllShops();
		model.addAttribute("shops", shops);

		setModelAttributes(model, "shangpinguanli");
		return "goods";
	}
	
	@RequestMapping(value = {"/goods"}, method = RequestMethod.POST)
	public void saveGoods(
			@RequestParam(name="gpic", required=false) MultipartFile file,
			@RequestParam(name="cateId", required=false) Long categoryId,
			@Valid GoodsWrapper goods,
			HttpServletResponse resp) {
		Goods oldg = null;
		if(goods.getId() == null) {
			// 新增商品，需提供该商品所在分类
			if(categoryId == null) {
				// 异常
				throw new RuntimeException();
			}
			oldg = new Goods();
		} else {
			oldg = catalogService.findGoodsById(goods.getId());
		}
		
		if(file != null) {
			Map<String, String> properties = new HashMap<>();
			properties.put("module", "goods");
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
			
			oldg.setPic(asset.getUrl());
		}
		
		oldg.setName(goods.getName());
		if(goods.getNeedSaler() != null) {
			oldg.setNeedSaler(goods.getNeedSaler());
		}
		oldg.setPrice(goods.getPrice());
		
		if(oldg.getId() == null) {
			Category category = new Category();
			category.setId(categoryId);
			catalogService.addGoodsToCategory(oldg, category);
		} else {
			catalogService.saveGoods(oldg);
		}
		
		new JsonResponse(resp).with("status", "200").with("data", "ok").done();
	}
	
	@RequestMapping(value = {"/goods/{}"}, method = RequestMethod.DELETE)
	public void nosale(
			@RequestParam(name="cateId", required=false) Long categoryId,
			@Valid GoodsWrapper goods,
			HttpServletResponse resp) {

		new JsonResponse(resp).with("status", "200").with("data", "ok").done();
	}
	
	
	
	@RequestMapping(value = {"/goodlist"}, method = RequestMethod.GET)
	public String goodlist(HttpServletResponse resp,Model model,@RequestParam("cateId") Long categoryId) {
		List<Goods> goods = catalogService.findAllGoods(categoryId);
		List<GoodsWrapper> wrappers = new ArrayList<GoodsWrapper>();
		for (Goods goodsItem : goods) {
			GoodsWrapper wrapper = new GoodsWrapper();
			wrapper.wrapDetails(goodsItem);
			wrappers.add(wrapper);
		}
		model.addAttribute("goods", wrappers);
		return "goodlist";
	}
	
	@RequestMapping(value = {"/category"}, method = RequestMethod.POST)
	public String categorys(@RequestParam("shopId") Long shopId,
			Model model) {
		List<Category> categories = catalogService.findCategories(shopId);
		model.addAttribute("categories", categories);
		return "category_list";
	}
	
	@RequestMapping(value = {"/shops"}, method = RequestMethod.POST)
	public void shops(
			HttpServletResponse resp) {
		List<Shop> shops = catalogService.findAllShops();
		new JsonResponse(resp).with("status", "200").with("data", shops).done();
	}

	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public String user(Model model) {
		model.addAttribute("users", userService.findAllUsers());

		setModelAttributes(model, "yonghuguanli");
		return "user";
	}
	@RequestMapping(value = "/printOrder", method = RequestMethod.POST)
	public void printOrder(HttpServletResponse resp, @RequestParam("id") Long orderId) {
		Order order=orderService.findOrderById(orderId);
		PrintOrder printer=new PrintOrder();
		printer.print("果之源", order);
	
		new JsonResponse(resp).with("status", "200").with("data", "ok").done();
	}

	@RequestMapping(value = "/{userId}/update", method = RequestMethod.GET)
	public GenericWebResult updateUserInfo(HttpServletRequest request, @RequestParam("nickname") String name,
			@PathVariable("userId") Long userId) {

		// // 1、頭像
		// if (file != null) {
		// Map<String, String> properties = new HashMap<>();
		// properties.put("module", "profile");
		// properties.put("resourceId", userId.toString());
		//
		// LOG.debug("开始头像资源存储.");
		// Asset asset = assetService.createAssetFromFile(file, properties);
		//
		// if (asset == null) {
		// return
		// GenericWebResult.error("500").withData(ErrorMsgWrapper.error("unknownError").withMsg("服务器内部错误"));
		// }
		//
		// try {
		// ass.storeAssetFile(file, asset);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// userService.updateAvatarUrl(userId, asset.getUrl());
		// }
		// LOG.debug("结束头像资源存储.");
		// 2.昵称
		if (Strings.isNullOrEmpty(name)) {
			return GenericWebResult.error("-1").withData(ErrorMsgWrapper.error("datanull").withMsg("昵称不能为空"));
		}

		if (userService.updateNickname(userId, name)) {
			return GenericWebResult.success("ok");
		}
		return GenericWebResult.error("200");
	}

	@RequestMapping(value = "/{userId}/update2", method = RequestMethod.GET)
	public GenericWebResult updateUserInfo1(HttpServletRequest request, @RequestParam("nickname") String name,
			@PathVariable("userId") Long userId) {
		// 2.昵称
		if (Strings.isNullOrEmpty(name)) {
			return GenericWebResult.error("-1").withData(ErrorMsgWrapper.error("datanull").withMsg("昵称不能为空"));
		}

		if (userService.updateNickname(userId, name)) {
			return GenericWebResult.success("ok");
		}
		return GenericWebResult.error("200");
	}

	
	
	@ExceptionHandler(BindException.class)
	public String validExceptionHandler(BindException e, WebRequest request, HttpServletResponse response) {
		List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
		for (FieldError error : fieldErrors) {
			log.error(error.getField() + ":" + error.getDefaultMessage());
		}
		request.setAttribute("fieldErrors", fieldErrors, WebRequest.SCOPE_REQUEST);
		if (AjaxUtils.isAjaxRequest(request)) {
			Map<String, Object> vmap = new HashMap<>();
			String[] atrrNames = request.getAttributeNames(WebRequest.SCOPE_REQUEST);
			for (String attr : atrrNames) {
				Object value = request.getAttribute(attr, WebRequest.SCOPE_REQUEST);
				if (value instanceof Serializable) {
					vmap.put(attr, value);
				}
			}
			new JsonResponse(response).with("status", "500").with("data", vmap);
			return null;
		}

		return "/validError";
	}

}
