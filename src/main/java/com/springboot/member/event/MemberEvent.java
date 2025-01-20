package com.springboot.member.event;

import com.springboot.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberEvent {
    private Member member;

    public MemberEvent(Member member) {
        this.member = member;
    }
}
