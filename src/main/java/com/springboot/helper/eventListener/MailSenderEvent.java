package com.springboot.helper.eventListener;

import com.springboot.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MailSenderEvent {
    private Member member;
}
