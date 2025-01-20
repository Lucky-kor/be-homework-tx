package com.springboot.member.event;

import lombok.Getter;

@Getter
public class MemberRegistrationEvent {
    private final Long memberId;
    private final String email;

    public MemberRegistrationEvent(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }
}
