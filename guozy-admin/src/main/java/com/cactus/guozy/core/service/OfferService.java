package com.cactus.guozy.core.service;

import java.util.List;

import com.cactus.guozy.core.domain.Offer;
import com.cactus.guozy.core.domain.UserOffer;

public interface OfferService {
	
	List<Offer> findAllOffers();
	
	List<UserOffer> findUserOffers(Long userid);
	
	List<UserOffer> findUnusedOffers(Long userid);
	
	UserOffer findUserOffer(Long id);
	
	void setUsed(Long userOfferId);
	
}
