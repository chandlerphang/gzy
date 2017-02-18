package com.cactus.guozy.api.endpoint;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cactus.guozy.core.domain.UserOffer;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.service.offer.OfferService;

@RestController
@RequestMapping("/offers")
public class OfferEndpoint {
	@Resource(name="offerService")
	protected OfferService offerService;
	
	@RequestMapping(value={"/", ""}, method = RequestMethod.GET)
	public GenericWebResult getOffersForUser(HttpServletRequest request,
			@RequestParam("uid") Long uid) {
		List<UserOffer> useroffers = offerService.findUserOffers(uid);
		return GenericWebResult.success(useroffers);
	}
}
