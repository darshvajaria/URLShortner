package com.proptiger.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proptiger.model.analytics;

@Repository
public interface analyticsDao extends JpaRepository<analytics, String>{
	public analytics findByhashAndomainName(String hashtext, String domainName);
}
