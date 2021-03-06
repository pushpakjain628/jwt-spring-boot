package com.microservices.spring.zuul.edge.security.utility;

import static com.microservices.spring.zuul.edge.security.constants.SecurityConstant.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.microservices.spring.model.UserPrincipal;


@Component
public class JWTTokenProvider {

	@Value("${jwt.secret}")
	private String secret;

	public String generateJwtToken(UserPrincipal userPrincipal) {
		String[] claims= getClaimsFromUser(userPrincipal);
		return JWT.create().withIssuer(GET_ARRAYS_LLC)
				.withAudience(GET_ARRAYS_ADMINISTRATION)
				.withIssuedAt(new Date())
				.withSubject(userPrincipal.getUsername())
				.withArrayClaim(AUTHORITIES, claims)

				.withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret.getBytes()));				
	}

	public List<GrantedAuthority> getAuthorities(String token) {
		String[] claims = getClaimsFromToken(token);
		return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	private String[] getClaimsFromToken(String token) {
		JWTVerifier verifier=getJWTVerifier();
		return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
	}

	private JWTVerifier getJWTVerifier() {
		JWTVerifier verifier = null;
		try {
			Algorithm algorithm= Algorithm.HMAC512(secret);
			verifier=JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
		}
		return verifier;
	}

	private String[] getClaimsFromUser(UserPrincipal user) {
		List<String> authorities=new ArrayList<>();
		for(GrantedAuthority granted: user.getAuthorities()) {
			authorities.add(granted.getAuthority());
		}
		return authorities.toArray(new String[0]);
	}



	public Authentication getAuthentication(String username,List<GrantedAuthority> authorities,HttpServletRequest request) {
		UsernamePasswordAuthenticationToken userPasswordauthToken=new UsernamePasswordAuthenticationToken(username,null,authorities);
		userPasswordauthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return userPasswordauthToken;
	}

	
	public boolean isTokenValid(String username,String token) {
		JWTVerifier verifier=getJWTVerifier();
		return StringUtils.isNotEmpty(username) && isTokenExpired(verifier,token);
	}

	private boolean isTokenExpired(JWTVerifier verifier, String token) {
		Date expiration=verifier.verify(token).getExpiresAt();
		return expiration.before(new Date());
	}
	
	public String getSubject(String token) {
		JWTVerifier verifier=getJWTVerifier();
		return verifier.verify(token).getSubject();
	}


}
