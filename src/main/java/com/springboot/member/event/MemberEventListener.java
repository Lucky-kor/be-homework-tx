package com.springboot.member.event;

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
    private final MemberService  memberService;

    public MemberEventListener(EmailSender emailSender, MemberService memberService) {
        this.emailSender = emailSender;
        this.memberService = memberService;
    }

    @Async
    @EventListener
    public void handleMemberRegistration(MemberRegistrationEvent event){
        try {
            // 이메일 전송 로직
            emailSender.sendEmail("Send Email" + event.getEmail());
            log.info("전송 성공", event.getEmail());
        } catch (MailSendException | InterruptedException e){
            log.error("전송 실패", event.getEmail());

            // 전송 실패 시 회원 삭제
            memberService.deleteMember(event.getMemberId());
            log.info("롤백되었습니다.", event.getMemberId());
        }
    }
}
