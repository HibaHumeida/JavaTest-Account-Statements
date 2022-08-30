package com.hibahumeida.nagarro.constant;

import java.util.HashMap;
import java.util.Map;


/**
 * Enum that contains response codes return by the application
 * 
 * @author h.humeida
 *
 */
public enum ResponseCodes {

	SUCCESS(0, "Success"),
	SERVER_ERROR(11, "Error"),
	BAD_REQUEST(400, "Bad request"),
	NO_STATEMENTS_FOUND(1000, "No Statements Found"),
	ACCOUNT_NOT_FOUND(1001, "Account Not Found"), 
	INVALID_FROM_DATE(1002, "Invalid from date parameter"),
	INVALID_ACCOUNT_ID(1003,"Invalid account ID"),
	INVALID_TO_DATE(1004, "Invalid to date parameter"),
	INVALID_DATE_RANGE(1005, "Invalid date range"),
	INVALID_MIN_AMOUNT(1006, "Invalid minimum amoun parameter"),
	INVALID_MAX_AMOUNT(1007, "Invalid maximum amount parameter"),
	INVALID_AMOUNT_RANGE(1008, "Invalid amount range");

	private int code;

	private String message;

	private ResponseCodes(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	private static Map<Integer, ResponseCodes> map = new HashMap<>();

	static {
		for (ResponseCodes value : ResponseCodes.values())
			map.put(value.getCode(), value);
	}

	public static ResponseCodes valueOf(int code) {
		return map.get(code);
	}
	

}
