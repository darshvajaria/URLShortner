package com.proptiger.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proptiger.model.url;

public interface urlDao extends JpaRepository<url, String>{
	@Query(value = "SELECT * FROM url.url WHERE url.longURL=?1 AND url.domainName=?2",nativeQuery=true)
	public url getUserByLongURLandDname(String lurl, String domainName);
}
