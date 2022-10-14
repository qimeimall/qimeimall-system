package com.qimeixun.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @desc MD5通用类
 * @author yueyufan
 * @date 2019年11月7日 下午2:31:16
 */
@Slf4j
public class MD5Util {

	public static void main(String[] args) throws Exception {
		System.out.println(MD5Util.md5("123456", "893d4a24ff3a4fbd9c644b3fff8fa0a0"));
	}

	/**
	 * MD5方法
	 *
	 * @param text 明文
	 * @param key  密钥
	 * @return 密文
	 * @throws Exception
	 */
	public static String md5(String text, String key) {
		String encodeStr = "";
		try {
			// 加密后的字符串
			encodeStr = DigestUtils.md5Hex(text + key);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("密码加密时发生异常:{}", e);
		}
		return encodeStr;
	}

	/**
	 * MD5验证方法
	 *
	 * @param text 明文
	 * @param md5  密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String md5, String key) throws Exception {
		// 根据传入的密钥进行验证
		String md5Text = md5(text, key);
		if (md5Text.equalsIgnoreCase(md5)) {
			return true;
		}
		return false;
	}

}
