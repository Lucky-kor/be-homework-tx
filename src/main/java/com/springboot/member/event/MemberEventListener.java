package com.springboot.member.event;

import com.springboot.helper.EmailSender;
import com.springboot.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@EnableAsync
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
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMail(MemberEvent memberEvent) throws Exception {
        try {
            emailSender.sendEmail("any email message");
        } catch (MailSendException e) {
            log.error("MailSendException happened: ", e);

            memberService.deleteMember(memberEvent.getMember().getMemberId());
        }
    }
}
