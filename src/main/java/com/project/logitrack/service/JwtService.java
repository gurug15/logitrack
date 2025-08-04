package com.project.logitrack.service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.project.logitrack.Entity.User;
import com.project.logitrack.Entity.UserPrinciple;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	
	@Value("${jwt.secret.key}") // Injects the key from application.properties
	private String secretKey;
	
public String generateToken(UserDetails userDetails) {
        
        Map<String, Object> claims = new HashMap<>();

        if (userDetails instanceof UserPrinciple) {
            User user = ((UserPrinciple) userDetails).getUser(); 

            // Add the user's role as a claim
            if (user.getRoleId() != null) {
                claims.put("role", user.getRoleId().getRoleName());
            }

            // Add the user's logistic center ID as a claim
            if (user.getLogisticCenterId() != null) {
                claims.put("logisticCenterId", user.getLogisticCenterId().getId());
            }
        }

        return Jwts.builder()
                .claims(claims) // Use the new claims method
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                
                // Set expiration to 100 years from now for "eternity"
                // IMPORTANT: This is not recommended for a real production environment.
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365 * 100))
                
                .signWith(getKey()) // Simplified from .and().signWith()
                .compact();
	}
	
	
	public SecretKey getKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}


	public String extractUserName(String token) {

		return extractClaims(token, Claims::getSubject);
	}

	
	
	private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	
	}


	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	
	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}
}
