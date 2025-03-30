package com.dailycodework.dreamshops.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.dailycodework.dreamshops.security.user.ShopUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;


@Component
public class JwtUtils {
	@Value("${auth.token.jwtSecret}")
	private String jwtSecret;
	
	@Value("${auth.token.expirationInMils}")
	private int expirationTime;
	
	
	public String generateTokenForUser(Authentication authentication) {
		ShopUserDetails userPricipal= (ShopUserDetails) authentication.getPrincipal();
		List<String> roles= userPricipal.getAuthorities()
					.stream()
					.map(GrantedAuthority:: getAuthority).toList();
		return Jwts.builder()
				.setSubject(userPricipal.getEmail())
				.claim("id", userPricipal.getId())
				.claim("role", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime()+expirationTime))
				.signWith(key(),SignatureAlgorithm.HS256).compact();
				
	}
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public String getUserNameFromToken(String token) {
	return	Jwts.parserBuilder()
		.setSigningKey(key())
		.build()
		.parseClaimsJws(token)
		.getBody().getSubject();
		
	}
	public boolean validateToken(String token) {
        if (token == null) {
            return false; // Handle null token
        }
        
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            Logger.getLogger(JwtUtils.class.getName()).log(Level.INFO, "JWT token has expired", e);
            return false;
        } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            Logger.getLogger(JwtUtils.class.getName()).log(Level.SEVERE, "Error validating JWT token", e);
            return false;
        }
    }

}
