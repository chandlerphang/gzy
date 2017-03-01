package com.cactus.guozy.api.endpoint;

import static com.cactus.guozy.core.domain.validator.GenericValidator.checkPasswd;
import static com.cactus.guozy.core.domain.validator.GenericValidator.checkPhone;
import static com.cactus.guozy.core.domain.validator.GenericValidator.checkVCode;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cactus.guozy.api.security.TokenHandler;
import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.api.wrapper.UserWrapper;
import com.cactus.guozy.core.domain.validator.GenericValidator;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.service.GenericResponse;
import com.cactus.guozy.core.service.MsgPushService;
import com.cactus.guozy.core.service.VerifyCodeService;
import com.cactus.guozy.profile.domain.User;

@RestController
public class LoginEndpoint extends BaseEndpoint {

	@Autowired
	protected VerifyCodeService vcodeService;
	
	@Resource(name="getuiPushService")
	protected MsgPushService pushService;

	@RequestMapping(value = { "/login"}, method = RequestMethod.POST)
	public GenericWebResult login(
			@RequestParam("phone") String phone, 
			@RequestParam("passwd") String passwd) {
		if (!GenericValidator.checkPhone(phone) || !GenericValidator.checkPasswd(passwd)) {
			return GenericWebResult.PARAMETER_ERROR;
		}
		
		User user = userService.tryLogin(phone, passwd);
		UserWrapper wrapper = new UserWrapper();
		wrapper.wrapSummary(user);
		wrapper.setToken(TokenHandler.createTokenForUser(user.getId(), false));
		
		return GenericWebResult.success(wrapper);
	}
	
	@RequestMapping(value = { "/nopwdlogin"}, method = RequestMethod.POST)
	public GenericWebResult nopwdlogin(
			@RequestParam("phone") String phone, 
			@RequestParam("passwd") String passwd, 
			@RequestParam("vcode") String vcode) {
		if (!GenericValidator.checkPhone(phone) || !GenericValidator.checkPasswd(passwd)) {
			return GenericWebResult.PARAMETER_ERROR;
		}
		
		boolean isCodeRight = vcodeService.verifyCode(phone, vcode, VerifyCodeService.MODULE_NOPASSWD);
		if(isCodeRight) {
			User user = userService.findUserByPhone(phone);
			if(user != null) {
				userService.changePasswd(passwd, user.getId());
				return GenericWebResult.success(TokenHandler.createTokenForUser(user.getId(), false));
			}
			return GenericWebResult.error("010303").withData(ErrorMsgWrapper.error("userNotFound").withMsg("用户不存在"));
		} else {
			return GenericWebResult.error("010404").withData(ErrorMsgWrapper.error("vcodeError").withMsg("验证码错误"));
		}
	}
	
	@RequestMapping(value = { "/nopwdlogin/vcode"}, method = RequestMethod.POST)
	public GenericWebResult nopwdlogin_vcode(@RequestParam("phone") String phone) {
		if (!checkPhone(phone)) {
			return GenericWebResult.error("000101").withData(ErrorMsgWrapper.error("paramsError").withMsg("参数错误"));
		}
		
		GenericResponse resp = vcodeService.sendAndSaveCodeByPhone(phone, VerifyCodeService.MODULE_NOPASSWD);
		if(resp.hasErrors()) {
			return GenericWebResult.error("000102").withData(ErrorMsgWrapper.error("reqveryoften").withMsg("请求过频"));
		}
		return GenericWebResult.success("ok");
	}

	@RequestMapping(value = { "/resetpwd" }, method = RequestMethod.POST)
	public GenericWebResult resetPassword(@RequestParam("passwd") String passwd, @RequestParam("vcode") String vcode) {
		if (!checkPasswd(passwd) || !checkVCode(vcode)) {
			return GenericWebResult.error("000101").withData(ErrorMsgWrapper.error("paramsError").withMsg("参数错误"));
		}
		
		Long userId = getCurrentUserId();
		boolean isCodeRight = vcodeService.verifyCode(userId, vcode, VerifyCodeService.MODULE_RESETPWD);
		if(isCodeRight) {
			userService.changePasswd(passwd, getCurrentUserId());
			return GenericWebResult.success("ok");
		} else {
			return GenericWebResult.error("010404").withData(ErrorMsgWrapper.error("vcodeError").withMsg("验证码错误"));
		}
	}

	@RequestMapping(value = { "/resetpwd/vcode" }, method = RequestMethod.POST)
	public GenericWebResult vcode() {
		Long userId = getCurrentUserId();
		String phone = userService.getPhoneById(userId);
		String vcode = vcodeService.sendCodeByPhone(phone, VerifyCodeService.MODULE_RESETPWD);
		GenericResponse resp = vcodeService.saveCode(userId, vcode, VerifyCodeService.MODULE_RESETPWD);
		if(resp.hasErrors()) {
			return GenericWebResult.error("000102").withData(ErrorMsgWrapper.error("reqveryoften").withMsg("请求过频"));
		}
		
		return GenericWebResult.success("ok");
	}
}
