package com.springboot.member.listener;

import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class MemberEvent {

    private final Member member;

    public MemberEvent(Member member) {
        this.member = member;
    }
}
