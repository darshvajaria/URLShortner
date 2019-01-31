package com.proptiger.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proptiger.model.url;

@Repository
public interface urlDao extends JpaRepository<url, String>{
	public url findBylurlAnddname(String lurl, String domainName);
}
