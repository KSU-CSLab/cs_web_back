package kr.ac.ks.cs_web_back.domain.member.service;

import kr.ac.ks.cs_web_back.domain.member.controller.code.MemberExceptionCode;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import kr.ac.ks.cs_web_back.global.exeption.domain.ConflictException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql(statements = "ALTER TABLE member ALTER COLUMN id RESTART WITH 1", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공: 멤버 객체를 생성 후 생성된 멤버의 id를 반환한다.")
    void shouldCreateMemberAndReturnsCreatedId() {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "example@ks.ac.kr",
                "validPassword1234!",
                "exampleUser"
        );

        // when
        Long createdId = memberService.createMember(request);

        // then
        Member savedMember = memberRepository.findById(createdId).orElseThrow();

        assertThat(createdId).isEqualTo(1L);
        assertThat(savedMember.getEmail()).isEqualTo(request.email());
        assertThat(savedMember.getNickName()).isEqualTo(request.username());
        assertThat(passwordEncoder.matches(request.password(), savedMember.getPassword())).isTrue();
    }


    @Test
    @DisplayName("회원가입 실패: 중복된 이메일로 가입 시 ConflictException이 발생한다.")
    void registeredFailedByConflictEmail() {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "example@ks.ac.kr",
                "validpassword1234!",
                "exampleUser"
        );

        memberRepository.saveAndFlush(Member.builder()
                .email(request.email())
                .password("anyPassword")
                .username("anyUsername")
                .build());

        // when & then
        assertThatThrownBy(() -> memberService.createMember(request))
                .isInstanceOf(ConflictException.class)
                .satisfies(exception -> {
                    ConflictException e = (ConflictException) exception;
                    assertThat(e.getExceptionCode()).isEqualTo(MemberExceptionCode.CONFLICT_EMAIL);
                });
    }

    @Test
    @DisplayName("회원가입 실패: 중복된 유저명으로 가입 시 ConflictException이 발생한다.")
    void registeredFailedByConflictUsername() {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "example@ks.ac.kr",
                "validpassword1234!",
                "exampleUser"
        );

        memberRepository.saveAndFlush(Member.builder()
                .email("anyEmail@naver.com")
                .password("anyPassword")
                .username(request.username())
                .build());

        // when & then
        assertThatThrownBy(() -> memberService.createMember(request))
                .isInstanceOf(ConflictException.class)
                .satisfies(exception -> {
                    ConflictException e = (ConflictException) exception;
                    assertThat(e.getExceptionCode()).isEqualTo(MemberExceptionCode.CONFLICT_USERNAME);
                });
    }
}
