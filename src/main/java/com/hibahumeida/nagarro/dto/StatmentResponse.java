/**
 * 
 */
package com.hibahumeida.nagarro.dto;

import java.util.List;

import com.hibahumeida.nagarro.dao.Account;
import com.hibahumeida.nagarro.dao.Statement;

/**
 * @author h.humeida
 *
 */
public class StatmentResponse {

	private Account account;
	private List<Statement> statements;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<Statement> getStatements() {
		return statements;
	}

	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}

}
