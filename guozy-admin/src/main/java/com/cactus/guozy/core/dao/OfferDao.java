package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.Offer;
import com.cactus.guozy.core.domain.UserOffer;

public interface OfferDao {
	
	List<Offer> readAllOffers();
	
	List<UserOffer> readUserOffers(Long id);
	
	List<UserOffer> readUnusedOffers(Long id);
	
	int setUsed(Long userOfferId);
	
	UserOffer readUserOffer(Long id);

}
