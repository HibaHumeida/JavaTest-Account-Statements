package com.hibahumeida.nagarro.dto;

import com.hibahumeida.nagarro.constant.ResponseCodes;

/**
 * @author h.humeida
 *
 */
public class AppResponse<T> extends BaseResponse {

	private T responseInfo;

	public AppResponse(ResponseCodes responseCode, T responseInfo) {
		super(responseCode);
		this.responseInfo = responseInfo;
	}

	public T getResponseInfo() {
		return responseInfo;
	}

	public void setResponseInfo(T responseInfo) {
		this.responseInfo = responseInfo;
	}

}
