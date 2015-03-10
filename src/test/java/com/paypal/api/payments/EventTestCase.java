package com.paypal.api.payments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

public class EventTestCase {
	
	private static final Logger logger = Logger.getLogger(EventTestCase.class);

	public static final String RESOURCETYPE_AUTHORIZATION = "authorization";
	public static final String EVENT_ID = "WH-SDK-1S115631EN580315E-9KH94552VF7913711";
	public static final String EVENT_SUMMARY = "A successful payment authorization was created";
	public static final String QUERY_PARAMS = "?page_size=10&start_time=2014-11-11T03:00:01Z";
	public static final String EVENT_ID_SANDBOX = "WH-7J43600981584542E-9HV9453863280901M";


	public static Event createEvent() {
		Event event = new Event();
		event.setCreateTime(new java.util.Date().toString());
		event.setEventType(WebhooksInputData.availableEvents[1][0]);
		event.setId(EVENT_ID);
		event.setResourceType(RESOURCETYPE_AUTHORIZATION);
		event.setSummary(EVENT_SUMMARY);
		return event;
	}
	
	public static Event createSaleEvent() {
		Event event = new Event();
		event.setCreateTime(new java.util.Date().toString());
		event.setEventType(WebhooksInputData.availableEvents[4][0]);
		event.setId(EVENT_ID);
		event.setResourceType(RESOURCETYPE_AUTHORIZATION);
		event.setSummary(EVENT_SUMMARY);
		return event;
	}
	
	@Test(groups = "unit")
	public void testEventConstruction() {
		Event event = createEvent();
		Event saleEvent = createSaleEvent();
		
		Assert.assertEquals(event.getEventType(), WebhooksInputData.availableEvents[1][0]);
		Assert.assertEquals(event.getId(), EVENT_ID);
		Assert.assertEquals(event.getResourceType(), RESOURCETYPE_AUTHORIZATION);
		Assert.assertEquals(event.getSummary(), EVENT_SUMMARY);
		
		Assert.assertEquals(saleEvent.getEventType(), WebhooksInputData.availableEvents[4][0]);
		Assert.assertEquals(saleEvent.getId(), EVENT_ID);
		Assert.assertEquals(saleEvent.getResourceType(), RESOURCETYPE_AUTHORIZATION);
		Assert.assertEquals(saleEvent.getSummary(), EVENT_SUMMARY);
	}

	@Test(groups = "unit")
	public void testTOJSON() {
		Event event = createEvent();
		Assert.assertEquals(event.toJSON().length() == 0, false);
		logger.info("EventJSON = " + event.toJSON());
	}
	
	@Test(groups = "unit")
	public void testTOString() {
		Event event = createEvent();
		Assert.assertEquals(event.toString().length() == 0, false);
	}

	
	
