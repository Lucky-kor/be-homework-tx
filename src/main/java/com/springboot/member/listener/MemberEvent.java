package com.springboot.member.listener;

import com.springboot.member.entity.Member;
import lombok.Getter;

// 회원 등록 이벤트를 정의합니다. 이 이벤트는 회원 ID와 이메일 주소를 포함.
// 이 이벤트로 전송된 이메일과 회원정보가 넘어옴.

@Getter
public class MemberEvent {
    private final Member member;

    public MemberEvent(Member member) {
        this.member = member;
    }
}
