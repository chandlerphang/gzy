package com.cactus.guozy.core.service.offer;

import java.util.List;

import com.cactus.guozy.core.domain.Offer;
import com.cactus.guozy.core.domain.UserOffer;
import com.cactus.guozy.profile.domain.User;

public interface OfferService {
	
	List<Offer> findAllOffers();
	
	List<UserOffer> findUserOffers(Long userid);
	
	List<UserOffer> findUnusedOffers(Long userid);
	
	UserOffer findUserOffer(Long id);
	
	void setUsed(Long userOfferId);
	
	void addOffer(User user, Long offerId, int count);
	
}
