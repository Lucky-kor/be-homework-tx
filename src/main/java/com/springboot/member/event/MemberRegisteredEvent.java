package com.springboot.member.event;

import lombok.Getter;

@Getter
public class MemberRegisteredEvent {
    private final Long memberId;

    public MemberRegisteredEvent(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
