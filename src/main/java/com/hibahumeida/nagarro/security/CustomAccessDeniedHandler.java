package com.hibahumeida.nagarro.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hibahumeida.nagarro.controller.advice.ResourceErrorResponse;

/**
 * @author h.humeida
 *
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	// return 401 to all access denied requests.
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		ResourceErrorResponse resp = new ResourceErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				"You are not authenticated");
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.getWriter().write(mapper.writeValueAsString(resp));
	}
}
