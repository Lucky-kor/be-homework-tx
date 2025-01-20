package com.springboot.helper.email;

import org.springframework.mail.MailSendException;

public class MockExceptionEmailSendable implements EmailSendable {
    @Override
    // 5초 후 예외 발생 메세지 보내기
    public void send(String message) throws InterruptedException {
        Thread.sleep(5000L);
        throw new MailSendException("error while send email");
    }
}
