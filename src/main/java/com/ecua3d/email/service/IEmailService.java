package com.ecua3d.email.service;

import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface IEmailService {
    void sendEmail(Integer emailLogId, Integer quoteId, String to, String name, List<String> fileNames) throws MessagingException, IOException;
    void sendEmailToCompany(Integer emailLogId, Integer quoteId, String name, String email, String phone,
                                   List<String> fileNames, Integer filamentId, Integer qualityId,
                                   String comment) throws MessagingException, IOException;
}
