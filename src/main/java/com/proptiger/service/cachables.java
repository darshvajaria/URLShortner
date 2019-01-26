package com.proptiger.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import com.proptiger.model.url;
import com.proptiger.repo.urlDao;

public class cachables {
	@Autowired
	private urlDao urlDao;
	
	Timestamp t = new Timestamp(System.currentTimeMillis());
	
	@Cacheable(value="cacheCheck",unless="#result==null") 
	public url check(String lurl) {    /*Query is executed to check if a tuple exists for the 
		                               given longURL.*/ 
		url u = urlDao.getUserByLongURL(lurl);
		if(u != null) {
		   u.setsecPassed(t.getTime());
		   urlDao.save(u);
		}
		return u;
	}
}
