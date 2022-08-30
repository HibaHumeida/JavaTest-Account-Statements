/**
 * 
 */
package com.hibahumeida.nagarro.controller;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.hibahumeida.nagarro.constant.ResponseCodes;
import com.hibahumeida.nagarro.dao.Account;
import com.hibahumeida.nagarro.dao.Statement;
import com.hibahumeida.nagarro.dto.AppResponse;
import com.hibahumeida.nagarro.dto.StatementSearchParams;
import com.hibahumeida.nagarro.dto.StatmentResponse;
import com.hibahumeida.nagarro.service.AccountService;
import com.hibahumeida.nagarro.service.StatementService;
import com.hibahumeida.nagarro.utils.ApplicationUtils;
import com.hibahumeida.nagarro.utils.Validator;

/**
 * @author h.humeida
 *
 */
@RestController
@RequestMapping("/statement")
public class StatementController {

	private static final Logger log = LogManager.getLogger(StatementController.class);
	@Autowired
	StatementService statementService;
	@Autowired
	AccountService accountService;

	@Autowired
	Validator validator;

	@GetMapping("/{accountId}")
	public ResponseEntity<AppResponse<StatmentResponse>> getAccountStatements(@PathVariable String accountId,
			@RequestParam(required = false, defaultValue = "") String fromDate,
			@RequestParam(required = false, defaultValue = "") String toDate,
			@RequestParam(required = false, defaultValue = "") String minAmount,
			@RequestParam(required = false, defaultValue = "") String maxAmount, HttpServletRequest request)
			throws AccessDeniedException {

		AppResponse<StatmentResponse> serviceResp = null;
		StatmentResponse resp = new StatmentResponse();
		StatementSearchParams params = null;
		if (request.isUserInRole("ROLE_ADMIN")) {
			ResponseCodes validationResp = validator.validateGetStatmentsRequestParams(fromDate, toDate, minAmount,
					maxAmount);
			if (validationResp.getCode() != ResponseCodes.SUCCESS.getCode()) {
				serviceResp = new AppResponse<>(validationResp, null);
				return new ResponseEntity<>(serviceResp, HttpStatus.OK);

			} else {
				params = ApplicationUtils.setSearchParams(fromDate, toDate, minAmount, maxAmount);
			}
		} else {
			params = ApplicationUtils.setDefaultSearchParams();
		}
		// Get Account details
		AppResponse<Account> getAccount = accountService.getAccount(accountId);

		if (getAccount.getCode() != ResponseCodes.SUCCESS.getCode()) {
			serviceResp = new AppResponse<>(ResponseCodes.valueOf(getAccount.getCode()), null);
			return new ResponseEntity<>(serviceResp, HttpStatus.OK);
		}
		// set Account
		resp.setAccount(getAccount.getResponseInfo());
		// Get Account Statements
		AppResponse<List<Statement>> getStatements = statementService.getAccountStatement(accountId, params);
		if (getStatements.getCode() != ResponseCodes.SUCCESS.getCode()) {
			serviceResp = new AppResponse<>(ResponseCodes.valueOf(getStatements.getCode()), resp);
			return new ResponseEntity<>(serviceResp, HttpStatus.OK);
		}
		resp.setStatements(getStatements.getResponseInfo());
		serviceResp = new AppResponse<>(ResponseCodes.SUCCESS, resp);

		return new ResponseEntity<>(serviceResp, HttpStatus.OK);
	}
}
