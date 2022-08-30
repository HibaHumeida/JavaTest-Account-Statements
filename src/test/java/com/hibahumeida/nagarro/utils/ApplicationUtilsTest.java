package com.hibahumeida.nagarro.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hibahumeida.nagarro.constant.Globals;
import com.hibahumeida.nagarro.dto.StatementSearchParams;

/**
 * @author h.humeida
 *
 */

@WebAppConfiguration
@ContextConfiguration(classes = { ApplicationUtils.class })
public class ApplicationUtilsTest {

	@Nested
	class DoubleValuesValidaterTest {
		@Test
		void invalid_double_string_should_return_null() {

			String doubleValue = "string";

			Double expectedValue = null;

			Double value = ApplicationUtils.isDouble(doubleValue);

			assertEquals(value, expectedValue);

		}

		@Test
		void invalid_double_string_should_return_double_value() {

			String doubleValue = "90";

			Double expectedValue = 90.00;

			Double value = ApplicationUtils.isDouble(doubleValue);

			assertEquals(value, expectedValue);

		}
	}

	@Nested
	class DateValuesValidaterTest {

		@Test
		void invalid_date_string_should_return_null() {

			String dateValue = "20.11";

			String datePattern = "dd.MM.yyyy";

			Date value = ApplicationUtils.isDate(datePattern, dateValue);

			assertNull(value);

		}

		@Test
		void invalid_empty_date_string_should_return_null() {

			String dateValue = "";

			String datePattern = "dd.MM.yyyy";

			Date expectedValue = null;

			Date value = ApplicationUtils.isDate(datePattern, dateValue);

			assertEquals(value, expectedValue);

		}

		@Test
		void invalid_date_pattern_string_should_return_null() {

			String dateValue = "10.11.2011";

			String datePattern = "null";

			Date expectedValue = null;

			Date value = ApplicationUtils.isDate(datePattern, dateValue);

			assertEquals(value, expectedValue);

		}

		@Test
		void valid_but_different_date_string_format_should_return_null_value() {

			String dateValue = "20-11-2020";

			String datePattern = "dd.MM.yyyy";

			Date expectedValue = null;

			Date value = ApplicationUtils.isDate(datePattern, dateValue);

			assertEquals(value, expectedValue);

		}

		@Test
		void valid_date_string_and_matches_format_should_return_date_value() {

			String dateValue = "20.11.2020";

			String datePattern = "dd.MM.yyyy";

			Calendar cal = Calendar.getInstance();
			cal.set(2020, 10, 20, 0, 0, 0);

			Date expectedValue = cal.getTime();

			Date value = ApplicationUtils.isDate(datePattern, dateValue);

			assertEquals(DateUtils.round(value, Calendar.HOUR), DateUtils.round(expectedValue, Calendar.HOUR));

		}
	}

	@Nested
	class dateRangeValidaterTest {
		@ParameterizedTest
		@CsvSource({ "string, 20.11.2020", "20.11.2010, 50.11.2010", ", 20.11.2020", "20.11.2010, ", "null, 20.11.2020",
				"20.11.2010, null", })
		void invalid_Range_should_return_false(String fromDate, String toDate) {

			assertFalse(ApplicationUtils.checkIfDateRangeIsValid(Globals.DATE_FORMAT, fromDate, toDate));

		}

		@Test
		void fromDateStr_before_toDateStr_should_return_true() {

			String fromDate = "20.11.2010";
			String toDate = "21.11.2010";
			assertTrue(ApplicationUtils.checkIfDateRangeIsValid(Globals.DATE_FORMAT, fromDate, toDate));

		}

	}

	@Nested
	class doubleRangeValidaterTest {
		@ParameterizedTest
		@CsvSource({ "string, 100.00", "90.00, 3pm", ", 100.00", "90.00, ", "null, 100.00", "90.00, null",
				"90.00, 89.00" })

		void invalidAmount_should_return_false(String minAmount, String maxAmount) {
			assertFalse(ApplicationUtils.checkIfAmountRangeIsValid(minAmount, maxAmount));

		}

		@Test
		void minAmount_lessThan_maxAmount_should_return_true() {

			String minAmount = "90.00";
			String maxAmount = "91.00";
			assertTrue(ApplicationUtils.checkIfAmountRangeIsValid(minAmount, maxAmount));

		}

	}

	@Nested
	class hashingTest {

		@Test
		void invalidAlgorithm_return_null() {

			String actual = ApplicationUtils.getHashed("0012250016001", "3rt");
			assertNull(actual);
		}

