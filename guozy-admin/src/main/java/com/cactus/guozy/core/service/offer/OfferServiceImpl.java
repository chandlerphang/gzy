package com.cactus.guozy.core.service.offer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.core.dao.OfferDao;
import com.cactus.guozy.core.dao.UserOfferDao;
import com.cactus.guozy.core.domain.Offer;
import com.cactus.guozy.core.domain.UserOffer;
import com.cactus.guozy.profile.domain.User;

@Service("offerService")
public class OfferServiceImpl implements OfferService {
	
	@Autowired
	protected OfferDao offerDao;
	
	@Autowired
	protected UserOfferDao userOfferDao;

	@Override
	public List<Offer> findAllOffers() {
		return offerDao.readAllOffers();
	}

	@Override
	public List<UserOffer> findUserOffers(Long userid) {
		return offerDao.readUserOffers(userid);
	}

	@Override
	public List<UserOffer> findUnusedOffers(Long userid) {
		return offerDao.readUnusedOffers(userid);
	}

	@Override
	@Transactional
	public void setUsed(Long userOfferId) {
		offerDao.setUsed(userOfferId);
	}

	@Override
	public UserOffer findUserOffer(Long id) {
		return offerDao.readUserOffer(id);
	}

	@Override
	@Transactional
	public void addOffer(User user, Long offerId, int count) {
		UserOffer userOffer = UserOffer.builder()
		.isUsed(false)
		.offerId(offerId)
		.userId(user.getId())
		.build();
		
		for(int i=0; i<count; i++) {
			userOfferDao.insert(userOffer);
			userOffer.setId(null);
		}
	}

}
