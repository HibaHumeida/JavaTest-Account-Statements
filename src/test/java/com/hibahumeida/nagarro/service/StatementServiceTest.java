package com.hibahumeida.nagarro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hibahumeida.nagarro.constant.ResponseCodes;
import com.hibahumeida.nagarro.dao.Statement;
import com.hibahumeida.nagarro.dto.AppResponse;
import com.hibahumeida.nagarro.dto.StatementSearchParams;
import com.hibahumeida.nagarro.repo.StatementRepo;
import com.hibahumeida.nagarro.utils.ApplicationUtils;

/**
 * @author h.humeida
 *
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = StatementService.class)
public class StatementServiceTest {

	@Autowired
	StatementService statementService;

	@MockBean
	StatementRepo statementRepo;

	@MockBean
	AccountService accountService;

	@Test
	void emptyStatmentList_ShouldReturnNOT_STATEMENTSCode() {

		String accountId = "500";
		List<Statement> statements = new ArrayList<Statement>();
		when(statementRepo.findByAccountId(accountId)).thenReturn(statements);
		AppResponse<List<Statement>> actualResponse = statementService.getAccountStatement(accountId,
				ApplicationUtils.setDefaultSearchParams());
		assertEquals(ResponseCodes.NO_STATEMENTS_FOUND.getCode(), actualResponse.getCode());
	}

	@Test
	void getStatmentwithDefaultsParams_shouldreturnSUCCESS() {

		String accountId = "1";
		// create mock statements
		Statement statement1 = new Statement("1", "18.06.2020", 100.00, "1");
		Statement statement2 = new Statement("1", "28.05.2022", 100.00, "1");
		Statement statement3 = new Statement("1", "18.07.2022", 99.50, "1");
		Statement statement4 = new Statement("1", "01.06.2010", 90.00, "1");
		Statement statement5 = new Statement("1", "20.06.2022", 50.00, "1");

		List<Statement> statements = Arrays.asList(statement1, statement2, statement3, statement4, statement5);
		when(statementRepo.findByAccountId(accountId)).thenReturn(statements);
		AppResponse<List<Statement>> actualResponse = statementService.getAccountStatement(accountId,
				ApplicationUtils.setDefaultSearchParams());
		List<Statement> expectedfiltered3MonthsBeforeStatment = Arrays.asList(statement3, statement5);

		assertEquals(ResponseCodes.SUCCESS.getCode(), actualResponse.getCode());
		assertEquals(expectedfiltered3MonthsBeforeStatment.size(), actualResponse.getResponseInfo().size());
	}

	@Test
	void getStatmentwithEmptySearchParams_shouldreturnSUCCESS() {

		String accountId = "1";
		// create mock statements
		Statement statement1 = new Statement("1", "18.06.2020", 100.00, "1");
		Statement statement2 = new Statement("2", "28.05.2022", 110.00, "1");
		Statement statement3 = new Statement("3", "18.07.2022", 99.50, "1");
		Statement statement4 = new Statement("4", "01.06.2010", 90.00, "1");
		Statement statement5 = new Statement("5", "20.06.2022", 50.00, "1");

		List<Statement> statements = Arrays.asList(statement1, statement2, statement3, statement4, statement5);

		when(statementRepo.findByAccountId(accountId)).thenReturn(statements);

		StatementSearchParams nullSearchParams = new StatementSearchParams(null, null, null, null);
		AppResponse<List<Statement>> actualResponse = statementService.getAccountStatement(accountId, nullSearchParams);

		List<Statement> nullSearchfilteredStatment = statements;

		assertEquals(ResponseCodes.SUCCESS.getCode(), actualResponse.getCode());
		assertEquals(nullSearchfilteredStatment.size(), actualResponse.getResponseInfo().size());
	}

	@Test
	void getStatmentwithCustomAmountRange_shouldreturnSUCCESS() {

		String accountId = "1";
		// create mock statements
		Statement statement1 = new Statement("1", "18.06.2020", 100.00, "1");
		Statement statement2 = new Statement("2", "28.05.2022", 110.00, "1");
		Statement statement3 = new Statement("3", "18.07.2022", 99.50, "1");
		Statement statement4 = new Statement("4", "01.06.2010", 90.00, "1");
		Statement statement5 = new Statement("5", "20.06.2022", 50.00, "1");

		List<Statement> statements = Arrays.asList(statement1, statement2, statement3, statement4, statement5);

		when(statementRepo.findByAccountId(accountId)).thenReturn(statements);

		StatementSearchParams AmountRangeSearchParams = new StatementSearchParams(89.00, 100.00, null, null);
		AppResponse<List<Statement>> actualResponse = statementService.getAccountStatement(accountId,
				AmountRangeSearchParams);

		List<Statement> expectedfilteredAmountRangeStatment = Arrays.asList(statement1, statement3, statement4);

		assertEquals(ResponseCodes.SUCCESS.getCode(), actualResponse.getCode());
		assertEquals(expectedfilteredAmountRangeStatment.size(), actualResponse.getResponseInfo().size());
	}

	@Test
	void getStatmentwithCustomDateRange_shouldreturnSUCCESS() {

		String accountId = "1";
		// create mock statements
		Statement statement1 = new Statement("1", "18.06.2020", 100.00, "1");
		Statement statement2 = new Statement("2", "28.05.2022", 110.00, "1");
		Statement statement3 = new Statement("3", "18.07.2022", 99.50, "1");
		Statement statement4 = new Statement("4", "01.06.2010", 90.00, "1");
		Statement statement5 = new Statement("5", "20.06.2022", 50.00, "1");

		List<Statement> statements = Arrays.asList(statement1, statement2, statement3, statement4, statement5);

		when(statementRepo.findByAccountId(accountId)).thenReturn(statements);

		Calendar fromCal = Calendar.getInstance();
		fromCal.set(2022, 04, 29, 0, 0, 0);
		Date fromDate = fromCal.getTime();
		Calendar toCal = Calendar.getInstance();
		toCal.set(2022, 06, 29, 0, 0, 0);
		Date toDate = toCal.getTime();
		StatementSearchParams dateRangeSearchParams = new StatementSearchParams(null, null, fromDate, toDate);
		AppResponse<List<Statement>> actualResponse = statementService.getAccountStatement(accountId,
				dateRangeSearchParams);

		List<Statement> expectedfilteredDateRangeStatment = Arrays.asList(statement3, statement5);

		assertEquals(ResponseCodes.SUCCESS.getCode(), actualResponse.getCode());
		assertEquals(expectedfilteredDateRangeStatment.size(), actualResponse.getResponseInfo().size());
	}
	
	

	@Test
	void getStatmentwithCustomDateRangeAndAmountRange_shouldreturnSUCCESS() {

		String accountId = "1";
		// create mock statements
		Statement statement1 = new Statement("1", "18.06.2020", 100.00, "1");
		Statement statement2 = new Statement("2", "28.05.2022", 110.00, "1");
		Statement statement3 = new Statement("3", "18.07.2022", 99.50, "1");
		Statement statement4 = new Statement("4", "01.06.2010", 90.00, "1");
		Statement statement5 = new Statement("5", "20.06.2022", 50.00, "1");

		List<Statement> statements = Arrays.asList(statement1, statement2, statement3, statement4, statement5);

		when(statementRepo.findByAccountId(accountId)).thenReturn(statements);

		Calendar fromCal = Calendar.getInstance();
		fromCal.set(2022, 04, 29, 0, 0, 0);
		Date fromDate = fromCal.getTime();
		Calendar toCal = Calendar.getInstance();
		toCal.set(2022, 06, 29, 0, 0, 0);
		Date toDate = toCal.getTime();
		StatementSearchParams dateRangeSearchParams = new StatementSearchParams(55.00, 90.00, fromDate, toDate);
		AppResponse<List<Statement>> actualResponse = statementService.getAccountStatement(accountId,
				dateRangeSearchParams);

		List<Statement> expectedfilteredCustomDateRangeAndAmountRangeStatment = new ArrayList<Statement>();

		assertEquals(ResponseCodes.SUCCESS.getCode(), actualResponse.getCode());
		assertEquals(expectedfilteredCustomDateRangeAndAmountRangeStatment.size(),
				actualResponse.getResponseInfo().size());
	}

}
