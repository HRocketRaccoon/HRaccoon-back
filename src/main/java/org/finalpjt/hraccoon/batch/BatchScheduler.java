package org.finalpjt.hraccoon.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BatchScheduler {

	private final JobLauncher jobLauncher;
	private final Job checkInJob;
	private final Job checkOutJob;
	private final Job clearAttendanceTableJob;

	// @Scheduled(cron = "0 32 0 * * ?") // 매일 00시 15분에 실행 (테스트용)
	@Scheduled(cron = "0 0 0 * * MON-FRI") // 매일 00시에 실행 (주말 제외)
	public void runCheckInJob() {
		try {
			jobLauncher.run(checkInJob, new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Scheduled(cron = "0 33 0 * * ?") // 매일 00시 15분에 실행 (테스트용)
	@Scheduled(cron = "0 0 17 * * MON-FRI") // 매일 17시에 실행 (주말 제외)
	public void runCheckOutJob() {
		try {
			jobLauncher.run(checkOutJob, new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Scheduled(cron = "0 37 0 * * ?") // 매일 00시 15분에 실행 (테스트용)
	public void runClearAttendanceTableJob() {
		try {
			jobLauncher.run(clearAttendanceTableJob, new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
