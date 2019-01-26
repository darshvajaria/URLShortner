package com.proptiger.testing;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;

import com.proptiger.model.url;
import com.proptiger.repo.urlDao;
import com.proptiger.service.urlService;

import junit.framework.TestCase;

public class urlShortenerTesting extends TestCase{
	Timestamp t = new Timestamp(System.currentTimeMillis());
	
	url testUrl = new url();
	urlService testurlService = new urlService();
	
	/*public void testEncrypt() throws NoSuchAlgorithmException {
		String lurl = "https://alvinalexander.com/java/java-today-get-todays-date-now";
		String dname = "housing.com";
		String query = "/java/java-today-get-todays-date-now";
	    
		testUrl = testurlService.encrypt(lurl,query,dname);
		assertEquals("https://housing.com/6e7ceffe",testUrl.getSurl());
	}*/
	
	public void testisEmpty() {
		testUrl.setsecPassed((long)2000000);
		assertEquals(testurlService.isExpire(testUrl),true);
	}
	
	public void testgenerateDesiredHash() throws NoSuchAlgorithmException {
		String query = "/java/java-today-get-todays-date-now";
		String type = "SHA-256";
		assertEquals(testurlService.generateDesiredHash(query, type),"6e7ceffee15e882ce239efc76a8ee2ae8d627c8171badcb5844557f99b8e344e");
	}
	
	public void testqString() throws MalformedURLException {
		String lurl = "https://alvinalexander.com/java/java-today-get-todays-date-now";
		assertEquals(testurlService.qString(lurl),"/java/java-today-get-todays-date-now");
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(urlShortenerTesting.class);
	}
}
