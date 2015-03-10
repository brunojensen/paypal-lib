package com.paypal.base;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.paypal.base.exception.SSLConfigurationException;

public class SSLUtilTest {

	@Test
	public void getSSLContextTest() throws SSLConfigurationException {
		Assert.assertNotNull(SSLUtil.getSSLContext(null));
	}

	@Test
	public void setupClientSSLTest() throws SSLConfigurationException {
		Assert.assertNotNull(SSLUtil.setupClientSSL(
				UnitTestConstants.CERT_PATH, UnitTestConstants.CERT_PASSWORD));
	}

	@Test(expectedExceptions = SSLConfigurationException.class)
	public void setupClientSSLExceptionTest() throws Exception {
		Assert.assertNotNull(SSLUtil.setupClientSSL("src/sdk_cert.p12",
				UnitTestConstants.CERT_PASSWORD));
	}

	@Test(expectedExceptions = SSLConfigurationException.class)
	public void setupClientSSLNoSuchKeyExceptionTest() throws Exception {
		Assert.assertNotNull(SSLUtil.setupClientSSL("src/sdk_cert.p12",
				null));
	}
}
