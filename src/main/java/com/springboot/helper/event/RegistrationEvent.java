package com.springboot.helper.event;

import com.springboot.member.entity.Member;


public class RegistrationEvent {
    private final Member member;

    public RegistrationEvent(Member member){
    this.member = member;
}

    public Member getMember() {
        return member;
    }

}
