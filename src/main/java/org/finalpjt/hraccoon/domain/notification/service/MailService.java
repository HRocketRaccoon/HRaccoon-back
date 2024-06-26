package org.finalpjt.hraccoon.domain.notification.service;

import java.util.HashMap;
import java.util.Map;

import org.finalpjt.hraccoon.domain.notification.data.entity.EmailNotification;
import org.finalpjt.hraccoon.domain.notification.repository.EmailNotificationRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender javaMailSender;

	private final SpringTemplateEngine templateEngine;

	private final EmailNotificationRepository emailNotificationRepository;

	private static final String EMAIL_TITLE_PREFIX = "[HRaccoon] ";

	@Async
	public void sendNotificationEmail(EmailNotification notification) throws MessagingException {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			/* 제목, 수신자 설정 */
			messageHelper.setSubject(EMAIL_TITLE_PREFIX + notification.getEmailNotificationSubject());
			messageHelper.setTo(notification.getEmailNotificationAddress());

			/* html 동적 데이터 설정 */
			Map<String, String> emailValues = new HashMap<>();
			emailValues.put("content", notification.getEmailNotificationContent());
			String text = setContext(emailValues);
			messageHelper.setText(text, true);
			messageHelper.addInline("logo", new ClassPathResource("static/images/logo.png"));

			javaMailSender.send(message);
			log.info("[SUCCESS] Email successfully sent to {}", notification.getEmailNotificationAddress());
		} catch (MessagingException e) {
			log.error("[ERROR] Failed to send email to {}", notification.getEmailNotificationAddress(), e);
		}
	}

	private String setContext(Map<String, String> emailValues) {
		Context context = new Context();
		emailValues.forEach(context::setVariable);
		return templateEngine.process("email/index", context);
	}

	@Transactional
	public void saveEmailNotification(EmailNotification notification) {
		emailNotificationRepository.save(notification);
	}
}
