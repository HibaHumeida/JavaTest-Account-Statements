package com.hibahumeida.nagarro.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hibahumeida.nagarro.constant.ResponseCodes;

/**
 * @author h.humeida
 *
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { Validator.class })
class ValidatorTest {

	@Autowired
	Validator validator;

	@Nested
	class validateGetStatmentsRequestParamsTests {
		@Test
		void emptyParams_return_SUCCESS_code() {

			String fromDate = "";
			String toDate = "";
			String minAmount = "";
			String maxAmount = "";

			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);

			assertEquals(ResponseCodes.SUCCESS.getCode(), validationResp.getCode());

		}

		@Test
		void invalid_fromDateFormat_return_INVALID_FROM_DATE_code() {

			String fromDate = "12.03-2022";// right format is dd.MM.yyyy
			String toDate = "";
			String minAmount = "60";
			String maxAmount = "70";

			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);

			assertEquals(ResponseCodes.INVALID_FROM_DATE.getCode(), validationResp.getCode());

		}

		@Test
		void invalid_toDateFormat_return_INVALID_TO_DATE_code() {

			String fromDate = "12.03.2022";
			String toDate = "12.2022";// right format is dd.MM.yyyy
			String minAmount = "60";
			String maxAmount = "70";

			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);

			assertEquals(ResponseCodes.INVALID_TO_DATE.getCode(), validationResp.getCode());

		}

		@Test
		void valid_dates_invalid_range_return_INVALID_DATE_RANGE_code() {

			String fromDate = "12.03.2022";
			String toDate = "12.01.2022";// right format is dd.MM.yyyy
			String minAmount = "60";
			String maxAmount = "70";

			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);

			assertEquals(ResponseCodes.INVALID_DATE_RANGE.getCode(), validationResp.getCode());

		}

		@Test
		void invalid_minAmountreturn_INVALID_MIN_AMOUNT_code() {

			String fromDate = "12.03.2022";
			String toDate = "12.04.2022";// right format is dd.MM.yyyy
			String minAmount = "x";
			String maxAmount = "70";

			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);

			assertEquals(ResponseCodes.INVALID_MIN_AMOUNT.getCode(), validationResp.getCode());

		}

		@Test
		void invalid_maxAmount_return_INVALID_MAX_AMOUNT_code() {

			String fromDate = "12.03.2022";
			String toDate = "12.04.2022";// right format is dd.MM.yyyy
			String minAmount = "20";
			String maxAmount = "23.km";

			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);

			assertEquals(ResponseCodes.INVALID_MAX_AMOUNT.getCode(), validationResp.getCode());

		}

		@Test
		void valid_amounts_invalid_range_return_INVALID_AMOUNT_RANGE_code() {

			String fromDate = "12.03.2022";
			String toDate = "12.04.2022";
			String minAmount = "71";
			String maxAmount = "70";

			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);

			assertEquals(ResponseCodes.INVALID_AMOUNT_RANGE.getCode(), validationResp.getCode());

		}

		@Test
		void valid_params_return_SUCCESS_code() {

			String fromDate = "12.03.2022";
			String toDate = "12.04.2022";
			String minAmount = "70";
			String maxAmount = "72";

			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);

			assertEquals(ResponseCodes.SUCCESS.getCode(), validationResp.getCode());

		}
	}
}
