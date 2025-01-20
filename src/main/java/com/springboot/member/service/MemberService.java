package com.springboot.member.service;

import com.springboot.event.CustomEvent;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.helper.EmailSender;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PostPersist;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  - 메서드 구현
 *  - DI 적용
 *  - Spring Data JPA 적용
 *  - 트랜잭션 적용
 */
@Slf4j
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final ApplicationEventPublisher eventPublisher;

    public MemberService(MemberRepository memberRepository,
                         EmailSender emailSender,
                         ApplicationEventPublisher eventPublisher) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
        this.eventPublisher = eventPublisher;
    }

    // 검증 후 로그 보내기
    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        // 회원 정보 저장
        Member savedMember = memberRepository.save(member);
        log.info("# Saved member");
        // @PostRemove 애너테이션은 Entity Delete
//         * TODO
//         *  - 현재 이메일 전송 중 5초 뒤에 예외가 발생합니다.
//         *  - 이메일 전송에 실패할 경우, 위에서(43번 라인) DB에 저장된 회원 정보를 삭제(rollback)하도록
//         *  코드를 구현하세요.
//         *
//         *  *****     추가 설명     ********
//         *  - 이메일이 비동기적으로 전송되기 때문에 MemberService에서 이메일을 전송하면 이메일 전송에 실패해도 회원 정보가 rollback이 되지 않습니다.
//         *  - 따라서 MemberService에서 이메일을 전송하는 것은 의미가 없을 가능성이 높습니다.
//         *  - Spring에서는 Event를 Publish(발행)하는 기능이 있으며, 회원 등록 자체를 이벤트로 보고 회원이 등록되었다는 이벤트를 애플리케이션 전체에
//         *  보낼 수 있습니다.
//         *      - MemberService에서 회원 등록 이벤트를 비동기적으로 먼저 보내고
//            이 이벤트를 리스닝(Listening)하는 곳에서 이메일을 보낼 수 있습니다.
//         *      - 이벤트 리스너(Event Listener)가 이메일을 보내고 실패할 경우 이미 저장된 회원 정보를 삭제할 수 있습니다.
//     *      - Spring에서는 @Async 애너테이션을 이용해서 비동기 작업을 손쉽게 처리할 수 있습니다.

        // 스레드 직접 관리하는 로직
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // submit은 비동기 실행 메서드
//        executorService.submit(() -> {
//            try {
//                // message 출력
//                emailSender.sendEmail(member.getEmail());
//            } catch (Exception e) {
//                // 실패 時 message 출력
//                memberRepository.delete(member);
//                log.error("MailSendException happened: ", e);
//                throw new RuntimeException(e);
//            }
//        });
        eventPublisher.publishEvent(new CustomEvent(member.getMemberId()));

        return savedMember;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
    public Member updateMember(Member member) {
        Member findMember = findVerifiedMember(member.getMemberId());

        Optional.ofNullable(member.getName())
                .ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getPhone())
                .ifPresent(phone -> findMember.setPhone(phone));
        Optional.ofNullable(member.getMemberStatus())
                .ifPresent(memberStatus -> findMember.setMemberStatus(memberStatus));

        return memberRepository.save(findMember);
    }

    @Transactional(readOnly = true)
    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }

    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);

        memberRepository.delete(findMember);
    }

    @Transactional(readOnly = true)
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =
                memberRepository.findById(memberId);
        Member findMember =
                optionalMember.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return findMember;
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
}
