package com.paypal.api.payments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.paypal.base.rest.PayPalRESTException;

public class RefundTestCase {

	private static final Logger logger = Logger.getLogger(RefundTestCase.class);

	public static final String CAPTUREID = "12345";

	public static final String ID = "12345";

	public static final String PARENTPAYMENT = "12345";

	public static final String SALEID = "12345";

	public static final String STATE = "Approved";

	public static final Amount AMOUNT = AmountTestCase.createAmount("100.00");

	public static final String CREATEDTIME = "2013-01-17T18:12:02.347Z";

	@BeforeClass
	public void beforeClass() throws PayPalRESTException {
		File testFile = new File(".",
				"src/test/resources/sdk_config.properties");
		Refund.initConfig(testFile);
	}

	public static Refund createRefund() {
		List<Links> links = new ArrayList<Links>();
		links.add(LinksTestCase.createLinks());
		Refund refund = new Refund();
		refund.setCaptureId(CAPTUREID);
		refund.setId(ID);
		refund.setParentPayment(PARENTPAYMENT);
		refund.setSaleId(SALEID);
		refund.setState(STATE);
		refund.setAmount(AMOUNT);
		refund.setCreateTime(CREATEDTIME);
		refund.setLinks(links);
		return refund;
	}

	@Test(groups = "unit")
	public void testConstruction() {
		Refund refund = createRefund();
		Assert.assertEquals(refund.getId(), ID);
		Assert.assertEquals(refund.getCaptureId(), CAPTUREID);
		Assert.assertEquals(refund.getParentPayment(), PARENTPAYMENT);
		Assert.assertEquals(refund.getSaleId(), SALEID);
		Assert.assertEquals(refund.getState(), STATE);
		Assert.assertEquals(refund.getAmount().getCurrency(),
				AmountTestCase.CURRENCY);
		Assert.assertEquals(refund.getCreateTime(), CREATEDTIME);
		Assert.assertEquals(refund.getLinks().size(), 1);
	}

	@Test(groups = "integration")
	public void testGetRefund() throws PayPalRESTException {
		if (ObjectHolder.refundId == null) {
			new SaleTestCase().testSaleRefundAPI();
		}
		logger.info("**** Get Refund ****");
		logger.info("Generated Access Token = " + TokenHolder.accessToken);
		try {
			Refund refund = Refund.get(TokenHolder.accessToken, ObjectHolder.refundId);
			logger.info("Refund Get Request = " + Refund.getLastRequest());
			logger.info("Payment Get Response = " + Refund.getLastResponse());
			logger.info("Refund ID = " + refund.getId());

		} catch (PayPalRESTException e) {
			Assert.fail();
		}
	}

	@Test(groups = "integration", dependsOnMethods = { "testGetRefund" })
	public void testGetRefundForNull() {
		logger.info("**** Get Refund For Null ****");
		logger.info("Generated Access Token = " + TokenHolder.accessToken);
		try {
			Refund refund = Refund.get(TokenHolder.accessToken, null);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e != null,
					"IllegalArgument exception not thrown for null Refund Id");
		} catch (PayPalRESTException e) {
			Assert.fail();
		}
	}

	@Test(groups = "unit")
	public void testRefundUnknownFileConfiguration() {
		try {
			Refund.initConfig(new File("unknown.properties"));
		} catch (PayPalRESTException e) {
			Assert.assertEquals(e.getCause().getClass().getSimpleName(),
					"FileNotFoundException");
		} catch (ConcurrentModificationException e) {}
	}

	@Test(groups = "unit")
	public void testRefundInputStreamConfiguration() {
		try {
			File testFile = new File(".",
					"src/test/resources/sdk_config.properties");
			FileInputStream fis = new FileInputStream(testFile);
			Refund.initConfig(fis);
		} catch (PayPalRESTException e) {
			Assert.fail("[sdk_config.properties] stream loading failed");
		} catch (FileNotFoundException e) {
			Assert.fail("[sdk_config.properties] file is not available");
		} catch (ConcurrentModificationException e) {}
	}

	@Test(groups = "unit")
	public void testRefundPropertiesConfiguration() {
		try {
			File testFile = new File(".",
					"src/test/resources/sdk_config.properties");
			Properties props = new Properties();
			FileInputStream fis = new FileInputStream(testFile);
			props.load(fis);
			Refund.initConfig(props);
		} catch (FileNotFoundException e) {
			Assert.fail("[sdk_config.properties] file is not available");
		} catch (IOException e) {
			Assert.fail("[sdk_config.properties] file is not loaded into properties");
		} catch (ConcurrentModificationException e) {}
	}

	@Test(groups = "unit")
	public void testTOJSON() {
		Refund refund = createRefund();
		Assert.assertEquals(refund.toJSON().length() == 0, false);
	}

	@Test(groups = "unit")
	public void testTOString() {
		Refund refund = createRefund();
		Assert.assertEquals(refund.toString().length() == 0, false);
	}

}
