package com.hibahumeida.nagarro.utils;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.hibahumeida.nagarro.constant.Globals;
import com.hibahumeida.nagarro.constant.ResponseCodes;

@Component
public class Validator {

	public ResponseCodes validateGetStatmentsRequestParams(String fromDate, String toDate, String minAmount,
			String maxAmount) {

		// check all params exists
		if (!fromDate.trim().isEmpty() && Objects.isNull(ApplicationUtils.isDate(Globals.DATE_FORMAT, fromDate))) {
			
			return ResponseCodes.INVALID_FROM_DATE;
		}
		if (!toDate.trim().isEmpty() && Objects.isNull(ApplicationUtils.isDate(Globals.DATE_FORMAT, toDate))) {
			return ResponseCodes.INVALID_TO_DATE;
		}
		if (!fromDate.trim().isEmpty() && !toDate.trim().isEmpty()
				&& !ApplicationUtils.checkIfDateRangeIsValid(Globals.DATE_FORMAT, fromDate, toDate)) {
			return ResponseCodes.INVALID_DATE_RANGE;
		}
		if (!minAmount.trim().isEmpty() && Objects.isNull(ApplicationUtils.isDouble(minAmount))) {
			return ResponseCodes.INVALID_MIN_AMOUNT;
		}
		if (!maxAmount.trim().isEmpty() && Objects.isNull(ApplicationUtils.isDouble(maxAmount))) {
			return ResponseCodes.INVALID_MAX_AMOUNT;
		}
		if (!minAmount.trim().isEmpty() && !maxAmount.trim().isEmpty()
				&& !ApplicationUtils.checkIfAmountRangeIsValid(minAmount, maxAmount)) {
			return ResponseCodes.INVALID_AMOUNT_RANGE;
		}

		return ResponseCodes.SUCCESS;
	}
}
