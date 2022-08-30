package com.hibahumeida.nagarro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.hibahumeida.nagarro.constant.Globals;
import com.hibahumeida.nagarro.constant.ResponseCodes;
import com.hibahumeida.nagarro.dao.Account;
import com.hibahumeida.nagarro.dto.AppResponse;
import com.hibahumeida.nagarro.repo.AccountRepo;
import com.hibahumeida.nagarro.utils.ApplicationUtils;

/**
 * @author h.humeida
 *
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = AccountService.class)
public class AccountServiceTest {

	@MockBean
	AccountRepo accountRepo;

	@Autowired
	AccountService accountService;

	@Test
	void accountIDNull_ShouldReturnINVALID_ACCOUNT_IDCode() {

		String accountId = null;
		AppResponse<Account> actualResponse = accountService.getAccount(accountId);
		assertEquals(ResponseCodes.INVALID_ACCOUNT_ID.getCode(), actualResponse.getCode());

	}

	@Test
	void emptyAccountID_ShouldReturnINVALID_ACCOUNT_IDCode() {

		String accountId = "  ";
		AppResponse<Account> actualResponse = accountService.getAccount(accountId);
		assertEquals(ResponseCodes.INVALID_ACCOUNT_ID.getCode(), actualResponse.getCode());

	}

	@Test
	void serviceThrowException_ShouldReturnSERVER_ERRORCode() {

		String accountId = "500";
		when(accountRepo.findById(accountId)).thenThrow(new IllegalArgumentException());
		AppResponse<Account> actualResponse = accountService.getAccount(accountId);
		assertEquals(ResponseCodes.SERVER_ERROR.getCode(), actualResponse.getCode());

	}
	@Test
	void accountIDNotFound_ShouldReturnACCOUNT_NOT_FOUNDCode() {

		String accountId = "500";
		Optional<Account> account = Optional.empty();
		when(accountRepo.findById(accountId)).thenReturn(account);
		AppResponse<Account> actualResponse = accountService.getAccount(accountId);
		assertEquals(ResponseCodes.ACCOUNT_NOT_FOUND.getCode(), actualResponse.getCode());

	}

	@Test
	void validAccount_ShouldReturnSUCCESSCodeAndHashedAccountNumber() {

		String accountId = "2";
		Account account = new Account();
		account.setId("2");
		account.setAccountType("savings");
		account.setAccountNumber("0012250016002");

		Optional<Account> accountDB = Optional.of(account);
		when(accountRepo.findById(accountId)).thenReturn(accountDB);

		AppResponse<Account> actualResponse = accountService.getAccount(accountId);
		assertEquals(ResponseCodes.SUCCESS.getCode(), actualResponse.getCode());
		assertEquals(ApplicationUtils.getHashed("0012250016002", Globals.SecurityConstants.ACCOUNT_NUMBER_HASHING_ALG),
				actualResponse.getResponseInfo().getAccountNumber());

	}

}
