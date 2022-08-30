/**
 * 
 */
package com.hibahumeida.nagarro.dto;

import java.util.Date;

/**
 * @author h.humeida
 *
 */
public class StatementSearchParams {

	private Double minAmount;
	private Double maxAmount;
	private Date fromDate;
	private Date toDate;

	public StatementSearchParams() {
	}

	public StatementSearchParams(Double minAmount, Double maxAmount, Date fromDate, Date toDate) {
		super();
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public Double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}

	public Double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
