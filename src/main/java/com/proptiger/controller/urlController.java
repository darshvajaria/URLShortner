package com.proptiger.controller;

import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.proptiger.model.analytics;
import com.proptiger.model.report;
import com.proptiger.model.url;
import com.proptiger.service.urlService;


@RestController
public class urlController {
	@Autowired
	private urlService urlService;
	
	@RequestMapping(value="/createShortURL",method=RequestMethod.POST)
	public url longToShorturl(@RequestBody url URL) throws NoSuchAlgorithmException, MalformedURLException, InterruptedException {
	    return urlService.longToShorturl(URL);   /*SHA-256 hash of longURL is 
	                                               generated and url entity is 
	                                               saved in db.*/
	}
	
	@RequestMapping(value="/getLongURL",method=RequestMethod.GET)
	public url shortToLongurl(@RequestParam("surl") String shortURL) throws MalformedURLException, NoSuchAlgorithmException{
		//urlService.updateGETs(); //increments GET requests.
		return urlService.shortToLongurl(shortURL);
	}
	
	@RequestMapping(value="/analytics",method = RequestMethod.GET)
	public analytics getAnalytics(@RequestParam("surl") String shortURL) throws NoSuchAlgorithmException, MalformedURLException{
		return urlService.getAnalysis(shortURL); /*output is clicks for every registered longURL.
		                                           Manipulation such as most clicked, least clicked
		                                           can be done by simple MySQL queries.*/
	}
	
	@RequestMapping(value="/report",method = RequestMethod.GET)
	public List<report> getReport() {
		return urlService.getReport();           //Report enlists number of GET and POST requests for every day.
	}
	
	@RequestMapping(value="/report/{date}",method = RequestMethod.GET)
	public report dateSpecificReport(@PathVariable Date date) {
		return urlService.dateSpecificReport(date); //to fetch date specific report.  
	}
}
