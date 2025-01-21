package com.springboot.member.event;
import com.springboot.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

//이벤트 객체는 과거형으로 만들어야함
// ex 회원가입이 되고 나서 email을 보내기 때문에. ("회원가입이 되고 나서" -> 과거형)

@Getter
@NoArgsConstructor(force = true) //강제로 만듭니다.
public class MemberCreatedEvent {
    private final Member member; //member 개체를 받음

    public MemberCreatedEvent(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

}
