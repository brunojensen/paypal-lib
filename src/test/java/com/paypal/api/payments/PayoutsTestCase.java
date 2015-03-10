package com.paypal.api.payments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.base.rest.JSONFormatter;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

public class PayoutsTestCase {
	
	PayoutBatch result;
	
	private static final Logger logger = Logger.getLogger(PayoutsTestCase.class);

	public static String getJson() {
		return "{\"sender_batch_header\":" + PayoutSenderBatchHeaderTestCase.getJson() + ",\"items\":[" + PayoutItemTestCase.getJson() + "],\"links\":[" + LinksTestCase.getJson() + "]}";
	}
	
	public static Payout getObject() {
		return JSONFormatter.fromJSON(getJson(), Payout.class);
	}
	
	@Test(groups = "unit")
	public void testJsontoObject() {
		Payout payout = PayoutsTestCase.getObject();
		Assert.assertEquals(payout.getSenderBatchHeader().toJSON(), PayoutSenderBatchHeaderTestCase.getObject().toJSON());
		Assert.assertEquals(payout.getItems().get(0).toJSON(), PayoutItemTestCase.getObject().toJSON());
		Assert.assertEquals(payout.getLinks().get(0).toJSON(), LinksTestCase.getObject().toJSON());
	}
	
	@Test(groups = "integration")
	public void testCreate() throws PayPalRESTException {
		logger.info("**** Create Payouts ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		
		Random random = new Random();
		PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();
		senderBatchHeader.setSenderBatchId(
				new Double(random.nextDouble()).toString()).setEmailSubject(
				"You have a Payout!");
		Currency amount = new Currency();
		amount.setValue("1.00").setCurrency("USD");

		PayoutItem senderItem = new PayoutItem();
		senderItem.setRecipientType("Email")
				.setNote("Thanks for your patronage")
				.setReceiver("shirt-supplier-one@gmail.com")
				.setSenderItemId("201404324234").setAmount(amount);

		List<PayoutItem> items = new ArrayList<PayoutItem>();
		items.add(senderItem);
		Payout payout = new Payout();
		payout.setSenderBatchHeader(senderBatchHeader).setItems(items);
		this.result = payout.create(TokenHolder.accessToken, null);
		
		Assert.assertNotNull(this.result);
		Assert.assertNotNull(this.result.getBatchHeader());
		Assert.assertNotNull(this.result.getBatchHeader().getBatchStatus());
	}
	
	@Test(groups = "integration")
	public void testCreateSync() throws PayPalRESTException {
		logger.info("**** Create Payouts Synchronously****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		
		Random random = new Random();
		PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();
		senderBatchHeader.setSenderBatchId(
				new Double(random.nextDouble()).toString()).setEmailSubject(
				"You have a Payout!");
		Currency amount = new Currency();
		amount.setValue("1.00").setCurrency("USD");

		PayoutItem senderItem = new PayoutItem();
		senderItem.setRecipientType("Email")
				.setNote("Thanks for your patronage")
				.setReceiver("shirt-supplier-one@gmail.com")
				.setSenderItemId("201404324234").setAmount(amount);

		List<PayoutItem> items = new ArrayList<PayoutItem>();
		items.add(senderItem);
		Payout payout = new Payout();
		payout.setSenderBatchHeader(senderBatchHeader).setItems(items);
		PayoutBatch result = payout.createSynchronous(TokenHolder.accessToken);
		
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getBatchHeader());
		Assert.assertNotNull(result.getBatchHeader().getBatchStatus());
	}
	
	@Test(groups = "integration")
	public void testGetPayout() throws PayPalRESTException {
		logger.info("**** Get Payout ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		String payoutBatchId = this.result.getBatchHeader().getPayoutBatchId();
		
		this.result = Payout.get(TokenHolder.accessToken, payoutBatchId);
		
		Assert.assertNotNull(this.result);
		Assert.assertEquals(this.result.getBatchHeader().getPayoutBatchId(), payoutBatchId);
		Assert.assertEquals(this.result.getItems().size(), 1);
	}
	
	
	@Test(groups = "integration")
	public void testGetPayoutItem() throws PayPalRESTException {
		logger.info("**** Get Payout Item ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		PayoutItemDetails payoutItem = this.result.getItems().get(0);
		String payoutItemId = payoutItem.getPayoutItemId();
		
		PayoutItemDetails result = PayoutItem.get(TokenHolder.accessToken, payoutItemId);
		
		Assert.assertNotNull(result);
		Assert.assertEquals(result.getPayoutItemId(), payoutItemId);
		Assert.assertEquals(result.getPayoutItemFee().getValue(), payoutItem.getPayoutItemFee().getValue());
		Assert.assertEquals(result.getPayoutItemFee().getCurrency(), payoutItem.getPayoutItemFee().getCurrency());
	}
	
	@Test(groups = "integration")
	public void testPayoutItemCancel() throws PayPalRESTException {
		logger.info("**** Cancel Payout Item ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		PayoutItemDetails payoutItem = this.result.getItems().get(0);
		String payoutItemId = payoutItem.getPayoutItemId();
		
		if (payoutItem.getTransactionStatus() == "UNCLAIMED") {
			PayoutItemDetails result = PayoutItem.cancel(TokenHolder.accessToken, payoutItemId);
			
			Assert.assertNotNull(result);
			Assert.assertEquals(result.getPayoutItemId(), payoutItemId);
			Assert.assertEquals(result.getPayoutItemFee().getValue(), payoutItem.getPayoutItemFee().getValue());
			Assert.assertEquals(result.getPayoutItemFee().getCurrency(), payoutItem.getPayoutItemFee().getCurrency());
			Assert.assertEquals("RETURNED", result.getTransactionStatus());
		}
		
	}
	
	
}
