package com.paypal.api.payments;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.base.rest.JSONFormatter;

public class PayoutSenderBatchHeaderTestCase {

	private static final Logger logger = Logger
			.getLogger(PayoutSenderBatchHeaderTestCase.class);

	public static String getJson() {
		return "{\"sender_batch_id\":\"TestSample\",\"email_subject\":\"TestSample\",\"recipient_type\":\"TestSample\"}";
	}

	public static PayoutSenderBatchHeader getObject() {
		return JSONFormatter.fromJSON(getJson(), PayoutSenderBatchHeader.class);
	}
	
	@Test(groups = "unit")
	public void testJsontoObject() {
		PayoutSenderBatchHeader obj = PayoutSenderBatchHeaderTestCase.getObject();
		Assert.assertEquals(obj.getRecipientType(), "TestSample");
		Assert.assertEquals(obj.getEmailSubject(), "TestSample");
		Assert.assertEquals(obj.getSenderBatchId(), "TestSample");
	}
}
