package com.springboot.event;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.helper.EmailSender;
import com.springboot.helper.email.MockExceptionEmailSendable;
import com.springboot.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.logging.ErrorManager;

@Slf4j
@Component
public class EventEmailListener {
        private final EmailSender emailSender;
        private final MemberService memberService;

    public EventEmailListener(EmailSender emailSender, MemberService memberService) {
        this.emailSender = emailSender;
        this.memberService = memberService;
    }

    //    비동기 처리
//    try - catch로
    @Async
    @EventListener
    public void handleEventEmail(EventEmail event) throws Exception{
        try {
            String message = "회원 가입 되었습니다.";
            emailSender.sendEmail(message);
        } catch (Exception e){
            e.printStackTrace();
            log.error("이메일이 발송되지 않아 등록한 회원 정보를 롤백합니다.");

            memberService.deleteMember(event.getMember().getMemberId());
            throw new RuntimeException(e);
        }
    }
}
