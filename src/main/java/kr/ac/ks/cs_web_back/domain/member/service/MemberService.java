package kr.ac.ks.cs_web_back.domain.member.service;

import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void createMember(String email, String password, String username, String birthdate, String number) {
        Member member = Member.of(email, password, username, birthdate, number);
        memberRepository.save(member);
    }
}
