package com.qimeixun.exceptions;

/**
 * @desc 未登录时抛出此异常
 * @author yueyufan
 * @date 2019年11月20日 下午4:59:04
 */
public class NoLoginException extends RuntimeException {
	
	private static final long serialVersionUID = -747450063800724688L;

	public NoLoginException(String msg) {
		super(msg);
	}
}
