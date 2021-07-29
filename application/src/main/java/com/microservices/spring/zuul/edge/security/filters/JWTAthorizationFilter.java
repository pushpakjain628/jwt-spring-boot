package com.microservices.spring.zuul.edge.security.filters;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;import javax.ws.rs.core.SecurityContext;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.microservices.spring.zuul.edge.security.constants.SecurityConstant.OPTIONS_HTTP_METHOD;

import static com.microservices.spring.zuul.edge.security.constants.SecurityConstant.TOKEN_PREFIX;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;

import com.microservices.spring.zuul.edge.security.utility.JWTTokenProvider;


@Component
public class JWTAthorizationFilter extends OncePerRequestFilter{

	private JWTTokenProvider jwtTokenProvider;

	public JWTAthorizationFilter(JWTTokenProvider jwtProvider) {
		this.jwtTokenProvider=jwtProvider;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(request.getMethod().equalsIgnoreCase(OPTIONS_HTTP_METHOD)) {
			response.setStatus(OK.value());
		} else {
			String authorizationHeader=request.getHeader(AUTHORIZATION);
			if(authorizationHeader ==null || !authorizationHeader.startsWith(TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}
			String token=authorizationHeader.substring(TOKEN_PREFIX.length());
			String username=jwtTokenProvider.getSubject(token);
			if(jwtTokenProvider.isTokenValid(username, token) && SecurityContextHolder.getContext().getAuthentication() == null) {
				List<GrantedAuthority> 	authorities = jwtTokenProvider.getAuthorities(token);
				Authentication authentication =jwtTokenProvider.getAuthentication(username, authorities, request);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.clearContext();
			}
		}
		filterChain.doFilter(request, response);
	}
}
