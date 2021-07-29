package com.microservices.spring.zuul.edge.security.filters;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.spring.model.Response;
import static com.microservices.spring.zuul.edge.security.constants.SecurityConstant.FORBIDDEN_MESSAGE;

@Component
public class JWTAuthenticationEntryPoint extends Http403ForbiddenEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2)
			throws IOException {
		Response httpresponse = new Response(
				FORBIDDEN.value(), 
				FORBIDDEN, 
				FORBIDDEN.getReasonPhrase().toUpperCase(), 
				FORBIDDEN_MESSAGE); 
		response.setContentType(APPLICATION_JSON_VALUE);
		response.setStatus(FORBIDDEN.value());
		
		OutputStream outputStream = response.getOutputStream();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(outputStream, httpresponse);
		outputStream.flush();
	}

}
