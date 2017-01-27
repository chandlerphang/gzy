package com.cactus.guozy.api;

public class WebServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static final String UNKNOWN_ERROR = "unknownError";
    public static final String NOT_FOUND = "notFound";
    public static final String CONTENT_TYPE_NOT_SUPPORTED = "contentTypeNotSupported";
    public static final String QUERY_PARAMETER_NOT_PRESENT = "queryParameterNotPresent";
    public static final String METHOD_NOT_SUPPORTED = "methodNotSupported";
    public static final String PRODUCT_NOT_FOUND = "productNotFound";
    public static final String CATEGORY_NOT_FOUND = "categoryNotFound";
    public static final String SEARCH_ERROR = "errorExecutingSearch";
    public static final String SEARCH_QUERY_EMPTY = "searchQueryEmpty";
    public static final String SEARCH_QUERY_MALFORMED = "searchQueryMalformed";
    public static final String INVALID_CATEGORY_ID = "invalidCategoryId";
    public static final String CART_NOT_FOUND = "cartNotFound";
    public static final String CART_ITEM_NOT_FOUND = "cartItemNotFound";
    public static final String CART_PRICING_ERROR = "cartPricingError";
    public static final String UPDATE_CART_ERROR = "updateCartError";
    public static final String PROMO_CODE_MAX_USAGES = "promoCodeMaxUsages";
    public static final String PROMO_CODE_INVALID = "promoCodeInvalid";
    public static final String PROMO_CODE_EXPIRED = "promoCodeExpired";
    public static final String PROMO_CODE_ALREADY_ADDED = "promoCodeAlreadyAdded";
    public static final String FULFILLMENT_GROUP_NOT_FOUND = "fulfillmentGroupNotFound";
    public static final String FULFILLMENT_OPTION_NOT_FOUND = "fulfillmentOptionNotFound";
    public static final String USER_NOT_FOUND = "userNotFound";
    public static final String CHECKOUT_PROCESSING_ERROR = "checkoutProcessingError";
    
    protected int status = 500;
    
    protected String key = null;
    
    protected String msg = null;

    public WebServiceException(int status, String key, String message, Throwable cause) {
        super(cause);
        this.status = status;
        this.key = key;
        msg = message;
    }

	public static WebServiceException build(int status) {
        return build(status, null, null, null);
    }

    public static WebServiceException build(int status, Throwable t) {
        return build(status, null, null, t);
    }

    public static WebServiceException build(int status, String key, String message) {
        return build(status, key, message, null);
    }

    public static WebServiceException build(int status, String key, String message, Throwable cause) {
        return new WebServiceException(status, key, message, cause);
    }

    public int getHttpStatusCode() {
        return status;
    }

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public WebServiceException withStatus(int status) {
		this.status = status;
		return this;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public WebServiceException withKey(String key) {
		this.key = key;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

    public WebServiceException withMsg(String msg) {
    	this.msg = msg;
    	return this;
    }
}
