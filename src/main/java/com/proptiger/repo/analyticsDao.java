package com.proptiger.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proptiger.model.analytics;

public interface analyticsDao extends JpaRepository<analytics, String>{
	@Query(value="SELECT * FROM url.analytics WHERE url.analytics.hash=?",nativeQuery=true)
	public analytics findHash(String hashtext);
}
