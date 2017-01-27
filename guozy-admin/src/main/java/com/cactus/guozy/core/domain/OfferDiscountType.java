package com.cactus.guozy.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class OfferDiscountType {
	
	private static final Map<String, OfferDiscountType> TYPES = new LinkedHashMap<String, OfferDiscountType>();

    public static final OfferDiscountType PERCENT_OFF = new OfferDiscountType("PERCENT_OFF", "百分比型");
    public static final OfferDiscountType AMOUNT_OFF = new OfferDiscountType("AMOUNT_OFF", "数量型");
    public static final OfferDiscountType FIX_PRICE = new OfferDiscountType("FIX_PRICE", "直接指定价格");

    public static OfferDiscountType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String type;
    private String friendlyType;

    public OfferDiscountType() {
        //do nothing
    }

    public OfferDiscountType(final String type, final String friendlyType) {
        this.friendlyType = friendlyType;
        setType(type);
    }

    public void setType(final String type) {
        this.type = type;
        if (!TYPES.containsKey(type)) {
            TYPES.put(type, this);
        }
    }

    public String getType() {
        return type;
    }

    public String getFriendlyType() {
        return friendlyType;
    }

}
