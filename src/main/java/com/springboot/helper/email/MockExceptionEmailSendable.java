package com.springboot.helper.email;

import lombok.Getter;
import org.springframework.mail.MailSendException;

@Getter
public class MockExceptionEmailSendable implements EmailSendable {
    @Override
    public void send(String message) throws InterruptedException {
        Thread.sleep(5000L);
        throw new MailSendException("error while send email");
    }
}
