package com.qimeixun.exceptions;

/**
 * @desc 无权限操作异常
 * @author yueyufan
 * @date 2019年11月20日 下午5:02:35
 */
public class NoPermissionException extends RuntimeException{
	
	private static final long serialVersionUID = -7385947547979865899L;

	public NoPermissionException(String msg) {
		super(msg);
	}

}
