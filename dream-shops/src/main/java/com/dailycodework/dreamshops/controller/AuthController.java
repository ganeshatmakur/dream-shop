package com.dailycodework.dreamshops.controller;

import  static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.request.LoginRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.response.JwtResponse;
import com.dailycodework.dreamshops.security.jwt.JwtUtils;
import com.dailycodework.dreamshops.security.user.ShopUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;
	
	
	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
		try {
			Authentication authenticate = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authenticate);
			String jwt=jwtUtils.generateTokenForUser(authenticate);
			ShopUserDetails userDetails= (ShopUserDetails)authenticate.getPrincipal();
			JwtResponse jwtResponse=new JwtResponse(userDetails.getId(), jwt);
			return ResponseEntity.ok(new ApiResponse("Login Successfull", jwtResponse));
		} catch (AuthenticationException e) {

			return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
		}
	}

}
