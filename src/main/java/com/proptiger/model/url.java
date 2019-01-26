package com.proptiger.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="url",schema="url")
public class url implements Serializable{
	private static final long serialVersionUID = 1L;

	@Column(name = "longURL")
	private String lurl;
	
	@Id
	@Column(name = "shortURL")
	private String surl;
    
	@Column(name = "domainName")
	private String dname;
	
	@JsonIgnore
	@Column(name = "timeStamp")
	private Long secPassed; 
	
	public String getLurl() {
		return lurl;
	}

	public void setLurl(String lurl) {
		this.lurl = lurl;
	}
	
	public String getSurl() {
		return surl;
	}

	public void setSurl(String surl) {
		this.surl = surl;
	}
	
    public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}
	
	public Long getsecPassed() {
		return secPassed;
	}
	public void setsecPassed(Long secPassed) {
		this.secPassed = secPassed;
	}
}
