package com.springboot.helper.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
public class EmailConfiguration {
    @Bean
    public EmailSendable emailSendable() {
        return new MockExceptionEmailSendable();
    }
}
