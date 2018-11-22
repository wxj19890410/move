package com.move.utils;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 获取用户的session dto
 */
public class LoginUserInfoMethodArgumentResolver implements HandlerMethodArgumentResolver {
	public static final String LOGIN_HEADER_NAME = "loginUuid";

	@Override
	public Object resolveArgument(MethodParameter arg0, ModelAndViewContainer arg1, NativeWebRequest arg2,
			WebDataBinderFactory arg3) throws Exception {
		String token = arg2.getHeader(LOGIN_HEADER_NAME);
		if (StringUtils.isNotBlank(token)) {
			return Utilities.getUserInfo(token);
		}
		return null;
	}

	@Override
	public boolean supportsParameter(MethodParameter arg0) {
		return arg0.getParameterType().equals(UserInfo.class);
	}

}
