package com.cactus.guozy.core.domain.validator;

public class GenericValidator {
	
	public static boolean checkPhone(String phone) {
		if (phone == null || phone.isEmpty()) {
			return false;
		}

		if (phone.length() != 11) {
			return false;
		}

		if (!phone.matches("[0-9]{11}")) {
			return false;
		}

		return true;
	}
	
	public static boolean checkPasswd(String passwd) {
		if (passwd == null || passwd.isEmpty()) {
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
