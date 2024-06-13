package org.finalpjt.hraccoon.domain.approval.repository;

import java.util.List;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.finalpjt.hraccoon.domain.approval.data.enums.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
	Page<Approval> findByUser_UserNo(Long userNo, Pageable pageable);

	Approval findByUser_UserNo(Long userNo);

	@Query("select a from Approval a join fetch a.user join fetch a.approvalDetail where a.user.userTeam = :userTeam and a.approvalStatus=:approvalStatus")
	List<Approval> findByUserTeamWithUserAndApprovalDetail(String userTeam, ApprovalStatus approvalStatus);
}
