/**
 * 
 */
package com.hibahumeida.nagarro.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author h.humeida
 *
 */
@Entity
public class Statement {

	@Id
	private String ID;
	private String datefield;
	private Double amount;
	@Column(name = "account_id")
	@JsonIgnore
	private String accountId;

	/**
	 * 
	 */
	public Statement() {
	}

	public Statement(String iD, String datefield, Double amount, String accountId) {
		super();
		ID = iD;
		this.datefield = datefield;
		this.amount = amount;
		this.accountId = accountId;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getDatefield() {
		return datefield;
	}

	public void setDatefield(String datefield) {
		this.datefield = datefield;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String toString() {
		return "statement [ID=" + ID + ", datefield=" + datefield + ", amount=" + amount + ", account_id=" + accountId
				+ "]";
	}

}
