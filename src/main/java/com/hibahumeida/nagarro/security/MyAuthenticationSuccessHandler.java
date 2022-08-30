/**
 * 
 */
package com.hibahumeida.nagarro.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.hibahumeida.nagarro.constant.Globals;

/**
 * @author h.humeida
 *
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		request.getSession(false).setMaxInactiveInterval(Globals.SESSION_TIMEOUT_IN_SECONDS);

	}
	
	
}