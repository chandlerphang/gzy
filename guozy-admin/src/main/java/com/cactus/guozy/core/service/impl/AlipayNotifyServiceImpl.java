package com.cactus.guozy.core.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.pay.PayPlatform;
import com.cactus.guozy.core.pay.PlatformType;
import com.cactus.guozy.core.pay.alipay.AlipayField;
import com.cactus.guozy.core.pay.alipay.AlipayFields;
import com.cactus.guozy.core.pay.alipay.TradeStatus;
import com.cactus.guozy.core.service.AlipayNotifyService;
import com.cactus.guozy.core.service.OrderLockManager;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.PayMapService;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝通知业务
 */
@Slf4j
@Service
public class AlipayNotifyServiceImpl implements AlipayNotifyService {

    @Autowired
    private PayMapService payMapService;
    
    @Autowired
    private AlipaySupport alipaySupport;
    
    @Autowired
	protected OrderService orderService;
    
	@Autowired
	protected OrderLockManager lockMgr;
    
    @Override
    public void alipayNotifyMainApp(HttpServletRequest request, HttpServletResponse response) {
    	
    	Map<String, String> notifyParams = new HashMap<>();
        for (AlipayField f : AlipayFields.APP_PAY_NOTIFY){
        	String value = request.getParameter(f.field());
        	if(!Strings.isNullOrEmpty(value)) {
        		notifyParams.put(f.field(), value);
        	}
        }
        
        log.info("backend notify params: {}", notifyParams);
        if (!alipaySupport.notifyVerify(notifyParams)){
            log.error("backend sign verify failed");
            toResponse(response, "FAIL");
            return;
        }
        boolean vflag = true;
        Long orderId = null;
        BigDecimal total = null;
        try {
        	orderId = Long.valueOf(notifyParams.get("out_trade_no"));
        	total = new BigDecimal(notifyParams.get("total_amount"));
        } catch (NumberFormatException e) { 
        	vflag = false;
        }
       
        if(!vflag) {
        	toResponse(response, "FAIL");
            return;
        }
        
        Order order = orderService.findOrderById(orderId);
        if(order == null || !order.getTotal().equals(total)) {
        	toResponse(response, "FAIL");
            return;
        }
        
        String seller_id = notifyParams.get("seller_id");
        String appid = notifyParams.get("app_id");
        if(!alipaySupport.getAlipay().getAppId().equals(appid)
        		|| !alipaySupport.getAlipay().getSellerId().equals(seller_id) ) {
        	toResponse(response, "FAIL");
            return;
        }
        
        String tradeStatus = notifyParams.get(AlipayField.TRADE_STATUS.field());
        if (TradeStatus.TRADE_FINISHED.value().equals(tradeStatus)
                || TradeStatus.TRADE_SUCCESS.value().equals(tradeStatus)){
        	Object lock = lockMgr.acquireLock(order);
    		try {
    			orderService.finishOrder(order);
    			payMapService.updatePayMapByPayCode((String) notifyParams.get("out_trade_no"), notifyParams.toString(), null,
    					PlatformType.TB, (String) notifyParams.get("trade_no"), PayPlatform.ALIPAY_COMMON.getCode());
    		} finally {
    			lockMgr.releaseLock(lock);
    		}

            log.info("backend notify success");
            toResponse(response, "SUCCESS");
            return;
        }
        
        toResponse(response, "FAIL");
        return;
    }
    
    private void toResponse(HttpServletResponse response, String cnt) {
		try {
			response.getWriter().println(cnt);
		} catch (IOException e) {
			log.error("response IO error:{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
