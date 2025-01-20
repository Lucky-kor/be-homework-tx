package com.springboot.member.event;

import com.springboot.helper.EmailSender;
import com.springboot.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class EmailNotificationListener {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    public EmailNotificationListener(MemberRepository memberRepository,
                                     EmailSender emailSender) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
    }

    @Async
    @EventListener
    public void handleMemberRegisteredEvent(MemberRegisteredEvent event) {
        Long memberId = event.getMemberId();

        try {
            emailSender.sendEmail("any email message");
        } catch (Exception e) {
            log.error("MailSendException happened: ", e);
            memberRepository.deleteById(memberId);
            System.out.println("삭제 완료");
            throw new RuntimeException(e);
        }
    }
}
