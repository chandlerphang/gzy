package com.cactus.guozy.api.security;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.cactus.guozy.core.domain.Saler;
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
    
    public static Object parse(String token) {
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
        
        if(id == null || claims.getBody().get("role") == null) {
        	return null;
        }
        
        if(claims.getBody().get("role").equals("saler")) {
        	Saler saler = new Saler();
        	saler.setId(id);
        	return saler;
        } else {
        	return new User(id);
        }
    }

    public static String createTokenForUser(Long id, boolean isSaler) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + TimeUnit.DAYS.toMillis(365l));
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(id.toString())
                .setIssuedAt(now)
                .setExpiration(expiration)
                .claim("role", isSaler ? "saler" : "customer")
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
