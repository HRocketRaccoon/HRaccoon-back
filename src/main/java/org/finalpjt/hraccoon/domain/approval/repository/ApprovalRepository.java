package org.finalpjt.hraccoon.domain.approval.repository;

import java.util.List;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
	List<Approval> findByUser_UserNo(Long userNo);

	Approval findByApprovalNo(Long approvalNo);

	@Query("select a from Approval a join fetch a.user join fetch a.approvalDetail where a.user.userTeam = :userTeam and a.approvalStatus=:approvalStatus")
	List<Approval> findByUserTeamWithUserAndApprovalDetail(String userTeam, ApprovalStatus approvalStatus);

	@Query("SELECT a FROM Approval a WHERE a.user.userTeam = :userTeam AND a.approvalAuthority = :approvalAuthority")
	List<Approval> findByApprovalAuthorityAndUserTeam(@Param("userTeam") String userTeam,
		@Param("approvalAuthority") String approvalAuthority);
}
