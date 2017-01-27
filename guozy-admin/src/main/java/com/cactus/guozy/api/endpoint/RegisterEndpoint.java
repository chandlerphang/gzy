package com.cactus.guozy.api.endpoint;

import static com.cactus.guozy.core.domain.validator.GenericValidator.checkPasswd;
import static com.cactus.guozy.core.domain.validator.GenericValidator.checkPhone;
import static com.cactus.guozy.core.domain.validator.GenericValidator.checkVCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.service.GenericResponse;
import com.cactus.guozy.core.service.VerifyCodeService;
import com.cactus.guozy.profile.domain.User;
import com.cactus.guozy.profile.service.UserService;

@RestController
@RequestMapping("/register")
public class RegisterEndpoint {
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected VerifyCodeService vcodeService;
	
	@RequestMapping(value={"", "/"}, method = RequestMethod.POST)
	public GenericWebResult processRegister(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("phone") String phone,
			@RequestParam("passwd") String passwd,
			@RequestParam("vcode") String vcode) {
		
		if (!checkPhone(phone) || !checkPasswd(passwd) || !checkVCode(vcode)) {
			return GenericWebResult.error("000101").withData(ErrorMsgWrapper.error("paramsError").withMsg("参数错误"));
		}
		
		boolean isCodeRight = vcodeService.verifyCode(phone, vcode, VerifyCodeService.MODULE_REGISTER);
		if(isCodeRight) {
			User user = userService.registerUser(phone, passwd);
			if(user == null) {
				return GenericWebResult.error("010203").withData(ErrorMsgWrapper.error("phoneExisted").withMsg("用户已存在"));
			} else {
				return GenericWebResult.success("ok");
			}
		} else {
			return GenericWebResult.error("010204").withData(ErrorMsgWrapper.error("vcodeError").withMsg("验证码错误"));
		}
	}
	
	@RequestMapping(value="/vcode", method = RequestMethod.POST)
	public GenericWebResult vcode(@RequestParam("phone") String phone) {
		if (!checkPhone(phone)) {
			return GenericWebResult.error("000101").withData(ErrorMsgWrapper.error("paramsError").withMsg("参数错误"));
		}
		
		GenericResponse resp = vcodeService.sendAndSaveCodeByPhone(phone, VerifyCodeService.MODULE_REGISTER);
		if(resp.hasErrors()) {
			return GenericWebResult.error("000102").withData(ErrorMsgWrapper.error("reqveryoften").withMsg("请求过频"));
		}
		
		return GenericWebResult.success("ok");
	}
	
}
