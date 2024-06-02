package org.finalpjt.hraccoon.domain.code.repository;

import org.finalpjt.hraccoon.domain.code.data.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<Code, String> {
	@Query("select c.codeNo from Code c where c.codeName = :codeName")
	String findCodeNoByCodeName(String codeName);
}