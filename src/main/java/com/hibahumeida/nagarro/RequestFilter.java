package com.hibahumeida.nagarro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.hibahumeida.nagarro.constant.Globals;
import com.hibahumeida.nagarro.utils.ApplicationUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

@Component
public class RequestFilter implements Filter {

	private static final Logger log = LogManager.getLogger(RequestFilter.class);

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = ((HttpServletRequest) servletRequest);
		HttpServletResponse httpServletResponse = ((HttpServletResponse) servletResponse);
		String responseBody = null;

        if (!httpServletRequest.getRequestURI().startsWith("/statement/")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

		RequestWrapper requestWrapper = new RequestWrapper(httpServletRequest);
		ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(
				httpServletResponse);

		log.info(Globals.LOG_SEP);

		log.info("request uri: {} ", httpServletRequest.getRequestURI());

		try {
			log.info(getRequestLog("REQUEST FROM CLIENT", httpServletRequest,
					requestWrapper.getBody()));

			filterChain.doFilter(requestWrapper, contentCachingResponseWrapper);
		} finally {
			byte[] responseBytes = contentCachingResponseWrapper.getContentAsByteArray();
			responseBody = new String(responseBytes, contentCachingResponseWrapper.getCharacterEncoding());
			log.info(getResponseLog("RESPONSE TO CLIENT", contentCachingResponseWrapper, responseBody));
			contentCachingResponseWrapper.copyBodyToResponse();
		}
	}
	
	private static String getRequestLog(String tag, HttpServletRequest httpServletRequest, String requestBody) {
		StringBuilder requestLog = new StringBuilder(tag + ":\r\n");
		requestLog.append("HTTP Request URI: " + httpServletRequest.getRequestURI() + "\r\n");
		requestLog.append("HTTP Request Headers:\r\n");
		// Log request except of the sensitive data specified in
		// SecurityConstants.SECURE_HTTP_HEADERS List
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String key = headerNames.nextElement();
				if (!Globals.SecurityConstants.SECURE_HTTP_HEADERS.contains(key)) {
					String val = httpServletRequest.getHeader(key);
					requestLog.append("\t" + key + ": " + val + "\r\n");
				}
			}
		}
		requestLog.append("HTTP Request Body:\r\n" + requestBody + "\r\n");
		return requestLog.toString();
	}

	private static String getResponseLog(String tag, ContentCachingResponseWrapper contentCachingResponseWrapper,
			String responseBody) {
		StringBuilder responseLog = new StringBuilder();
		responseLog.append(":\r\n");
		responseLog.append("HTTP Status Code: " + String.valueOf(contentCachingResponseWrapper.getStatus()) + "\r\n");
		responseLog.append("HTTP Response Headers:\r\n");

		Collection<String> headerNames = contentCachingResponseWrapper.getHeaderNames();
		for (String key : headerNames) {
			String val = contentCachingResponseWrapper.getHeader(key);
			responseLog.append("\t" + key + ": " + val + "\r\n");
		}
		responseLog.append("HTTP Response Body:\r\n" + responseBody + "\r\n");
		return responseLog.toString();
	}
//
//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//
//	}
//
//	@Override
//	public void destroy() {
//
//	}
}
