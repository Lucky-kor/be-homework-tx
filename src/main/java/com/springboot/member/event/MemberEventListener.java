package com.springboot.member.event;


import com.springboot.helper.EmailSender;
import com.springboot.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component //이벤트를 관리하는 곳은 spring 컨테이너가 주체이기 때문입니다.
public class MemberEventListener {
    public MemberEventListener(MemberRepository memberRepository, EmailSender emailSender) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
    }

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;

    @Async
    @EventListener
    public void handleMemberEvent(MemberCreatedEvent memberCreatedEvent) {
        try {
             emailSender.sendEmail("회원가입을 추가합니다. 인증 번호를 확인하세요.");

        } catch (Exception e) {
            log.error("mailSenderException : ",e);
            memberRepository.delete(memberCreatedEvent.getMember());
            throw new RuntimeException();
        }
    }
}
