package com.cactus.guozy.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.common.exception.BusinessException;
import com.cactus.guozy.core.dao.PayMapDao;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderStatus;
import com.cactus.guozy.core.domain.PayMap;
import com.cactus.guozy.core.dto.PayRequestParam;
import com.cactus.guozy.core.pay.PayType;
import com.cactus.guozy.core.pay.PlatformType;
import com.cactus.guozy.core.pay.StrategyContext;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.PayRouteService;
import com.cactus.guozy.core.service.PricingService;

/**
 * 支付请求路由业务
 */
@Service("payRouteService")
public class PayRouteServiceImpl implements PayRouteService {
	
    private static Logger logger = LoggerFactory.getLogger(PayRouteServiceImpl.class);
    
    @Autowired
    private PayMapDao payMapDao;
    
    @Resource(name="pricingService")
    protected PricingService pricingService;
    
    @Resource(name="orderService")
    protected OrderService orderService;
    
    public PayType dealPayType(String onLineStyle, String browseType) {
        PayType payType = null;
        switch (onLineStyle) {
            case "alipay":
                switch (browseType) {
                    case "app":
                        payType = PayType.ALIPAY_APP;
                        break;
                    default:
                        payType = null;
                }
                break;
            case "wechat":
                switch (browseType) {
                    case "app":
                        payType = PayType.WECHAT_APP;
                        break;
                    default:
                        payType = null;
                }
                break;
            default:
                payType = null;
        }
        return payType;
    }

    private void savePayRecord(String payRequsetMsg, PayType payType, PayRequestParam payRequestParam,Order order) {
        PayMap payMap = new PayMap();
        payMap.setOrderId(order.getId());
        payMap.setOrderCode(order.getOrderNumber());
        PlatformType type = PlatformType.getPlatform(payType.value());
        payMap.setPlatform(type.value());
        payMap.setTempPayCode(order.getOrderNumber());
        payMap.setPayParams(payRequsetMsg);
        payMap.setRequestBiz(payRequestParam.getRequestBiz());
        payMapDao.insertSelective(payMap);
    }

    @Override
    public Map<String, Object> getPayRetMap(PayRequestParam payRequestParam) {
        Map<String, Object> retMap = assembleRetMap(payRequestParam);
        return retMap;
    }

    private Map<String, Object> assembleRetMap(PayRequestParam payRequestParam) {
    	Long orderId = payRequestParam.getOrderId();
    	if(orderId == null) {
    		throw new BusinessException("订单Id不能为空"); 
    	}
    	
    	Order order = orderService.findOrderById(orderId);
    	if(order == null) {
    		throw new BusinessException("订单" + orderId +"不存在");
    	}
    	
    	if(!order.getStatus().getType().equals(OrderStatus.SUBMITTED.getType())){
    		throw new BusinessException("订单状态异常. 只有已提交的订单可以支付");
    	}
    	
    	
    	if(order.getOrderNumber() == null) {
    		order.setOrderNumber(determineOrderNumber(order));
    		orderService.updateOrderNumber(order);
    	}
    	
    	order = pricingService.executePricing(order);
    	
        Map<String, Object> paramsToPass = new HashMap<>();
        paramsToPass.put("order", order);
        if (StringUtils.isNotBlank(payRequestParam.getRetUrl())) {
            paramsToPass.put("retUrl", payRequestParam.getRetUrl());
        }
        PayType payType = dealPayType(payRequestParam.getPlatformType(), payRequestParam.getBrowseType());
        StrategyContext context = new StrategyContext();
        String payRequsetMsg = context.generatePayParams(payType, paramsToPass);
        if(logger.isDebugEnabled()){
            logger.debug("订单code为{}的支付请求参数生成信息：{}", new Object[]{payRequestParam.getOrderNum(), payRequsetMsg});
        }
        savePayRecord(payRequsetMsg, payType, payRequestParam, order);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("payData", payRequsetMsg);
        retMap.put("payment", payRequestParam.getPlatformType());
        retMap.put("orderID", payRequestParam.getOrderId());
        retMap.put("message", "请去第三方平台支付");
        return retMap;
    }
    
	protected String determineOrderNumber(Order order) {
		return new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date()) + order.getId();
	}

}

