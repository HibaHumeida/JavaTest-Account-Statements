package com.hibahumeida.nagarro.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author h.humeida
 *
 */
@Entity
public class Account {

	@Id
	@Column(name = "ID")
	private String id;
	@Column(name = "account_type")
	private String accountType;
	@Column(name = "account_number")
	private String accountNumber;

	public Account() {
	}

	public Account(String iD, String account_type, String account_number) {
		super();
		id = iD;
		this.accountType = account_type;
		this.accountNumber = account_number;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "Account [ID=" + id + ", account_type=" + accountType + ", account_number=" + accountNumber + "]";
	}

}