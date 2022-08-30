package com.hibahumeida.nagarro.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author h.humeida
 *
 */
public final class Globals {

	private Globals() {

	}

	public static class SecurityConstants {

		public static final String ACCOUNT_NUMBER_HASHING_ALG = "SHA-256";
		public static final String ALG_SHA3_256 = "SHA3-256";
		public static final String ALG_SHA3_512 = "SHA3-512";
		public static final List<String> SECURE_HTTP_HEADERS = new ArrayList<>(Arrays.asList("cookie"));
	}

	public static final String DATE_FORMAT = "dd.MM.yyyy";
	public static final String LOG_SEP = null;
	public static final int SESSION_TIMEOUT_IN_SECONDS = 60 * 5;

}
