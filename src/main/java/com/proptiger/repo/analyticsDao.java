package com.proptiger.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proptiger.model.analytics;

@Repository
public interface analyticsDao extends JpaRepository<analytics, String>{
	@Query(value="SELECT * FROM url.analytics WHERE url.analytics.hash=?1 AND url.analytics.domainName=?2",nativeQuery=true)
	public analytics findHashandDname(String hashtext, String domainName);
}
