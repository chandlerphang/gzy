package com.cactus.guozy.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.core.dao.OfferDao;
import com.cactus.guozy.core.domain.Offer;
import com.cactus.guozy.core.domain.UserOffer;
import com.cactus.guozy.core.service.OfferService;

@Service("offerService")
public class OfferServiceImpl implements OfferService {
	
	@Autowired
	protected OfferDao offerDao;

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
	
}
