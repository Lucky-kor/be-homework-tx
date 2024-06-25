package com.springboot.helper.eventListener;

import com.springboot.helper.EmailSender;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import com.springboot.member.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
@AllArgsConstructor
public class MailSenderService {
    private MemberRepository memberRepository;
    private final EmailSender emailSender;

    public void sendEmail(Member member) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {

            try {
                emailSender.sendEmail("any email message");
            } catch (Exception e) {
                log.error("MailSendException happened: ", e);
                memberRepository.delete(member);
                throw new RuntimeException(e);
            }
        });

        log.info("{} deleteMember", member.getMemberId());
    }
}
