package com.cactus.guozy.api.security;

import org.springframework.security.core.AuthenticationException;

public class ApiTokenMalformedException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public ApiTokenMalformedException(String msg) {
		super(msg);
	}

}
