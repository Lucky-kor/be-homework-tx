package com.springboot.event;

import com.springboot.helper.EmailSender;
import com.springboot.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j // 로그를 명확하게 남길수있음
@Component
public class MemberRegisteredEventListener {
    private final MemberService memberService;
    private final EmailSender emailSender;

    public MemberRegisteredEventListener(EmailSender emailSender, MemberService memberService) {
        this.emailSender = emailSender;
        this.memberService = memberService;
    }

    @Async // 이메일 전송을 비동기적으로 처리하여 회원등록 후 즉시 응답을 반환할수 있도록한다.
    @EventListener // 이벤트를 리스닝하는 메서드에 사용
    public void MemberRegisteredEvent(MemberRegisteredEvent event) throws Exception{
        try {
            emailSender.sendEmail("any email message");
            log.info("Email sent successfully to :");
        } catch (MailSendException e) {
            memberService.deleteMember(event.getMember().getMemberId());
            // 이메일 전송 실패 시 회원을 삭제하거나 적절히 처리하는 로직
            log.error("이메일 실패했음 다시하셈");
        }
    }

}
