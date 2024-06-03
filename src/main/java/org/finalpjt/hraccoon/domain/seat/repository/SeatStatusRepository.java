package org.finalpjt.hraccoon.domain.seat.repository;

import java.util.List;

import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeatStatusRepository extends JpaRepository<SeatStatus,Long> {

	@Query("select s from SeatStatus s  join fetch s.seat where s.seat.seatOffice=:seatOffice and s.seatStatusYn=false")
	List<SeatStatus> findBySeatOfficeWithSeat(String seatOffice);
}
