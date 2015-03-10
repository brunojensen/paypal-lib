package com.paypal.api.payments;

import com.paypal.base.rest.PayPalModel;


public class ErrorDetails  extends PayPalModel {

	/**
	 * Name of the field that caused the error.
	 */
	private String field;

	/**
	 * Reason for the error.
	 */
	private String issue;

	/**
	 * Default Constructor
	 */
	public ErrorDetails() {
	}

	/**
	 * Parameterized Constructor
	 */
	public ErrorDetails(String field, String issue) {
		this.field = field;
		this.issue = issue;
	}


	/**
	 * Setter for field
	 */
	public ErrorDetails setField(String field) {
		this.field = field;
		return this;
	}

	/**
	 * Getter for field
	 */
	public String getField() {
		return this.field;
	}


	/**
	 * Setter for issue
	 */
	public ErrorDetails setIssue(String issue) {
		this.issue = issue;
		return this;
	}

	/**
	 * Getter for issue
	 */
	public String getIssue() {
		return this.issue;
	}
	
}
