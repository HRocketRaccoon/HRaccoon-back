package org.finalpjt.hraccoon.domain.seat.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Seat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_no")
	private Long seatNo;

	@Column(name = "seat_location", nullable = false)
	private String seatLocation;

	@Column(name = "seat_office", nullable = false)
	private String seatOffice;

	@Builder
	public Seat(Long seatNo, String seatLocation, String seatOffice) {
		this.seatNo = seatNo;
		this.seatLocation = seatLocation;
		this.seatOffice = seatOffice;
	}
}
