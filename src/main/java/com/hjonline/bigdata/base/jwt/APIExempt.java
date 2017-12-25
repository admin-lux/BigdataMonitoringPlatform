package com.hjonline.bigdata.base.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口免token验证类
 * @author rick_lu
 *
 */
public class APIExempt {

	public static boolean exempt(HttpServletRequest httpRequest, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (httpRequest.getRequestURI().contains("/monitor/downloadFailedsYaml")) {
			chain.doFilter(httpRequest, response);
			return true;
		}
		if (httpRequest.getRequestURI().contains("/monitor/exportLog")) {
			chain.doFilter(httpRequest, response);
			return true;
		}
		if (httpRequest.getRequestURI().contains("users/login")) {
			chain.doFilter(httpRequest, response);
			return true;
		}
		if (httpRequest.getRequestURI().contains("yaml/downloadYaml")) {
			chain.doFilter(httpRequest, response);
			return true;
		}
		if (httpRequest.getMethod().toUpperCase().equals("OPTIONS")) {
			chain.doFilter(httpRequest, response);
			return true;
		}
		return false;
	}

}
