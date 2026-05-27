package com.finTracker.utility;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;

	public String generateToken(String email) {
		return Jwts.builder()
				   .subject(email)
				   .issuedAt(new Date())
				   .expiration(new Date(System.currentTimeMillis() + expiration))
				   .signWith(getSigningkey())
				   .compact();
	}
	
	public SecretKey getSigningkey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	
	public Claims extractAllClaims(String token) {
		return Jwts.parser()
				   .verifyWith(getSigningkey())
				   .build()
				   .parseSignedClaims(token)
				   .getPayload();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	public boolean validateToken(String token, String email) {
		String extractedEmail = extractEmail(token);
		return extractedEmail.equals(email) && !isTokenExpired(token);
	}

}
