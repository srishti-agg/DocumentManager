package com.example.securedoc.service.Impl;

import com.example.securedoc.exception.APIException;
import com.example.securedoc.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.example.securedoc.utils.EmailUtils.getEmailMessage;
import static com.example.securedoc.utils.EmailUtils.getResetPassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String NEW_USER_ACCOUNT_VERIFICATION="New User Account Verification";
    private static final String PASSWORD_RESET_REQUEST = "Password Reset Request";

    private final JavaMailSender sender;
    @Value("${spring.mail.verify.host}")
    public String host;
    @Value("${spring.mail.username}")
    public String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String email, String token){
        try {
            var message = new SimpleMailMessage();
            message.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(email);
            System.out.println("Token: "+token+" email: "+email);
            message.setText(getEmailMessage(name, host, token));
            sender.send(message);

        }catch (Exception e){
            log.error(e.getMessage());
            throw new APIException("Unable to send email");
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(String name, String email, String token){

        try {
            var message = new SimpleMailMessage();
            message.setSubject(PASSWORD_RESET_REQUEST);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(getResetPassword(name, host, token));
            sender.send(message);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new APIException("Unable to send email");
        }
    }

}
