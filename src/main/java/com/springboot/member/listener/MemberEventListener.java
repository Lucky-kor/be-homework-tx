package com.springboot.member.listener;

import com.springboot.helper.EmailSender;
import com.springboot.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class MemberEventListener {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;

    public MemberEventListener(MemberRepository memberRepository, EmailSender emailSender) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
    }

    @Async
    @EventListener
    public void handleMemberEvent(MemberEvent memberEvent) {
        try {
            emailSender.sendEmail("any email message");
        } catch (Exception e) {
            log.error("MailSendException happened: ", e);
            memberRepository.delete(memberEvent.getMember());
            throw new RuntimeException(e);
        }
    }
}
