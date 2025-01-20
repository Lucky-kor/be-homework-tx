package com.springboot.event;

import com.springboot.helper.EmailSender;
import com.springboot.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberEventListener {
    private final EmailSender emailSender;
    private final MemberService memberService;

    public MemberEventListener(EmailSender emailSender, MemberService memberService) {
        this.emailSender = emailSender;
        this.memberService = memberService;
    }

    @Async
    @EventListener
    public void eventListen(CustomEvent event) throws Exception {
        try {
            emailSender.sendEmail("메일 발송");
        } catch (MailSendException e) {
            memberService.deleteMember(event.getMemberId());
            log.error("이메일 발송 실패 : 회원 정보를 삭제합니다.");
        }
    }

}
