package com.hackerrank.api.repository;

import com.hackerrank.api.model.Scan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScanRepository extends JpaRepository<Scan, Long> {
    @Query("SELECT s FROM Scan s WHERE s.domainName=:domainName ")
    List<Scan> findAllByDomainName(@Param("domainName") String domainName, Sort sort);
}
