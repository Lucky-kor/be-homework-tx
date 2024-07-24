package com.springboot.helper;

import com.springboot.helper.email.EmailSendable;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    private final EmailSendable emailSendable;

    public EmailSender(EmailSendable emailSendable) {
        this.emailSendable = emailSendable;
    }

    public void sendEmail(String message) throws InterruptedException, MailSendException {
        emailSendable.send(message); // 이 메서드 따라가보면 5초후 예외를 발생시키는 로직이 있음.
    }
}
