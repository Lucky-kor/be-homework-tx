package com.springboot.member.listener;

import com.springboot.helper.EmailSender;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

// 서비스를 대신하여 이벤트 처리를 하는 곳

@Slf4j // 로그 찍어줌 롬복꺼
@Component // 빈 등록
public class MemberEventListener {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;

    public MemberEventListener(MemberRepository memberRepository, EmailSender emailSender) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
    }

    @Async // 애를 사용하려면 enabledAsync 를 달아줘여함 어디다? 어플리케이션 실핼하는 클래스 엔트리포인트에.
    @EventListener
    public void handleMemberRegisteredEvent(MemberEvent memberEvent) throws Exception {
        try {
            // 이메일센더로 이메일 전송
            emailSender.sendEmail("이메일 보냈습니다.");
        } catch (MailSendException e) {
            // 이메일 전송 실패 시 레포지토리로 memberEvent에 저장된 회원 정보 삭제
            e.printStackTrace();
            log.error("이메일이 발송되지 않아서, 등록한 Member정보를 롤백합니다.");
            memberRepository.delete(memberEvent.getMember());

        }

    }

}
