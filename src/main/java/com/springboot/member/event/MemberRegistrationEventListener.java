package com.springboot.member.event;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.helper.EmailSender;
import com.springboot.member.repository.MemberRepository;
import com.springboot.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
//
//@Slf4j
//@Component
//public class MemberRegistrationEventListener {
//    private final EmailSender emailSender;
//    private final MemberService memberService;
//
//    public MemberRegistrationEventListener(EmailSender emailSender, MemberService memberService) {
//        this.emailSender = emailSender;
//        this.memberService = memberService;
//    }
//
//   @Async
//   @EventListener
////   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void listen(MemberRegistrationEvent event) throws Exception{
//        try {
//            String message = "회원 가입 되었습니다.";
//            emailSender.sendEmail(message);
//            //문자 전송 관련 코드 작성
//        } catch (MailSendException e) {
//            e.printStackTrace();
//            log.error("이메일이 발송되지 않아 등록한 회원 정보를 롤백합니다.");
//            memberService.deleteMember(event.getMember().getMemberId());
//            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
//        }
//
//    }
//}


@Slf4j
@Component
public class MemberRegistrationEventListener {
    private final EmailSender emailSender;
    private final MemberRepository memberRepository;

    public MemberRegistrationEventListener(EmailSender emailSender, MemberRepository memberRepository) {
        this.emailSender = emailSender;
        this.memberRepository = memberRepository;
    }

    @Async
    @EventListener
//   @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMemberRegisteredEvent(MemberRegistrationEvent event){
        Long memberId = event.getMember().getMemberId();

        try {
            emailSender.sendEmail("회원 가입 되었습니다.");
            //문자 전송 관련 코드 작성
        } catch (MailSendException | InterruptedException e) {
            log.error("이메일이 발송되지 않아 등록한 회원 정보를 롤백합니다.", e);
            memberRepository.deleteById(event.getMember().getMemberId());
            throw new RuntimeException(e);
        }

    }
}