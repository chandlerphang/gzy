package com.cactus.guozy.core.service;

public interface VerifyCodeService {
	
	public final static String MODULE_REGISTER = "register";
	
	public final static String MODULE_RESETPWD = "resetpwd";
	
	public final static String MODULE_NOPASSWD = "nopasswd";

	GenericResponse sendAndSaveCodeByPhone(String phone, String moduleName);
	
	String sendCodeByPhone(String phone, String moduleName);
	
	GenericResponse saveCode(Object key, String code, String moduleName);
	
	boolean verifyCode(Object key, String code, String muduleName);
}
