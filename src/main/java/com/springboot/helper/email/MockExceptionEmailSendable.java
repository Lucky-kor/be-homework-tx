package com.springboot.helper.email;

import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;

public class MockExceptionEmailSendable implements EmailSendable {
    @Override
    public void send(String message) throws InterruptedException {
        Thread.sleep(10000L);
        throw new MailSendException("error while send email");
    }
}
