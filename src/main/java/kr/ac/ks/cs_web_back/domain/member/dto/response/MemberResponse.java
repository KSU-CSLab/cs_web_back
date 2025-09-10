package kr.ac.ks.cs_web_back.domain.member.dto.response;

import kr.ac.ks.cs_web_back.domain.member.model.Member;

import java.time.LocalDate;

public record MemberResponse(
        String email,
        String nickname,
        LocalDate birthdate,
        String number
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getEmail(),
                member.getUsername(),
                member.getBirthdate(),
                member.getNumber()
        );
    }
}
