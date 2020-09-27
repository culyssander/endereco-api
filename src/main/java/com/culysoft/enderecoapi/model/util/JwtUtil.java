package com.culysoft.enderecoapi.model.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.culysoft.enderecoapi.model.exception.NegocioException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
@PropertySource("classpath:application.properties")
public class JwtUtil {

	private final String SECRETEKEY = "spring.security.oauth2.resourceserver.jwt.public-key-location";
	
	@Autowired 
	private Environment environment;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.setSigningKey(Keys.hmacShaKeyFor(environment.getRequiredProperty(SECRETEKEY).getBytes()))
				.parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) throws NegocioException {
		try {
			return extractExpiration(token).before(new Date());
		}catch(JwtException e) {
			throw new NegocioException("token expirou: " + e.getMessage());
		}
	}
	
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}
	
	private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 1))
                .signWith(Keys.hmacShaKeyFor(environment.getRequiredProperty(SECRETEKEY).getBytes())).compact();
    }

	public Boolean validateToken(String token, UserDetails userDetails) throws NegocioException {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
