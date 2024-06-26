package org.finalpjt.hraccoon.domain.seat.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import org.finalpjt.hraccoon.domain.user.data.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_status_no")
	private Long seatStatusNo;

	@Column(name = "seat_status_yn", nullable = false, columnDefinition = "boolean default false")
	private boolean seatStatusYn;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "seat_no", nullable = false)
	private Seat seat;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_no")
	private User user;

	@Builder
	public SeatStatus(Long seatStatusNo, boolean seatStatusYn, Seat seat, User user) {
		this.seatStatusNo = seatStatusNo;
		this.seatStatusYn = seatStatusYn;
		this.seat = seat;
		this.user = user;
	}

	public SeatStatus selectSeat(User user) {
		return SeatStatus.builder()
			.seatStatusNo(this.seatStatusNo)
			.seatStatusYn(true)
			.seat(this.seat)
			.user(user)
			.build();
	}

	public SeatStatus cancelSeat() {
		return SeatStatus.builder()
			.seatStatusNo(this.seatStatusNo)
			.seatStatusYn(false)
			.seat(this.seat)
			.user(null)
			.build();
	}
}