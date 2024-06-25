package com.springboot.helper.event;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.helper.EmailSender;
import com.springboot.member.entity.Member;

import com.springboot.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationEventHandler {
    private final EmailSender emailSender;
    private final MemberService memberService;

    @Async
    @EventListener
    public void handle(RegistrationEvent event) throws Exception{
            Member member = event.getMember();
            try {
                emailSender.sendEmail("Hi there");

            } catch (MailSendException e) {
                log.error("이메일 발송 되지 않아 rollback을 실행합니다.");
                memberService.deleteMember(member.getMemberId());

            }
    }
}
