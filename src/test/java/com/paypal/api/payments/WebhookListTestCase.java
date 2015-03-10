package com.paypal.api.payments;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

public class WebhookListTestCase {

	private static final Logger logger = Logger.getLogger(WebhookTestCase.class);

	public WebhookList createWebhookList() {
		WebhookList webhookList = new WebhookList();
		List<Webhook> webhooks = new ArrayList<Webhook>();
		Webhook webhook1 = WebhookTestCase.createWebhook();
		Webhook webhook2 = WebhookTestCase.createWebhookWithAllEvents();
		webhooks.add(webhook1);
		webhooks.add(webhook2);
		webhookList.setWebhooks(webhooks);
		return webhookList;
	}

	@Test(groups = "unit")
	public void testWebhooksListConstruction() {
		WebhookList webhookList = createWebhookList();
		Assert.assertEquals(webhookList.getWebhooks().size(), 2);
		Assert.assertNotEquals(webhookList.getWebhooks().get(0), webhookList.getWebhooks().get(1));
	}

	@Test(groups = "unit")
	public void testTOJSON() {
		WebhookList webhookList = createWebhookList();
		Assert.assertEquals(webhookList.toJSON().length() == 0, false);
		logger.info("WebhookListJSON = " + webhookList.toJSON());
	}

	@Test(groups = "unit")
	public void testTOString() {
		WebhookList webhookList = createWebhookList();
		Assert.assertEquals(webhookList.toJSON().length() == 0, false);
	}
	
	@Test(groups = "integration", dependsOnMethods = {"testDeleteAllWebhooks"})
	public void testListWebhooks() throws PayPalRESTException {
		logger.info("**** List All Webhooks ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		logger.info("Generated Access Token = " + TokenHolder.accessToken);
		
		for(int i=0; i<2; i++) {
			Webhook webhookRequest = new Webhook();
			String uuid = UUID.randomUUID().toString();
			webhookRequest.setUrl(WebhooksInputData.WEBHOOK_URL + uuid);
			webhookRequest.setEventTypes(EventTypeListTestCase.createAuthEventTypeList());
			webhookRequest.create(TokenHolder.accessToken, webhookRequest);
		}
			
		WebhookList webhookList = new WebhookList();
		WebhookList webhookResponse = webhookList.getAll(TokenHolder.accessToken);
		logger.info("Response = " + webhookResponse.toJSON());
		
		Assert.assertTrue(webhookResponse.getWebhooks().size() >= 2, "Webhook List contains Two or More Webhooks");
	}
	
	@Test(groups = "integration")
	public void testDeleteAllWebhooks() throws PayPalRESTException {
		logger.info("**** Delete All Webhooks ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		logger.info("Generated Access Token = " + TokenHolder.accessToken);
			
		WebhookList webhookList = new WebhookList();
		WebhookList webhookRequest = webhookList.getAll(TokenHolder.accessToken);
		
		for(int i=0; i<webhookRequest.getWebhooks().size(); i++) {
			Webhook webhook = new Webhook();
			webhook.delete(TokenHolder.accessToken, webhookRequest.getWebhooks().get(i).getId());
		}
		
		webhookRequest = webhookList.getAll(TokenHolder.accessToken);
		logger.info("Response = " + webhookRequest.toJSON());
		Assert.assertEquals(webhookRequest.getWebhooks().size(), 0);
	}
}
