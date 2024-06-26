package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.helper.EmailSender;
import com.springboot.helper.event.RegistrationEvent;
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

import java.util.Optional;

/**
 *  - 메서드 구현
 *  - DI 적용
 *  - Spring Data JPA 적용
 *  - 트랜잭션 적용
 */
@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
   //private final EmailSender emailSender;
    private final ApplicationEventPublisher eventPublisher;

    public MemberService(MemberRepository memberRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.memberRepository = memberRepository;
        this.eventPublisher=eventPublisher;
    }

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        Member savedMember = memberRepository.save(member);
        //DB에 저장해놓고 기다림, 검증이 완료되면 실행
        log.info("# Saved member");
        eventPublisher.publishEvent(new RegistrationEvent(savedMember));
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
