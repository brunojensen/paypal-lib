package com.paypal.api.payments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.paypal.base.rest.JSONFormatter;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

import java.util.Random;

public class BillingAgreementTestCase {
	private String id = null;
	private Agreement agreement = null;

	public static Agreement loadAgreement() {
	    try {
		    BufferedReader br = new BufferedReader(new FileReader("src/test/resources/billingagreement_create.json"));
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append(System.getProperty("line.separator"));
	            line = br.readLine();
	        }
	        br.close();
	        return JSONFormatter.fromJSON(sb.toString(), Agreement.class);
	        
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return null;
	    }
	}
	
	@BeforeTest(groups = "integration")
	public void setUp() throws PayPalRESTException {
		String clientID = "AUASNhD7YM7dc5Wmc5YE9pEsC0o4eVOyYWO9ezXWBu2XTc63d3Au_s9c-v-U";
		String clientSecret = "EBq0TRAE-4R9kgCDKzVh09sm1TeNcuY-xJirid7LNtheUh5t5vlOhR0XSHt3";
		TokenHolder.accessToken = new OAuthTokenCredential(clientID,
				clientSecret).getAccessToken();
	}
	
	@Test(groups = "integration")
	public void testCreateAgreement() throws PayPalRESTException, MalformedURLException, UnsupportedEncodingException {
		// first, set up plan to be included in the agreement
		Plan plan = new Plan();
		plan.setId("P-0V2939118D268823YFYLVH4Y");
		
		// construct an agreement and add the above plan to it
		Agreement agreement = loadAgreement();
		agreement.setPlan(plan);
		agreement.setShippingAddress(null);
		agreement = agreement.create(TokenHolder.accessToken);
		
		this.id = agreement.getId();
		Assert.assertNull(agreement.getId());
		Assert.assertNotNull(agreement.getToken());
		Assert.assertEquals(this.id, agreement.getId());
	}
	
	@Test(groups = "integration", dependsOnMethods = {"testCreateAgreement"})
	public void testExecuteAgreement() throws PayPalRESTException {
		Agreement agreement =  new Agreement();
		agreement.setToken("EC-2CD33889A9699491E");
		this.agreement = agreement.execute(TokenHolder.accessToken);
		Assert.assertEquals("I-ASXCM9U5MJJV", this.agreement.getId());
	}
	
	@Test(enabled=false, groups = "integration", dependsOnMethods = {"testExecuteAgreement"})
	public void testUpdateAgreement() throws PayPalRESTException {
		// set up changes to update with randomized string as description
		Agreement newAgreement = new Agreement();
		Random random = new Random();
		String newDescription = String.valueOf(random.nextLong());
		newAgreement.setDescription(newDescription);
		
		// create a patch object and set value to the above updated agreement
		Patch patch = new Patch();
		patch.setOp("replace");
		patch.setPath("/");
		patch.setValue(newAgreement);
		
		// build PatchRequest which is an array of Patch
		List<Patch> patchRequest = new ArrayList<Patch>();
		patchRequest.add(patch);
		
		Agreement updatedAgreement = this.agreement.update(TokenHolder.accessToken, patchRequest);
		Assert.assertNotSame(agreement.getDescription(), updatedAgreement.getDescription());
		Assert.assertEquals(updatedAgreement.getId(), this.agreement.getId());
		Assert.assertEquals(newAgreement.getDescription(), updatedAgreement.getDescription());
	}
	
	@Test(groups = "integration", dependsOnMethods = {"testExecuteAgreement"})
	public void testRetrieveAgreement() throws PayPalRESTException {
		Agreement agreement = Agreement.get(TokenHolder.accessToken, "I-ASXCM9U5MJJV");
		Assert.assertEquals("I-ASXCM9U5MJJV", agreement.getId());
		Assert.assertEquals("2015-02-19T08:00:00Z", agreement.getStartDate());
		Assert.assertNotNull(agreement.getPlan());
	}
	
	@Test(groups = "integration")
	public void testSearchAgreement() throws PayPalRESTException {
		Date startDate = new GregorianCalendar(2014, 10, 1).getTime();
		Date endDate = new GregorianCalendar(2014, 10, 14).getTime();
		AgreementTransactions transactions = Agreement.transactions(TokenHolder.accessToken, "I-9STXMKR58UNN", startDate, endDate);
		Assert.assertNotNull(transactions);
		Assert.assertNotNull(transactions.getAgreementTransactionList());
	}
	
	/**
	 * The following tests are disabled due to them requiring an active billing agreement.
	 * 
	 * @throws PayPalRESTException
	 */
	@Test(enabled=false, groups = "integration", dependsOnMethods = {"testRetrieveAgreement"})
	public void testSuspendAgreement() throws PayPalRESTException {
		String agreementId = "";
		Agreement agreement = Agreement.get(TokenHolder.accessToken, agreementId);
		System.out.println("agreement state: " + agreement.getPlan().getState());
		
		AgreementStateDescriptor agreementStateDescriptor = new AgreementStateDescriptor();
		agreementStateDescriptor.setNote("Suspending the agreement.");
		agreement.suspend(TokenHolder.accessToken, agreementStateDescriptor);
		
		Agreement suspendedAgreement = Agreement.get(TokenHolder.accessToken, "I-ASXCM9U5MJJV");
		Assert.assertEquals("SUSPENDED", suspendedAgreement.getPlan().getState());
	}
	
	@Test(enabled=false, groups = "integration", dependsOnMethods = {"testSuspendAgreement"})
	public void testReactivateAgreement() throws PayPalRESTException {
		String agreementId = "";
		Agreement agreement = Agreement.get(TokenHolder.accessToken, agreementId);
		
		AgreementStateDescriptor stateDescriptor = new AgreementStateDescriptor();
		stateDescriptor.setNote("Re-activating the agreement");
		agreement.reActivate(TokenHolder.accessToken, stateDescriptor);
	}
	
	@Test(enabled=false, groups = "integration", dependsOnMethods = {"testReactivateAgreement"})
	public void testCancelAgreement() throws PayPalRESTException {
		String agreementId = "";
		Agreement agreement = Agreement.get(TokenHolder.accessToken, agreementId);
		
		AgreementStateDescriptor stateDescriptor = new AgreementStateDescriptor();
		stateDescriptor.setNote("Cancelling the agreement");
		agreement.cancel(TokenHolder.accessToken, stateDescriptor);
	}
}
