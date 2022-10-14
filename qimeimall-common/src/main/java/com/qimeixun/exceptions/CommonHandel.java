package com.qimeixun.exceptions;

import java.util.HashMap;
import java.util.Map;

public class CommonHandel {

	protected Map<String, Object> success(String code, String msg, HashMap<String, Object> data) {

		return this.result(code, msg, data);
	}

	protected Map<String, Object> success(String code, String msg) {

		return this.result(code, msg);
	}

	protected Map<String, Object> error(String code, String msg, HashMap<String, Object> data) {

		return this.result(code, msg, data);
	}

	protected Map<String, Object> error(String code, String msg) {

		return this.result(code, msg);
	}

	private Map<String, Object> result(String code, String msg, HashMap<String, Object> data) {

		Map<String, Object> result = new HashMap<>();
		result.put("code", code);
		result.put("msg", msg);
		result.put("data", data);

		return result;
	}

	private Map<String, Object> result(String code, String msg) {

		Map<String, Object> result = new HashMap<>();
		result.put("code", code);
		result.put("msg", msg);
		result.put("data", new HashMap<>());

		return result;
	}

}
