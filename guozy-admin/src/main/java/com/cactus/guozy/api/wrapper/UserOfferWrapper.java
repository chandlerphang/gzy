package com.cactus.guozy.api.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.core.domain.UserOffer;

@XmlRootElement(name = "useroffer")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class UserOfferWrapper {

	@XmlElement
	private Long id;
	
	@XmlElement
	private OfferWrapper offer;
	
	@XmlElement
	private boolean isUsed;
	
	public void wrapDetails(UserOffer userOffer) {
		wrapSummary(userOffer);
	}

	public void wrapSummary(UserOffer userOffer) {
		id = userOffer.getId();
		isUsed = userOffer.isUsed();
		offer = new OfferWrapper();
		offer.wrapSummary(userOffer.getOffer());
	}
	
	public UserOffer unwrap() {
		UserOffer userOffer = new UserOffer();
		userOffer.setId(id);
		userOffer.setUsed(isUsed);
		userOffer.setOffer(offer.upwrap());
		return userOffer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public OfferWrapper getOffer() {
		return offer;
	}

	public void setOffer(OfferWrapper offer) {
		this.offer = offer;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

}