	@Test(groups = "integration")
	public void testEventList() throws PayPalRESTException {
		logger.info("**** Create Event ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		logger.info("Generated Access Token = " + TokenHolder.accessToken);
		
		EventList list = Event.list(TokenHolder.accessToken, QUERY_PARAMS);
		logger.info("Response = " + list.toJSON());
		
		Assert.assertNotNull(list);
		Assert.assertTrue(list.getCount() >=1, "List contains an Event created during the timeframe");
		Assert.assertNotNull(list.getEvents().get(0).getId(), "Identifier of the Webhooks event resource.");
		Assert.assertNotNull(list.getEvents().get(0).getResourceType(), "Name of the resource contained in resource element.");
		Assert.assertNotNull(list.getEvents().get(0).getEventType(), "Event type that occurred on resource to trigger the Webhooks event.");
		Assert.assertNotNull(list.getEvents().get(0).getCreateTime(), "Time the resource was created.");
		Assert.assertNotNull(list.getEvents().get(0).getResource(), "Resource that is identified by resource_type element is Not Null");
		Assert.assertNotNull(list.getEvents().get(0).getLinks(), "HATEOAS links related to this call is Not Null");
	}
	
	@Test(groups = "integration")
	public void testEventResend() throws PayPalRESTException {
		logger.info("**** Resend Event ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		logger.info("Generated Access Token = " + TokenHolder.accessToken);

		Event event = Event.get(TokenHolder.accessToken, EVENT_ID_SANDBOX);
		Event eventResponse = event.resend(TokenHolder.accessToken);
		logger.info("Response = " + eventResponse.toJSON());

		Assert.assertEquals(event.getId(), EVENT_ID_SANDBOX);
		Assert.assertEquals(event.getResourceType(), "authorization");
		Assert.assertEquals(event.getEventType(), WebhooksInputData.availableEvents[2][0]);
		Assert.assertNotNull(event.getCreateTime(), "Time the resource was created.");
		Assert.assertNotNull(event.getResource(), "Resource that is identified by resource_type element is Not Null");
		Assert.assertNotNull(event.getLinks(), "HATEOAS links related to this call is Not Null");
	}
	
	@Test(groups = "integration")
	public void testEventGet() throws PayPalRESTException {
		logger.info("**** Get Event ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		logger.info("Generated Access Token = " + TokenHolder.accessToken);

		Event event = Event.get(TokenHolder.accessToken, EVENT_ID_SANDBOX);
		logger.info("Response = " + event.toJSON());
		
		Assert.assertEquals(event.getId(), EVENT_ID_SANDBOX);
		Assert.assertEquals(event.getResourceType(), "authorization");
		Assert.assertEquals(event.getEventType(), WebhooksInputData.availableEvents[2][0]);
		Assert.assertNotNull(event.getCreateTime(), "Time the resource was created.");
		Assert.assertNotNull(event.getResource(), "Resource that is identified by resource_type element is Not Null");
		Assert.assertNotNull(event.getLinks(), "HATEOAS links related to this call is Not Null");
	}
	
	/**
	 * To create a New Notification event, use the stub below. 
	 * When prompted for payerId, use the payerId obtained from executing approval URL in payment.create request
	 */
	@Test(groups = "integration")
	public void createNotification() throws PayPalRESTException, IOException {
		/*
		logger.info("**** Create Notification ****");
		TokenHolder.accessToken = new OAuthTokenCredential(WebhooksInputData.CLIENT_ID, WebhooksInputData.CLIENT_SECRET).getAccessToken();
		logger.info("Generated Access Token = " + TokenHolder.accessToken);
		
		Payment payment = createPaymentWithAuthorizeIntent();
		payment = payment.create(TokenHolder.accessToken);
		logger.info("Create Payment Response: " + payment);
		logger.info("Enter Payer ID:");
		
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		String payerId = in.readLine();
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		payment = payment.execute(TokenHolder.accessToken, paymentExecution);
		
		logger.info("Request = " + Payment.getLastRequest());
		logger.info("Response = " + Payment.getLastResponse());
		logger.info("Retrieved Payment status = " + payment.getState());
		*/
	}
	
	/**
	 * Creates a Payment with AuthorizeIntent and PayPal as Sale method 
	 */
	@Test(groups = "integration")
	public Payment createPaymentWithAuthorizeIntent() {
		Details details = new Details();
		details.setShipping("10");
		details.setSubtotal("80");
		details.setTax("10");

		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal("100");
		amount.setDetails(details);

		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://www.yahoo.com");
		redirectUrls.setReturnUrl("http://www.yahoo.com");


		ShippingAddress shippingAddress = new ShippingAddress();
		shippingAddress.setRecipientName("Paypal SDK DEV");
		
		shippingAddress.setCountryCode("US");
		shippingAddress.setPostalCode("95131");
		shippingAddress.setState("CA");
		shippingAddress.setCity("San Jose");
		shippingAddress.setLine1("2200 N 1st");
		shippingAddress.setLine2("building 17");
		
		ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<Item>();
    	Item item = new Item();
		item.setName("Rubik's Cube");
		item.setCurrency("USD");
		item.setPrice("8");
		item.setQuantity("10");
		item.setSku("TOYS-RC-548");
        items.add(item);
        
        itemList.setItems(items);
        itemList.setShippingAddress(shippingAddress);
        
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
        transaction.setItemList(itemList);
		transaction.setDescription("This is the payment transaction description.");
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		
		Payment payment = new Payment();
		payment.setIntent("authorize");
		payment.setPayer(payer);
		payment.setRedirectUrls(redirectUrls);
		payment.setTransactions(transactions);
		
		logger.info(payment.toJSON());
		return payment;
	}

}
