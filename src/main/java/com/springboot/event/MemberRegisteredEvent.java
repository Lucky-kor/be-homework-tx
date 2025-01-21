package com.springboot.event;

import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberRegisteredEvent {
    private final Member member;
}
