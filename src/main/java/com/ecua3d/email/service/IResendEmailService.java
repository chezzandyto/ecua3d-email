package com.ecua3d.email.service;

public interface IResendEmailService {
    void reSendEmail(Integer emailLogId, Integer quoteId);
    void reSendEmailToCompany(Integer emailLogId, Integer quoteId);
    void reSendTasks();
}
