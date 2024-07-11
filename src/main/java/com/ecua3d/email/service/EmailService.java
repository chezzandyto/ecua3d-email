package com.ecua3d.email.service;

import com.ecua3d.email.client.ICorporativeClient;
import com.ecua3d.email.vo.FilamentToQuoteResponse;
import com.ecua3d.email.vo.QualityToQuoteResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
@Log4j2
public class EmailService implements IEmailService{
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ICorporativeClient iCorporativeClient;

    @Value("${quote.email.company-name}")
    String companyName;
    @Value("${quote.email.contact-number}")
    String contactNumber;
    @Value("${quote.email.logo-email}")
    String logoEmail;
    @Value("${quote.email.logo-whatsapp}")
    String logoWhatsapp;
    @Value("${quote.email.logo-company}")
    String logoCompany;
    @Value("${quote.email.mail-company}")
    String mailCompany;
    @Value("${quote.email.mail-quote}")
    String mailQuote;

    public void sendEmail(String to, String name, List<String> fileNames) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String content = loadHtmlContent("html/email-template.html");
        content = replaceHtmlParameters(content,name,companyName,fileNames,contactNumber);
        helper.setFrom(mailCompany);
        helper.setTo(to);
        helper.setSubject("Cotizacion ecua3D");
        helper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

    public void sendEmailToCompany(Integer quoteId, String name, String email, String phone, List<String> fileNames, Integer filamentId, Integer qualityId, String comment) throws MessagingException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        String content = loadHtmlContent("html/email-received-template.html");
        content = replaceHtml2Parameters(content,quoteId,name,email,phone,fileNames, filamentId, qualityId, comment);
        helper.setFrom(mailCompany);
        helper.setTo(mailQuote);
        helper.setSubject("Nueva Cotizacion ecua3D");
        helper.setText(content, true);
        javaMailSender.send(mimeMessage);
    }

    private String loadHtmlContent(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.getURI()));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String replaceHtmlParameters(String htmlContent, String name, String companyName, List<String> fileNames, String contactNumber) {
        final String fileHtmlBase = "<h3 style=\"Margin:0;line-height:34px;mso-line-height-rule:exactly;font-family:Imprima, Arial, sans-serif;font-size:20px;font-style:normal;font-weight:bold;color:#2D3142\">${file}</h3>";
        htmlContent = htmlContent.replace("${name}", name.toUpperCase());
        htmlContent = htmlContent.replace("${company_name}", companyName);
        htmlContent = htmlContent.replace("${contact_number}", contactNumber);
        htmlContent = htmlContent.replace("${imgLogoEmail}", logoEmail);
        htmlContent = htmlContent.replace("${imgLogoWhatsapp}", logoWhatsapp);
        htmlContent = htmlContent.replace("${imgLogoCompany}", logoCompany);
        StringBuilder fileHtml = new StringBuilder();
        for (String file:fileNames){
            fileHtml.append(fileHtmlBase.replace("${file}", file));
        }
        htmlContent = htmlContent.replace("${files}", fileHtml.toString());
        return htmlContent;
    }

    private String replaceHtml2Parameters(String htmlContent, Integer quoteId, String name, String email, String phone, List<String> fileNames, Integer filamentId, Integer qualityId, String comment) {
        final String fileHtmlBase = "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:Imprima, Arial, sans-serif;line-height:27px;color:#2D3142;font-size:18px\">${file}</p>";
        String materialName = "";
        String colorName = "";
        String qualityName = "";
//        String token = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
        try{
            ResponseEntity<FilamentToQuoteResponse> responseFilament = iCorporativeClient.getByFilamentId(filamentId);
            materialName = responseFilament.getBody().getMaterial();
            colorName = responseFilament.getBody().getColor();
            ResponseEntity<QualityToQuoteResponse> responseQuality = iCorporativeClient.getByQualityId(qualityId);
            qualityName = responseQuality.getBody().getNameQuality();
        } catch (Exception e){
            log.error("Can not retrieve from Corporative : {}" , e.getMessage());
        }
        if (comment == null){
            comment = "";
        }
        htmlContent = htmlContent.replace("${quote-id}", quoteId.toString());
        htmlContent = htmlContent.replace("${name}", name.toUpperCase());
        htmlContent = htmlContent.replace("${phone}", phone);
        htmlContent = htmlContent.replace("${email}",email);
        htmlContent = htmlContent.replace("${material}", materialName);
        htmlContent = htmlContent.replace("${color}",colorName);
        htmlContent = htmlContent.replace("${quality}", qualityName);
        htmlContent = htmlContent.replace("${comment}", comment);
        htmlContent = htmlContent.replace("${imgLogoEmail}", logoEmail);
        htmlContent = htmlContent.replace("${imgLogoCompany}", logoCompany);
        StringBuilder fileHtml = new StringBuilder();
        for (String file:fileNames){
            fileHtml.append(fileHtmlBase.replace("${file}", file));
        }
        htmlContent = htmlContent.replace("${files}", fileHtml.toString());
        return htmlContent;
    }
}
