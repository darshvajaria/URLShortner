package com.proptiger.repo;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.proptiger.model.report;

public interface reportDao extends JpaRepository<report, Date>{
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO url.report(date,POSTs,GETs) VALUES(?,0,0) ON DUPLICATE KEY UPDATE POSTs = POSTS+1",nativeQuery=true)
	public void updatePOSTs(Date ld);
	
	@Transactional
	@Modifying
	@Query(value="INSERT INTO url.report(date,POSTs,GETs) VALUES(?,0,0) ON DUPLICATE KEY UPDATE GETs = GETs+1",nativeQuery=true)
	public void updateGETs(Date ld);
}
