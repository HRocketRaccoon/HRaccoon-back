package org.finalpjt.hraccoon.domain.approval.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
	Page<Approval> findByUser_UserNo(Long userNo, Pageable pageable);

	Page<Approval> findByApprovalAuthority(String userId, Pageable pageable);

	Approval findByUser_UserNo(Long userNo);

	@Query("select a from Approval a join fetch a.user join fetch a.approvalDetail where a.user.userTeam = :userTeam and a.approvalStatus=:approvalStatus")
	List<Approval> findByUserTeamWithUserAndApprovalDetail(String userTeam, ApprovalStatus approvalStatus);

	@Query("SELECT a FROM Approval a WHERE a.user.userNo = :userNo " +
		"AND a.approvalStatus = :approvalStatus " +
		"AND a.approvalDetail.approvalDetailStartDate >= :startOfMonth " +
		"AND a.approvalDetail.approvalDetailEndDate <= :endOfMonth")
	List<Approval> findByUserNoAndApprovalStatusAndDateBetween(Long userNo,
		ApprovalStatus approvalStatus,
		LocalDateTime startOfMonth,
		LocalDateTime endOfMonth);

	List<Approval> findByUserAndApprovalStatus(User user, ApprovalStatus approvalStatus);
}
