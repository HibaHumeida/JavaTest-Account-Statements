/**
 * 
 */
package com.hibahumeida.nagarro.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.hibahumeida.nagarro.constant.ResponseCodes;
import com.hibahumeida.nagarro.dao.Account;
import com.hibahumeida.nagarro.dao.Statement;
import com.hibahumeida.nagarro.dto.AppResponse;
import com.hibahumeida.nagarro.service.AccountService;
import com.hibahumeida.nagarro.service.StatementService;
import com.hibahumeida.nagarro.utils.Validator;

/**
 * @author h.humeida
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = StatementController.class)
@ContextConfiguration(classes = { StatementController.class })
public class StatementControllerTest {

	@Autowired
	StatementController statementController;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	StatementService statementService;
	@MockBean
	AccountService accountService;
	@MockBean
	Validator validator;

//	@BeforeAll
//	void setup() {
//        mockMvc = MockMvcBuilders
//            .webAppContextSetup(wac)
//            .addFilters(springSecurityFilterChain)
//            .build();
//    }
//	
	@Test
	@WithMockUser(username = "admin", password = "admin", roles = { "ADMIN" })
	void invalidRequestParamsForUserAdmin_shouldReturnValidationError() throws Exception {

		// mock validation of the requests
		when(validator.validateGetStatmentsRequestParams(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
				Mockito.anyString())).thenReturn(ResponseCodes.INVALID_FROM_DATE);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statement/1?fromDate=20-3-2020")
				.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.code").value(1002));

	}

	@Test
	@WithMockUser(username = "user", password = "user", roles = { "USER" })
	void FailedGetAccountStatus_shouldReturnAccountError() throws Exception {

		AppResponse<Account> account = new AppResponse<Account>(ResponseCodes.ACCOUNT_NOT_FOUND, null);
		when(accountService.getAccount(Mockito.anyString())).thenReturn(account);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statement/90").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.code").value(1001));

	}

	@Test
	@WithMockUser(username = "user", password = "user", roles = { "USER" })
	void noStatementFound_shouldReturnNO_STATEMENTS_FOUNDCodeAndAccount() throws Exception {

		Account account = new Account("2", "current", "0012250016003");

		AppResponse<Account> accountServiceResp = new AppResponse<Account>(ResponseCodes.SUCCESS, account);
		List<Statement> statements = new ArrayList<Statement>();
		AppResponse<List<Statement>> statementServiceResp = new AppResponse<List<Statement>>(
				ResponseCodes.NO_STATEMENTS_FOUND, statements);

		when(accountService.getAccount(Mockito.anyString())).thenReturn(accountServiceResp);
		when(statementService.getAccountStatement(Mockito.anyString(), Mockito.any())).thenReturn(statementServiceResp);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statement/2").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.code").value(1000))
				.andExpect(jsonPath("$.responseInfo.account.id").value("2"));

	}

	@Test
	@WithMockUser(username = "user", password = "user", roles = { "USER" })
	void ValidParams_shouldReturnSuccessAndAccountAndStatements() throws Exception {

		Account account = new Account("2", "current", "0012250016003");

		AppResponse<Account> accountServiceResp = new AppResponse<Account>(ResponseCodes.SUCCESS, account);

		Statement statement1 = new Statement("1", "18.06.2020", 100.00, "2");
		Statement statement2 = new Statement("2", "28.05.2022", 100.00, "2");
		Statement statement3 = new Statement("3", "18.07.2022", 99.50, "2");
		Statement statement4 = new Statement("4", "01.06.2010", 90.00, "2");
		Statement statement5 = new Statement("5", "20.06.2022", 50.00, "2");

		List<Statement> statements = Arrays.asList(statement1, statement2, statement3, statement4, statement5);
		AppResponse<List<Statement>> statementServiceResp = new AppResponse<List<Statement>>(ResponseCodes.SUCCESS,
				statements);

		when(accountService.getAccount(Mockito.anyString())).thenReturn(accountServiceResp);
		when(statementService.getAccountStatement(Mockito.anyString(), Mockito.any())).thenReturn(statementServiceResp);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/statement/1").accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andDo(print())
		.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value(0))
				.andExpect(jsonPath("$.responseInfo.account.id").value("2"));

	}

}
