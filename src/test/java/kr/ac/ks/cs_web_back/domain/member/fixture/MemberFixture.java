package kr.ac.ks.cs_web_back.domain.member.fixture;

import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.model.Member;

import java.time.LocalDate;

public class MemberFixture {

    public static Member memberFixture() {
        MemberCreateRequest request = new MemberCreateRequest(
                "example@ks.ac.kr",
                "password123",
                "username",
                LocalDate.parse("2002-09-05"),
                "010-1234-5678"
        );
        return Member.of(request);
    }

}
