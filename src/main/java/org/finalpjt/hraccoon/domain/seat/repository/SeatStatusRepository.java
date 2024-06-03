package org.finalpjt.hraccoon.domain.seat.repository;

import java.util.List;

import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeatStatusRepository extends JpaRepository<SeatStatus,Long> {

	@Query("select s from SeatStatus s  join fetch s.seat where s.seat.seatOffice=:seatOffice and s.seatStatusYn=false")
	List<SeatStatus> findBySeatOfficeWithSeat(String seatOffice);

	@Query("select s from SeatStatus s  join fetch s.seat where s.seat.seatOffice=:seatOffice and substring(s.seat.seatLocation, 3, 1) = :floor")
	List<SeatStatus> findBySeatOfficeAndFloorWithSeat(String seatOffice, String floor);

	@Query("select s.user from SeatStatus s join fetch s.user where s.seatStatusNo=:seatStatusNo")
	List<User> findUserBySeatStatusNoWithUser(Long seatStatusNo);
}
