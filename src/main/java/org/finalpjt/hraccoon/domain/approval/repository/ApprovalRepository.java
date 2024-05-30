package org.finalpjt.hraccoon.domain.approval.repository;

import java.util.List;

import org.finalpjt.hraccoon.domain.approval.data.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApprovalRepository extends JpaRepository<Approval,Long> {

	@Query("select a from Approval a join fetch a.user join fetch a.approvalDetail where a.user.userTeam = :userTeam and a.approvalStatus='승인'")
	List<Approval> findByUserTeamWithUserAndApprovalDetail(String userTeam);

}
