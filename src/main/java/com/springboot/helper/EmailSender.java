package com.springboot.helper;

import com.springboot.helper.email.EmailSendable;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    private final EmailSendable emailSendable;


    // 실제 구현체 : MockExceptionEmailSendable
    public EmailSender(EmailSendable emailSendable) {
        this.emailSendable = emailSendable;
    }

    public void sendEmail(String message) throws InterruptedException, MailSendException {
        emailSendable.send(message);
    }
}
