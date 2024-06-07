package org.finalpjt.hraccoon.domain.seat.repository;

import java.util.List;
import java.util.Optional;

import org.finalpjt.hraccoon.domain.seat.data.entity.SeatStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatStatusRepository extends CrudRepository<SeatStatus, Long> {
	@Modifying
	@Query("update SeatStatus s SET s.seatStatusYn = false")
	void resetSeatStatus();

	@Query("select s from SeatStatus s  join fetch s.seat where s.seat.seatOffice=:seatOffice and s.seatStatusYn=false")
	List<SeatStatus> findBySeatOfficeWithSeat(String seatOffice);

	Optional<SeatStatus> findBySeatSeatNoAndUserUserNo(Long seatNo, Long userNo);

	Optional<SeatStatus> findByUserUserNo(Long userNo);
}
