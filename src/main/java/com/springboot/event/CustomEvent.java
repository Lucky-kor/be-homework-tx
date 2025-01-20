package com.springboot.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomEvent { // 이벤트 객체
    private long memberId;
}
