package kr.ac.ks.cs_web_back.domain.member.fixture;

import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.model.Member;

import java.time.LocalDate;

public class MemberFixture {

    public static Member memberFixture() {
        return Member.builder()
                .email("example@ks.ac.kr")
                .password("examplePassword1234!")
                .username("exampleUser")
                .birthdate(LocalDate.parse("2002-09-05"))
                .number("055-663-5140")
                .build();
    }

}
