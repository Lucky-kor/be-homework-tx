package com.springboot.helper.eventListener;

import com.springboot.helper.EmailSender;
import com.springboot.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class MailSenderEventListener {
    private final EmailSender emailSender;
    private final MemberService memberService;
    private final MailSenderService mailSenderExceptionService;

    @EventListener
    public void listen(MailSenderEvent event) throws Exception {
        try {
            String message = "회원 가입을 축하합니다.";
            emailSender.sendEmail(message);
        } catch (MailSendException e) {
            e.printStackTrace();
            log.error("이메일 전송이 실패되었습니다. 롤백합니다.");
            memberService.deleteMember(event.getMember().getMemberId());
            throw new RuntimeException(e);
        }
//        mailSenderExceptionService.sendEmail(event.getMember());
    }
}
