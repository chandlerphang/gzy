package com.cactus.guozy.api.security;

import java.util.Collections;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class TokenAuthentication extends AbstractAuthenticationToken {
	
	private static final long serialVersionUID = 1712786265425544071L;
	
	private String token;

	public String getToken() {
		return token;
	}

	public TokenAuthentication(String token) {
		super(Collections.emptyList());
		this.token = token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}
	
}
