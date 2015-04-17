package com.gan4x4.greedyalarm.test.android;



import org.apache.http.HttpStatus;

import com.gan4x4.greedyalarm.objects.HttpRequestResponse;
import com.gan4x4.greedyalarm.test.TestHelper;

import android.content.Intent;
import android.test.AndroidTestCase;

public class HttpRequestResponseTest extends AndroidTestCase {
	private String raw_response;
	
		
	public HttpRequestResponseTest() {
		this(" HttpRequestResponseTest");
	}
	
	public HttpRequestResponseTest(String name) {
		super();
		setName(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		raw_response = TestHelper.readFileFromAssets(this,"assets/response.txt");
		assertFalse(raw_response.length() == 0);
		
	}
	
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testConstructor() {
		HttpRequestResponse r = new HttpRequestResponse(raw_response);
		r.setHttpCode(200); // No error
		assertTrue("Response not success",r.isSuccess());
		assertEquals("Bad code",10,r.getCode());
		assertEquals("Bad message","Cookie send",r.getMessage());
		//assertEquals("Bad time",123456789l,r.getServerTime());
		
		try {
			assertEquals("Bad data on key1","data1",r.getDataByKey("k1"));
			assertEquals("Bad data on key2","2333",r.getDataByKey("k2"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Some keys not found");
		}
		
		
	}
	
	public void testParceble(){
		HttpRequestResponse r = new HttpRequestResponse(raw_response);
		r.setHttpCode(HttpStatus.SC_OK);
		Intent dt = new Intent();
		dt.putExtra("response", r);
		HttpRequestResponse rc = dt.getParcelableExtra("response");
		assertEquals(r.getHttpCode(),rc.getHttpCode());
	}
	
	

}
