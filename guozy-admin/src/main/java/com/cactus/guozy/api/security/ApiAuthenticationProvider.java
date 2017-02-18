package com.cactus.guozy.api.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class ApiAuthenticationProvider implements AuthenticationProvider {
    @Override
    public boolean supports(Class<?> authentication) {
        return (TokenAuthentication.class.isAssignableFrom(authentication));
    }

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		TokenAuthentication jwtAuthenticationToken = (TokenAuthentication) authentication;
        String token = jwtAuthenticationToken.getToken();

        Object parsedUser = TokenHandler.parse(token);
        if (parsedUser == null) {
            throw new ApiTokenMalformedException("User token is not valid");
        }

        //List<GrantedAuthority> authorityList = Collections.emptyList();//AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getRole());
        jwtAuthenticationToken.setDetails(parsedUser);
        jwtAuthenticationToken.setAuthenticated(true);
		return jwtAuthenticationToken;
	}
}
