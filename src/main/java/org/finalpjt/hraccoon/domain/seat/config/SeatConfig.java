package org.finalpjt.hraccoon.domain.seat.config;

import org.finalpjt.hraccoon.domain.seat.service.SeatService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class SeatConfig {
	private final SeatService seatService;

	public SeatConfig(SeatService seatService) {
		this.seatService = seatService;
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void scheduleTask() {
		seatService.resetSeatStatus();
	}
}
