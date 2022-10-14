package com.qimeixun.exceptions;

/**
 * @desc 文件不存在
 * @author yueyufan
 * @date 2019年12月13日 上午10:17:32
 */
public class FileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -519148153016942380L;

	public FileNotFoundException(String msg) {
		super(msg);
	}

}
