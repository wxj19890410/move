package com.move.exception;

/**
 * 未登录异常
 * 
 * @author Administrator
 * 
 */
public class NotLogedInException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotLogedInException() {
		super("not loged in");
	}
}
