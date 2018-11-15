package com.move.exception;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 验证失败异常
 * 
 * @author Administrator
 * 
 */
public class ValidateException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<String> messages;

	public ValidateException(String message) {
		super("ValidateException");
		messages = Lists.newArrayList(message);
	}

	public ValidateException(List<String> messages) {
		super("ValidateException");
		this.messages = messages;
	}

	public List<String> getMessages() {
		return messages;
	}
}
