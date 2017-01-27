package com.cactus.guozy.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.common.utils.Maps;
import com.cactus.guozy.core.pay.PayPlatform;
import com.cactus.guozy.core.pay.PlatformType;
import com.cactus.guozy.core.service.IPayMapService;
import com.cactus.guozy.core.service.IWechatNotifyService;

/**
 * 微信通知业务
 */
@Service
public class WechatNotifyService implements IWechatNotifyService {

	private static Logger logger = LoggerFactory.getLogger(WechatNotifyService.class);
	@Autowired
	private IPayMapService payMapService;

	@Override
	public void wechatNotify(HttpServletRequest request, HttpServletResponse response) {
		String xml = getPostRequestBody(request);
		if (null != xml) {
			if (logger.isDebugEnabled()) {
				logger.debug("wechat notify xml:{}", xml);
			}
			Map<String, String> retParam = Maps.toMap(xml);
			if (null != retParam && retParam.size() > 0) {
				if (logger.isDebugEnabled()) {
					logger.debug("wechat xml to map,retParam:{}", retParam.toString());
				}
				if ("SUCCESS".equals(retParam.get("return_code"))) {
					payMapService.updatePayMapByPayCode((String) retParam.get("out_trade_no"), retParam.toString(),
							null, PlatformType.WECHAT, (String) retParam.get("transaction_id"),
							PayPlatform.WECHAT_APP.getCode());
					String toRet = "<xml>\n" + "  <return_code><![CDATA[SUCCESS]]></return_code>\n"
							+ "  <return_msg><![CDATA[OK]]></return_msg>\n" + "</xml>";
					try {
						response.getWriter().write(toRet);
					} catch (IOException e) {
						logger.error("response io error,ex:{}", e.getMessage());
						throw new RuntimeException(e);
					}
				} else {
					logger.error("微信交易失败，订单号：{}", retParam.get("out_trade_no"));
					String toRet = "<xml>\n" + "  <return_code><![CDATA[FAIL]]></return_code>\n"
							+ "  <return_msg><![CDATA[not success]]></return_msg>\n" + "</xml>";
					try {
						response.getWriter().write(toRet);
					} catch (IOException e) {
						logger.error("response io error,ex:{}", e.getMessage());
						throw new RuntimeException(e);
					}
				}
			}

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
				StringWriter stringWriter = new StringWriter();
				e.printStackTrace(new PrintWriter(stringWriter));
				logger.warn("failed to read request body, cause: {}", stringWriter.toString());
			}
			return sb.toString();
		}
		return "";
	}
}
