package com.paypal.api.payments;

import java.util.List;

public class TransactionBase extends CartBase {

	/**
	 * List of financial transactions (Sale, Authorization, Capture, Refund) related to the payment.
	 */
	private List<RelatedResources> relatedResources;

	/**
	 * Default Constructor
	 */
	public TransactionBase() {
	}


	/**
	 * Setter for relatedResources
	 */
	public TransactionBase setRelatedResources(List<RelatedResources> relatedResources) {
		this.relatedResources = relatedResources;
		return this;
	}

	/**
	 * Getter for relatedResources
	 */
	public List<RelatedResources> getRelatedResources() {
		return this.relatedResources;
	}


}
