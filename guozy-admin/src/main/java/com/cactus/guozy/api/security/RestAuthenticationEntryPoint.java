package com.cactus.guozy.api.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.common.json.JsonResponse;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		new JsonResponse(response)
		.with("status", HttpServletResponse.SC_UNAUTHORIZED)
		.with("data", ErrorMsgWrapper.error("unauthorized").withMsg(authException.getMessage()))
		.done();
	}

}
