package com.gan4x4.greedyalarm.test.serverside;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.test.AndroidTestCase;

import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.test.TestHelper;

import junit.framework.TestCase;

public class HttpRequestResponseTest extends AndroidTestCase {

	
	public HttpRequestResponseTest() {
		this("HttpRequestResponseNetworkBasedTest");
	}
	
	public HttpRequestResponseTest(String name) {
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	
	public void testSendData(){
		String api= "http://greedyalarm.com/api/sync_auth_by_id_token.php";
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", "999");
		String cookie = "";
		HttpRequestResponse r = HttpRequestResponse.doRequest(api, params, cookie);
		assertEquals("Bad http code" + r.getHttpCode(),200,r.getHttpCode());
		assertEquals("Bad Cookie" + r.getCookie(),"---",r.getCookie());
	}
	
	
	
}
