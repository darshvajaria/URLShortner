package com.proptiger.service;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.proptiger.model.analytics;
import com.proptiger.model.report;
import com.proptiger.model.url;
import com.proptiger.repo.analyticsDao;
import com.proptiger.repo.reportDao;
import com.proptiger.repo.urlDao;

@Service
public class urlService {		
	
	@Autowired
	private urlDao urlDao;
	@Autowired
	private analyticsDao analyticsDao;
	@Autowired 
	private reportDao reportDao;
	@Autowired
	private ApplicationContext appContext;
	
	Timestamp t = new Timestamp(System.currentTimeMillis());
	
	@Async
	private void updatePOSTs() {                   /*Registers number of POST requests in a day.
	                                                LocalDate.now() is converted into mySQL Date format
	                                                and then query is executed to increment requests.*/  
		LocalDate ld = java.time.LocalDate.now();
		Date date = Date.valueOf(ld);
		reportDao.updatePOSTs(date);
	}
	
	@Async
	private void updateGETs() {
		LocalDate ld = java.time.LocalDate.now();
		Date date = Date.valueOf(ld);
 		reportDao.updateGETs(date);
	}
	
	@Cacheable(value="cacheCheck",unless="#result==null")
	public url check(String lurl) {         /*Query is executed to check if a tuple exists for the 
		                                      given longURL.*/ 
		url u = urlDao.getUserByLongURL(lurl);
		if(u != null) {
		   u.setsecPassed(t.getTime());
		   urlDao.save(u);
		}
		return u;
	}
	
	public String qString(String lurl) throws MalformedURLException{ 
		URL u = new URL(lurl);
		return u.getFile();                 //eg: /user/login/db1?authenticate=right
	}
	
	public url longToShorturl(url url) throws NoSuchAlgorithmException, MalformedURLException {
		String lurl = url.getLurl(); 
		String query = qString(lurl);                      //returns File of longURL.
		String domainName;
		
		if(url.getDname()==null)domainName = "makkan.com"; /*if domain name is not provided as input then 
                                                            makkan.com is considered.*/
        else domainName = url.getDname(); 
		
		
		
		updatePOSTs();                                     //increments POST requests.
		
		urlService tempService = appContext.getBean(urlService.class); //to enable caching
		url u = tempService.check(lurl);
		if(u!=null)return u;
		
		/*Initially first 8 characters of hashtext is considered. Checked if already used.
		 * If used and not expired(a shortURL expires after a year. The expiration time can be manipulated by user) 
		 * then consider next 8 bits. If used but expired then used that 8 bit hashtext. Follow the procedure until 
		 * we get a 8 character hashtext. Re-usability is achieved. 
		 * */
		String hashtext = generateDesiredHash(query,"SHA-256");
		int i, l = hashtext.length(),bucket = 8;  /*if all the 54 combinations of 8 bits are considered then go for 9 bits. 
		                                            Number of bits currently considered is bucket.*/
		String surl = "https://"+domainName+"/", probableSURL;
		url temp = new url();
		boolean urlFlag = false;
		
		for(i=0;i<=l-bucket;i++) {   
		    probableSURL = surl+hashtext.substring(i,i+bucket); 
		    urlFlag = urlDao.exists(probableSURL);
		    if(urlFlag) {
		       url U = urlDao.findOne(probableSURL);
		       if(isExpire(U)) {              
		    	  U.setsecPassed(t.getTime()); 
		    	  U.setSurl(probableSURL);
		    	  U.setLurl(lurl); 
		    	  U.setDname(domainName);
		    	  temp = U;
		    	  urlDao.save(U);
		    	  break;
		       }
		       if(i == l-bucket) {
		    	  bucket++; 
		    	  i = -1;
		       }
		       continue;
		    }
		    else {
		        url U = new url();
		        U.setLurl(lurl);
		        U.setSurl(probableSURL);
		        U.setsecPassed(t.getTime());
		        U.setDname(domainName);
		        temp = U;
		        urlDao.save(U);
		        break;
		    }
		}
		return temp;
	}
	
	public url shortToLongurl(String shortURL) throws MalformedURLException, NoSuchAlgorithmException {
		updateGETs(); //increments GET requests.
		
		/*Given shortURL is first searched in urlDao. 
		 * If the url is valid then, expiry is checked.
		 * */
		url temp = urlDao.findOne(shortURL); 
		
		if(temp != null){
		   if(isExpire(temp)) {
			  temp.setLurl("short url is expired. Generate shortURL for longURL again.");
			  return temp;
		   }
		   updateAnalytics(temp.getLurl()); //as the url is valid update analytics.
		   return temp;
		}
		temp = new url();
		temp.setLurl("url doesn't exist.");
		return temp;
	}
	
	private void updateAnalytics(String longURL) throws NoSuchAlgorithmException {
		//hash is generated to cut down the time complexity of search. 
		String hashtext = generateDesiredHash(longURL,"MD5");
		   
	    analytics a = analyticsDao.findHash(hashtext);
	    if(a != null) {   
		   a.setClicks(a.getClicks()+1); 
	    }
	    else {
		    a = new analytics();
		    a.setLongURL(longURL);
		    a.setHash(hashtext);
		    a.setClicks((long)1); 
	    }
	    analyticsDao.save(a);
	}
	
	public analytics getAnalysis(String shortURL) throws NoSuchAlgorithmException {
		url u = urlDao.findOne(shortURL);
		if(u != null){
		   String s = u.getLurl();	
		   String hashtext = generateDesiredHash(s,"MD5");
		   return analyticsDao.findHash(hashtext);
		}
		analytics a = new analytics();
		a.setLongURL("url doesn't exist");
		return a;	
	}
	
	public List<report> getReport() {
		return reportDao.findAll();
	}
	
	public report dateSpecificReport(Date date) {
		if(reportDao.exists(date)) {
		   return reportDao.findOne(date);
		}
		
		report r = new report();
		r.setGetRequests(0);
		r.setPostRequests(0);
		r.setLd(date);
		return r;
	}
	
	public String generateDesiredHash(String query, String type) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(type);
		byte[] encodedhash = md.digest(query.getBytes());
		BigInteger no = new BigInteger(1, encodedhash);
		String hashtext = no.toString(16);
		return hashtext;
	}
	
	public boolean isExpire(url u) {
		return ((t.getTime()-u.getsecPassed())/10000 > 3153600); //31536000 seconds in year.
	}
}
