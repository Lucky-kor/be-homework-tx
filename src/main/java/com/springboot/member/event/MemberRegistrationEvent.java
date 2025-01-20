package com.springboot.member.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 회원이 등록되었을 때 발행할 이벤트 객체
public class MemberRegistrationEvent {
    private long memberId;
}
