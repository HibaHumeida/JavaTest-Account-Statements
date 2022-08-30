/**
 * 
 */
package com.hibahumeida.nagarro.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.hibahumeida.nagarro.constant.Globals;
import com.hibahumeida.nagarro.dto.StatementSearchParams;

/**
 * @author h.humeida
 *
 */
public final class ApplicationUtils {

	private static final Logger log = LogManager.getLogger(ApplicationUtils.class);

	private ApplicationUtils() {

	}

	public static Double isDouble(String str) {
		try {

			return Double.parseDouble(str);
		} catch (Exception e) { // catch NumberFormatException if better message is required
			log.catching(e);
			return null;
		}
	}

	public static Date isDate(String pattern, String str) {
		try {

			SimpleDateFormat format = new SimpleDateFormat(pattern);
			format.setLenient(false);
			return format.parse(str.trim());
		} catch (Exception e) { // catch ParseException if better message is required
			log.catching(e);
			return null;
		}
	}

	public static Boolean checkIfDateRangeIsValid(String pattern, String fromDate, String toDate) {

		try {
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			format.setLenient(false);
			Date convertedFromDate = format.parse(fromDate);
			Date convertedToDate = format.parse(toDate);
			return convertedToDate.after(convertedFromDate);
		} catch (Exception e) { // catch ParseException if better message is required
			log.catching(e);
			return false;
		}
	}

	public static Boolean checkIfAmountRangeIsValid(String minAmount, String maxAmount) {

		try {
			Double convertedMinAmount = Double.parseDouble(minAmount.trim());
			Double convertedMaxAmount = Double.parseDouble(maxAmount.trim());
			if (convertedMaxAmount < convertedMinAmount) {
				return false;
			}
		} catch (Exception e) { // catch ParseException if better message is required
			log.catching(e);
			return false;
		}
		return true;
	}

	public static String getHashed(String input, String algorithm) {
		try {
			byte[] inputByte = input.getBytes(StandardCharsets.UTF_8);
			byte[] hashBytes = getSHA2(algorithm, inputByte);
			return Base64.getEncoder().encodeToString(hashBytes);
		} catch (Exception e) {
			log.catching(e);
			return null;
		}

	}

	private static byte[] getSHA2(String algorithm, byte[] bytesToHash) {

		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			byte[] hashBytes = messageDigest.digest(bytesToHash);
			return hashBytes;
		} catch (NoSuchAlgorithmException e) {
			log.catching(e);
			return null;
		}
	}

	public static StatementSearchParams setSearchParams(String fromDate, String toDate, String minAmount,
			String maxAmount) {
		if (minAmount.isEmpty() && maxAmount.isEmpty() && fromDate.isEmpty() && toDate.isEmpty()) {
			return setDefaultSearchParams();
		}
		StatementSearchParams params = new StatementSearchParams();
		params.setMinAmount(minAmount.isEmpty() ? null : isDouble(minAmount));
		params.setMaxAmount(maxAmount.isEmpty() ? null : isDouble(maxAmount));
		params.setFromDate(fromDate.isEmpty() ? null : isDate(Globals.DATE_FORMAT, fromDate));
		params.setToDate(toDate.isEmpty() ? null : isDate(Globals.DATE_FORMAT, toDate));
		return params;
	}

	public static StatementSearchParams setDefaultSearchParams() {
		StatementSearchParams params = new StatementSearchParams();
		Date now = new Date();
		params.setFromDate(addMonths(now, -3));
		params.setToDate(now);
		return params;
	}

	public static Date addMonths(Date date, int duration) {

		Calendar cal = getCalendar(date);
		cal.add(Calendar.MONTH, duration);
		return cal.getTime();
	}

	private static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.US);
		cal.setTime(date);
		return cal;
	}

}
