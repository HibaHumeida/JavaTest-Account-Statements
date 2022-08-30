package com.hibahumeida.nagarro.dto;

import com.hibahumeida.nagarro.constant.ResponseCodes;

/**
 * @author h.humeida
 *
 */
public abstract class BaseResponse {

	protected int code = ResponseCodes.SERVER_ERROR.getCode();

	protected String message = ResponseCodes.SERVER_ERROR.getMessage();

	protected BaseResponse() {
		super();
	}

	protected BaseResponse(ResponseCodes responseCode) {
		this.code = responseCode.getCode();
		this.message = responseCode.getMessage();
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