		@Test
		void SameString_Hashed_differentAlgorithm_notEqual() {

			String sha512Hashing = ApplicationUtils.getHashed("0012250016001", "SHA-512");
			String sha256Hashing = ApplicationUtils.getHashed("0012250016001", "SHA-256");
			assertNotEquals(sha256Hashing, sha512Hashing);

		}

		@Test
		void getHashValue_algorithmsha256_equal() {

			String actual = ApplicationUtils.getHashed("0012250016001", "SHA-256");
			String expectedValue = "BDco2TX3quCd6P5n6lbGP2AhMTPOoL20L/dokfpLdZc=";
			assertEquals(expectedValue, actual);
		}

	}

	@Nested
	class setSearchParamsTest {

		@Test
		void allParams_empty_return_defaultParams() {

			String fromDate = "";
			String toDate = "";
			String minAmount = "";
			String maxAmount = "";

			StatementSearchParams actualParams = ApplicationUtils.setSearchParams(fromDate, toDate, minAmount,
					maxAmount);
			StatementSearchParams expectedParams = ApplicationUtils.setDefaultSearchParams();

			assertThat(actualParams).usingRecursiveComparison().isEqualTo(expectedParams);
		}

		@Test
		void validEmptyTodate_searchParams_to_statementSearchParams() {

			String fromDate = "12.03.2022";
			String toDate = "";
			String minAmount = "60";
			String maxAmount = "70";

			StatementSearchParams actualParams = ApplicationUtils.setSearchParams(fromDate, toDate, minAmount,
					maxAmount);
			StatementSearchParams expectedParams = new StatementSearchParams();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(2022, 02, 12, 0, 0, 0);

			Date from = cal.getTime();
			expectedParams.setFromDate(from);
			expectedParams.setToDate(null);
			expectedParams.setMinAmount(60.00);
			expectedParams.setMaxAmount(70.00);

			assertThat(actualParams).usingRecursiveComparison().isEqualTo(expectedParams);
		}

		@Test
		void valid_searchParams_to_statementSearchParams() {

			String fromDate = "";
			String toDate = "12.03.2022";
			String minAmount = "60";
			String maxAmount = "70";

			StatementSearchParams actualParams = ApplicationUtils.setSearchParams(fromDate, toDate, minAmount,
					maxAmount);
			StatementSearchParams expectedParams = new StatementSearchParams();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(0);
			cal.set(2022, 02, 12, 0, 0, 0);

			Date to = cal.getTime();
			expectedParams.setFromDate(null);
			expectedParams.setToDate(to);
			expectedParams.setMinAmount(60.00);
			expectedParams.setMaxAmount(70.00);

			assertThat(actualParams).usingRecursiveComparison().isEqualTo(expectedParams);
		}

	}

	@Nested
	class setDefaultSearchParamsTest {

		@Test
		void defaultParams_AmountRangeShouldBeNull() {

			StatementSearchParams actualParams = ApplicationUtils.setDefaultSearchParams();

			assertNull(actualParams.getMinAmount());
			assertNull(actualParams.getMaxAmount());
		}

		@Test
		void defaultParams_DateRangeIs3Months() {

			StatementSearchParams actualParams = ApplicationUtils.setDefaultSearchParams();

			// convert date to localDate to calculate date difference
			LocalDate date = actualParams.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate date2 = actualParams.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int actualDateRangeInMonths = Period.between(date, date2).getMonths();
			int expectedDateRangeInMonths = 3;

			assertEquals(expectedDateRangeInMonths, actualDateRangeInMonths);
		}

	}

	@Nested
	class addMonthsTest {

		@Test
		void add3Months() {

			int expectedDiff = 3;
			Date actualdatePlus3Months = ApplicationUtils.addMonths(new Date(), 3);
			Date expectedValue = new Date();

			LocalDate actualdatePlus3MonthsLD = actualdatePlus3Months.toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			LocalDate expectedValueLD = expectedValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int actualdiff = Period.between(expectedValueLD, actualdatePlus3MonthsLD).getMonths();

			assertEquals(expectedDiff, actualdiff);

		}

		@Test
		void substract3Months() {

			int expectedDiff = -3;
			Date actualdatePlus3Months = ApplicationUtils.addMonths(new Date(), -3);
			Date expectedValue = new Date();

			LocalDate actualdatePlus3MonthsLD = actualdatePlus3Months.toInstant().atZone(ZoneId.systemDefault())
					.toLocalDate();
			LocalDate expectedValueLD = expectedValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int actualdiff = Period.between(expectedValueLD, actualdatePlus3MonthsLD).getMonths();

			assertEquals(expectedDiff, actualdiff);

		}

	}

}
