package com.ecua3d.email.service;

import com.ecua3d.email.client.IQuoteClient;
import com.ecua3d.email.model.EmailLogEntity;
import com.ecua3d.email.model.enums.EmailDetails;
import com.ecua3d.email.repository.IEmailRepository;
import com.ecua3d.email.vo.FileResponse;
import com.ecua3d.email.vo.QuoteToEmailResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ResendEmailService implements IResendEmailService{

    @Autowired
    private IQuoteClient iQuoteClient;

    @Autowired
    private IEmailService iEmailService;

    @Autowired
    private IEmailRepository iEmailRepository;

    @Override
    public void reSendEmail(Integer emailLogId, Integer quoteId) {
        ResponseEntity<QuoteToEmailResponse> responseQuote = iQuoteClient.getByQuoteId(quoteId);
        try {
            iEmailService.sendEmail(emailLogId, quoteId, responseQuote.getBody().getEmail(), responseQuote.getBody().getName(),
                    responseQuote.getBody().getFiles().stream().map(FileResponse::getNameFile).toList());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void reSendEmailToCompany(Integer emailLogId, Integer quoteId) {
        ResponseEntity<QuoteToEmailResponse> responseQuote = iQuoteClient.getByQuoteId(quoteId);
        try {
            iEmailService.sendEmailToCompany(emailLogId, quoteId, responseQuote.getBody().getName(), responseQuote.getBody().getEmail(),
                    responseQuote.getBody().getPhone(),responseQuote.getBody().getFiles().stream().map(FileResponse::getNameFile).toList(),
                    responseQuote.getBody().getFilamentId(), responseQuote.getBody().getQualityId(), responseQuote.getBody().getComment());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    @Scheduled(cron = "*/15 * * * * *")
    public void reSendTasks() {
        List<EmailLogEntity> emailLogEntities = iEmailRepository.findByNotStatusEmail(String.valueOf(EmailDetails.SENT));
        for(EmailLogEntity emailLogEntity : emailLogEntities){
            final String emailType = emailLogEntity.getTypeEmail();
            final Integer emailTries = emailLogEntity.getChances();
            if(emailType.equals(String.valueOf(EmailDetails.toClient)) && emailTries < 3){
                this.reSendEmail(emailLogEntity.getEmailLogId(), emailLogEntity.getQuoteId());
            } else if (emailType.equals(String.valueOf(EmailDetails.toCompany)) && emailTries < 3) {
                this.reSendEmailToCompany(emailLogEntity.getEmailLogId(), emailLogEntity.getQuoteId());
            }
        }
    }

}
