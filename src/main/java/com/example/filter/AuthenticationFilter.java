package com.example.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		log.info("attemptAuthentication username: {}", username);
		log.info("attemptAuthentication password: {}", password);

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		log.info("attemptAuthentication token: {}", token);

		return authenticationManager.authenticate(token);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		log.info("successfulAuthentication");
		
		User user = (User) auth.getPrincipal();
		
		Algorithm algorithm = Algorithm.HMAC256("secret123");
				
		String accessToken = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60))
				.withIssuer(request.getRequestURL().toString())
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algorithm);
		
		String refreshToken = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60))
				.withIssuer(request.getRequestURL().toString()).sign(algorithm);
		
		log.info("successfulAuthentication accessToken: {}", accessToken);
		log.info("successfulAuthentication refreshToken: {}", refreshToken);

		Map<String, String> tokens = new HashMap<String, String>();
		tokens.put("access_token", accessToken);
		tokens.put("refresh_token", refreshToken);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);		
	}
}
