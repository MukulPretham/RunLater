package com.example.demo.Services.coreServices;

import com.example.demo.Entities.EmailJobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Sends a simple text email.
     */
    public void sendEmail(EmailJobs job) throws Exception {

        logger.info("Starting email send for jobId={}", job.getId());

        var message = javaMailSender.createMimeMessage();
        var helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setFrom(job.getFromEmail());
        helper.setTo(job.getToEmail());
        helper.setSubject(job.getSubject());
        helper.setText(job.getBody(), "text/html".equals(job.getContentType()));
        javaMailSender.send(message);
        logger.info("Email sent successfully for jobId={}", job.getJob().getId());
    }
}
