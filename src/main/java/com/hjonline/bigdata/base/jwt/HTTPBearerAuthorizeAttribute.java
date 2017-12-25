package com.hjonline.bigdata.base.jwt;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjonline.bigdata.base.LocalManager;
import com.hjonline.bigdata.base.LocalManagerType;
import com.hjonline.bigdata.base.conf.Audience;
import com.hjonline.bigdata.model.Users;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.TokenVerifyService;

import io.jsonwebtoken.Claims;

@WebFilter(filterName = "HTTPBearerAuthorizeAttribute", urlPatterns = "/*")
public class HTTPBearerAuthorizeAttribute implements Filter {
	@Autowired
	private Audience audienceEntity;
	@Autowired
	private TokenVerifyService tokenVerifyService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		// 接口豁免
		if (APIExempt.exempt(httpRequest, response, chain)) {
			return;
		}
		String auth = httpRequest.getHeader("Authorization");
		if ((auth != null) && (auth.length() > 7)) {
			String HeadStr = auth.substring(0, 6).toLowerCase();
			if (HeadStr.compareTo("bearer") == 0) {

				auth = auth.substring(6, auth.length());
				Claims claims = JwtHelper.parseJWT(auth, audienceEntity.getBase64Secret());
				if (claims != null) {
					Users users = new Users();
					users.setId(Integer.parseInt(claims.get("userid").toString()));
					users.setAccount(claims.get("name").toString());
					users.setAdmin(Integer.parseInt(claims.get("admin").toString()));
					LocalManager.setVal(LocalManagerType.USERS, users);

					LocalManager.setVal(LocalManagerType.TOKEN, auth);

					if (tokenVerifyService.verifyUser(auth, users)) {
						chain.doFilter(httpRequest, response);
						return;
					}
//					chain.doFilter(httpRequest, response);
//					return;
				}
			}
		}

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json; charset=utf-8");
		httpResponse.setStatus(HttpServletResponse.SC_OK);

		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
		httpResponse.setHeader("Access-Control-Max-Age", "3600");
		httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

		ObjectMapper mapper = new ObjectMapper();

		httpResponse.getWriter()
				.write(mapper.writeValueAsString(BaseResponseFactory.getParamError_500().setData("token错误")));
		return;
	}

	@Override
	public void destroy() {
	}
}
