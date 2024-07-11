package com.ecua3d.email.listeners;

import com.ecua3d.email.event.SendEmailEvent;
import com.ecua3d.email.event.SendEmailToCompanyEvent;
import com.ecua3d.email.service.IEmailService;
import com.ecua3d.email.utils.JsonUtils;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class SendEmailEventListener {

    @Autowired
    private IEmailService iEmailService;

    @KafkaListener(topics = "sendEmail-topic")
    public void handleOrdersNotifications(String message) throws MessagingException, IOException {
        var emailEvent = JsonUtils.fromJson(message, SendEmailEvent.class);
        iEmailService.sendEmail(emailEvent.to(),emailEvent.name(), emailEvent.fileNames());
        log.info("Received email event: {} ",message);
    }
    @KafkaListener(topics = "sendEmailToCompany-topic")
    public void handleOrdersNotifications1(String message) throws MessagingException, IOException {
        var emailEvent = JsonUtils.fromJson(message, SendEmailToCompanyEvent.class);
        iEmailService.sendEmailToCompany(emailEvent.quoteId(),emailEvent.name(), emailEvent.email(),
                emailEvent.phone(), emailEvent.fileNames(), emailEvent.fileId(), emailEvent.qualityId(),
                emailEvent.comment());
        log.info("Received email to Company event: {}",message);
    }
}
