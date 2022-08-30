package com.hibahumeida.nagarro.service;

import java.util.Objects;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class AccountService {

	private static final Logger log = LogManager.getLogger(AccountService.class);
	@Autowired
	AccountRepo accountRepo;

	public AppResponse<Account> getAccount(String accountID) {

		log.info("Get Account Details with ID: {}", accountID);
		try {
			ResponseCodes validateAccountIDResp = validateAccountID(accountID);
			if (validateAccountIDResp.getCode() != ResponseCodes.SUCCESS.getCode()) {
				return new AppResponse<>(validateAccountIDResp, null);
			}
			Optional<Account> accountOptional = accountRepo.findById(accountID);
			if (!accountOptional.isPresent()) {
				return new AppResponse<>(ResponseCodes.ACCOUNT_NOT_FOUND, null);
			} else {

				Account account = accountOptional.get();
				account.setAccountNumber(ApplicationUtils.getHashed(account.getAccountNumber(),
						Globals.SecurityConstants.ACCOUNT_NUMBER_HASHING_ALG));

				return new AppResponse<>(ResponseCodes.SUCCESS, account);
			}
		} catch (Exception e) {
			log.catching(e);
			return new AppResponse<>(ResponseCodes.SERVER_ERROR, null);

		}

	}

	private ResponseCodes validateAccountID(String accountID) {
		if (Objects.isNull(accountID) || accountID.isBlank()) {
			return ResponseCodes.INVALID_ACCOUNT_ID;
		}
		return ResponseCodes.SUCCESS;
	}
}
