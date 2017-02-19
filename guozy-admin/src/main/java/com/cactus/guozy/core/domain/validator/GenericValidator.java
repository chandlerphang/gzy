package com.cactus.guozy.core.domain.validator;

import com.cactus.guozy.common.utils.Strings;

public class GenericValidator {
	
	/**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	
	public static boolean checkPhone(String phone) {
		if(Strings.isNullOrEmpty(phone)) {
			return false;
		}
		
		return phone.length() == 11;
	}
	
	public static boolean checkPasswd(String passwd) {
		if (Strings.isNullOrEmpty(passwd)) {
			return false;
		}

		if (passwd.length() < 6 || passwd.length() > 20) {
			return false;
		}

		return true;
	}

	public static boolean checkVCode(String vcode) {
		if (vcode == null || vcode.isEmpty()) {
			return false;
		}

		if (vcode.length() != 6) {
			return false;
		}

		return true;
	}

}
