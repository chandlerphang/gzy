package com.cactus.guozy.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.common.utils.Maps;
import com.cactus.guozy.common.utils.Strings;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.pay.PayPlatform;
import com.cactus.guozy.core.pay.PlatformType;
import com.cactus.guozy.core.service.OrderLockManager;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.PayMapService;
import com.cactus.guozy.core.service.WechatNotifyService;
import com.google.common.base.Throwables;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信通知业务
 */
@Slf4j
@Service
public class WechatNotifyServiceImpl implements WechatNotifyService {

	@Autowired
	private PayMapService payMapService;
	
	@Autowired
	protected OrderService orderService;
	
	@Autowired
	protected WepaySupport wepaySupport;
	
	@Autowired
	protected OrderLockManager lockMgr;

	@Override
	public void wechatNotify(HttpServletRequest request, HttpServletResponse response) {
		String notifyXml = getPostRequestBody(request);
		if (Strings.isNullOrEmpty(notifyXml)) {
			toResponse(response, wepaySupport.notifyNotOk("body为空"));
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("wechat notify xml:{}", notifyXml);
		}
		Map<String, String> retParam = Maps.toMap(notifyXml);
		if (wepaySupport.verifySign(retParam)) {
			log.info("verify sign success: {}", retParam);
			
			Order order = new Order(Long.valueOf(retParam.get("out_trade_no")));
			Object lock = lockMgr.acquireLock(order);
			try {
				orderService.finishOrder(order);
				payMapService.updatePayMapByPayCode((String) retParam.get("out_trade_no"), retParam.toString(), null,
						PlatformType.WECHAT, (String) retParam.get("transaction_id"), PayPlatform.WECHAT_APP.getCode());
			} finally {
				lockMgr.releaseLock(lock);
			}
			
			toResponse(response, wepaySupport.notifyOk());
			return;
		} else {
			log.error("verify sign failed: {}", retParam);
			toResponse(response, wepaySupport.notifyNotOk("签名失败"));
			return;
		}

	}

	private void toResponse(HttpServletResponse response, String cnt) {
		try {
			response.getWriter().println(cnt);
		} catch (IOException e) {
			log.error("response IO error:{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static String getPostRequestBody(HttpServletRequest req) {
		if (req.getMethod().equals("POST")) {
			StringBuilder sb = new StringBuilder();
			try (BufferedReader br = req.getReader()) {
				char[] charBuffer = new char[128];
				int bytesRead;
				while ((bytesRead = br.read(charBuffer)) != -1) {
					sb.append(charBuffer, 0, bytesRead);
				}
			} catch (IOException e) {
				log.warn("failed to read request body, cause: {}", Throwables.getStackTraceAsString(e));
			}
			return sb.toString();
		}
		return "";
	}
}
