package com.paypal.api.payments;

import java.util.List;

import com.paypal.base.rest.PayPalModel;

public class AgreementTransactions  extends PayPalModel {

	/**
	 * Array of agreement_transaction object.
	 */
	private List<AgreementTransaction> agreementTransactionList;

	/**
	 * Default Constructor
	 */
	public AgreementTransactions() {
	}


	/**
	 * Setter for agreementTransactionList
	 */
	public AgreementTransactions setAgreementTransactionList(List<AgreementTransaction> agreementTransactionList) {
		this.agreementTransactionList = agreementTransactionList;
		return this;
	}

	/**
	 * Getter for agreementTransactionList
	 */
	public List<AgreementTransaction> getAgreementTransactionList() {
		return this.agreementTransactionList;
	}


}
