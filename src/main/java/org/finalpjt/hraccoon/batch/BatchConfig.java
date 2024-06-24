package org.finalpjt.hraccoon.batch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.persistence.EntityManagerFactory;

import org.finalpjt.hraccoon.domain.attendance.data.entity.Attendance;
import org.finalpjt.hraccoon.domain.attendance.service.AttendanceService;
import org.finalpjt.hraccoon.domain.user.data.entity.User;
import org.finalpjt.hraccoon.domain.user.repository.UserRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private final EntityManagerFactory entityManagerFactory;

	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private UserRepository userRepository;


	// checkInJob - 출근 처리

	@Bean
	public Job checkInJob() {
		log.info(">>> simpleJob1");
		return new JobBuilder("checkInJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(checkInStep())
			.build();
	}

	@Bean
	@JobScope
	public Step checkInStep() {
		log.info(">>> simpleStep1");
		return new StepBuilder("checkInStep", jobRepository)
			.<User, Attendance>chunk(10, transactionManager)
			.reader(userReader())
			.processor(userProcessor())
			.writer(userWriter())
			.build();
	}

	@Bean
	public JpaCursorItemReader<User> userReader() {
		return new JpaCursorItemReaderBuilder<User>()
			.name("userReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("SELECT u FROM User u")
			.build();
	}

	@Bean
	public ItemProcessor<User, Attendance> userProcessor() {
		log.info(">>> ItemProcessor");
		return user -> {
			LocalDate today = LocalDate.now();
			List<Attendance> existingAttendances = attendanceService.findAttendancesByUserAndDate(user, today);
			// 해당 날짜에 결재 승인된 직원 데이터가 근태에 없는 경우만 (오늘 batch로 추가할 데이터)
			if (existingAttendances.isEmpty()) {
				LocalDateTime startTime = LocalDateTime.of(today, getRandomLocalTime(8,10));
				return Attendance.builder()
					.user(user)
					.attendanceDate(today)
					.attendanceStatus("출근")
					.attendanceStartTime(startTime)
					.build();
			} else {
				// 이미 오늘 날짜의 근태 데이터가 있는 경우 null을 반환하여 스킵하도록 처리
				return null;
			}
		};
	}

	@Bean
	public ItemWriter<Attendance> userWriter() {
		return new JpaItemWriterBuilder<Attendance>()
			.entityManagerFactory(entityManagerFactory)
			.build();
	}

	// checkOutJob - 퇴근 처리

	@Bean
	public Job checkOutJob() {
		return new JobBuilder("checkOutJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(checkOutStep())
			.build();
	}

	@Bean
	@JobScope
	public Step checkOutStep() {
		return new StepBuilder("checkOutStep", jobRepository)
			.tasklet(userCheckOutTasklet(),transactionManager)
			.build();
	}

	@Bean
	public Tasklet userCheckOutTasklet() {
		LocalDate today = LocalDate.now();
		return (contribution, chunkContext) -> {
			List<Attendance> attendances = attendanceService.findByAttendanceDate(today);
			attendances.forEach(attendance -> {
				// 출근인 attendance만 퇴근으로 변경 후 퇴근시간을 랜덤으로 설정 (16~17시 사이)
				// 출근이 아닌 경우는 무시 (결재 승인된 경우: 출장, 휴가, 외근 등)
				if(attendance.getAttendanceStatus().equals("출근")) {
					LocalDateTime endTime = LocalDateTime.of(today, getRandomLocalTime(16,17));
					attendance.updateAttendanceEndTime(endTime);
				}
			});
			return RepeatStatus.FINISHED;
		};
	}

	// clearAttendanceTableJob - 들어간 테이블 비우기 (테스트용)

	@Bean
	public Job clearAttendanceTableJob() {
		return new JobBuilder("clearAttendanceTableJob",jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(clearAttendanceTableStep())
			.build();
	}

	@Bean
	@JobScope
	public Step clearAttendanceTableStep() {
		return new StepBuilder("clearAttendanceTableStep", jobRepository)
			.tasklet(userCheckOutTasklet(),transactionManager)
			.build();
	}

	@Bean
	public Tasklet clearAttendanceTableTasklet() {
		return (contribution, chunkContext) -> {
			log.info("Clearing Today's Attendance table...");

			LocalDate today = LocalDate.now();
			List<User> users = userRepository.findAll();
			AtomicInteger cnt = new AtomicInteger();

			for(User user : users) {
				List<Attendance> attendances = attendanceService.findAttendancesByUserAndDate(user,today);
				// 오늘 batch로 추가한 데이터
				if(attendances.isEmpty()) {
					attendances.forEach(attendance -> {
						attendanceService.delete(attendance);
						cnt.getAndIncrement();
					});
				}
			}

			log.info("deleted {} attendances", cnt.get());

			return RepeatStatus.FINISHED;
		};
	}

	private LocalTime getRandomLocalTime(int start, int end) {
		int hour = ThreadLocalRandom.current().nextInt(start, end); // Random hour between 8 and 9
		int minute = ThreadLocalRandom.current().nextInt(0, 60); // Random minute between 0 and 59
		int second = ThreadLocalRandom.current().nextInt(0, 60); // Random second between 0 and 59
		return LocalTime.of(hour, minute, second);
	}

}

