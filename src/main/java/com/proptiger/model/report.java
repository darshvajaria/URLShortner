package com.proptiger.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="report",schema="url")
public class report {
	
	@Id
	@Column(name="date")
	private Date date;
	
	@Column(name="POSTs")
	private int postRequests;
	
	@Column(name="GETs")
	private int getRequests;

	public Date getLd() {
		return date;
	}

	public void setLd(Date ld) {
		this.date = ld;
	}

	public int getPostRequests() {
		return postRequests;
	}

	public void setPostRequests(int postRequests) {
		this.postRequests = postRequests;
	}

	public int getGetRequests() {
		return getRequests;
	}

	public void setGetRequests(int getRequests) {
		this.getRequests = getRequests;
	}
	
}
