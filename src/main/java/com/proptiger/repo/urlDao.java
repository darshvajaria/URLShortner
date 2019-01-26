package com.proptiger.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.proptiger.model.url;

public interface urlDao extends JpaRepository<url, String>{
	@Query(value = "SELECT * FROM url.url WHERE url.url.longURL=?1",nativeQuery=true)
	public url getUserByLongURL(String lurl);
}
