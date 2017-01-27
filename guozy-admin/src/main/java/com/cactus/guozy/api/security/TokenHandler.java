package com.cactus.guozy.api.security;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.cactus.guozy.profile.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public abstract class TokenHandler {

	// NVcSnf*K4iRX$62!j8OvTBe!@@jlrmt5
    private final static String secret = "TlZjU25mKks0aVJYJDYyIWo4T3ZUQmUhQEBqbHJtdDU=";
    
    public static User parse(String token) {
    	JwtParser parser = Jwts.parser().setSigningKey(secret);
    	Jws<Claims> claims = null;
    	try {
    		claims = parser.parseClaimsJws(token);
    	} catch(ExpiredJwtException | SignatureException | MalformedJwtException e) {
    	}
    	
    	if(claims == null) {
    		return null;
    	}
    	
        Long id = null;
        try{
        	id = Long.parseLong(claims.getBody().getSubject());
        } catch (NumberFormatException e) {}
        
        return id==null?null:new User(id);
    }

    public static String createTokenForUser(User customer) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TimeUnit.DAYS.toMillis(365l));
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(customer.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
