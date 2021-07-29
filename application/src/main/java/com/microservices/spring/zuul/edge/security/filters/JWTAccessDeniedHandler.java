package com.microservices.spring.zuul.edge.security.filters;

import static com.microservices.spring.zuul.edge.security.constants.SecurityConstant.ACCESS_DENIED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.spring.model.Response;

@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		Response httpresponse = new Response(
				UNAUTHORIZED.value(), 
				UNAUTHORIZED, 
				UNAUTHORIZED.getReasonPhrase().toUpperCase(), 
				ACCESS_DENIED); 
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(UNAUTHORIZED.value());

		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream, httpresponse);
		outputStream.flush();
	}

}
