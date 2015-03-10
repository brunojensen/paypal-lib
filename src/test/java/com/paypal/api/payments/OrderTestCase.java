package com.paypal.api.payments;

import java.io.File;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.PayPalResource;

/**
 * NOTE: Tests that use this class must be ignored when run in an automated environment because executing an order will require approval via the executed payment's approval_url.
 */
@Test(enabled=false)
public class OrderTestCase {

	private static final Logger logger = Logger
			.getLogger(OrderTestCase.class);
	
	private static Order order = null;
	
	public static final String ID = "O-2HT09787H36911800";

	@BeforeClass
	public void beforeClass() throws PayPalRESTException {
		File testFile = new File(".",
				"src/test/resources/sdk_config.properties");
		PayPalResource.initConfig(testFile);
		String clientID = "EBWKjlELKMYqRNQ6sYvFo64FtaRLRR5BdHEESmha49TM";
		String clientSecret = "EO422dn3gQLgDbuwqTjzrFgFtaRLRR5BdHEESmha49TM";
		TokenHolder.accessToken = new OAuthTokenCredential(clientID,
				clientSecret).getAccessToken();
	}
	
	@Test(groups = "integration")
	public void testGetOrder() throws PayPalRESTException {
		order = Order.get(TokenHolder.accessToken, ID);
	}
	
	@Test(groups = "integration", dependsOnMethods = { "testGetOrder" })
	public void testAuthorize() throws PayPalRESTException {
		Authorization authorization = order.authorize(TokenHolder.accessToken);
		Assert.assertEquals(authorization.getState(), "Pending");
	}
	
	@Test(groups = "integration", dependsOnMethods = { "testAuthorize" })
	public void testCapture() throws PayPalRESTException {
		Capture capture = order.capture(TokenHolder.accessToken, CaptureTestCase.createCapture());
		Assert.assertEquals(capture.getState(), "Pending");
	}
	
	@Test(groups = "integration", dependsOnMethods = { "testCapture" })
	public void testDoVoid() throws PayPalRESTException {
		order = order.doVoid(TokenHolder.accessToken);
		Assert.assertEquals(order.getState(), "voided");
	}
}
