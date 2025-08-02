package kr.ac.ks.cs_web_back.domain.member.service;

import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 성공: 멤버 객체를 생성 후 생성된 멤버의 id를 반환한다.")
    void shouldCreateMemberAndReturnsCreatedId() {
        // given
        MemberCreateRequest request = new MemberCreateRequest(
                "example@ks.ac.kr",
                "validPassword1234!",
                "exampleUser"
        );

        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(memberRepository.existsByEmail(anyString())).willReturn(false);
        given(memberRepository.existsByUsername(anyString())).willReturn(false);

        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> {
            Member memberToSave = invocation.getArgument(0);
            ReflectionTestUtils.setField(memberToSave, "id", 1L);
            return memberToSave;
        });

        // when
        Long createdId = memberService.createMember(request);

        // then
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        Member savedMember = memberCaptor.getValue();

        assertThat(createdId).isEqualTo(1L);

        assertThat(savedMember.getEmail()).isEqualTo(request.email());
        assertThat(savedMember.getUsername()).isEqualTo(request.username());
        assertThat(savedMember.getPassword()).isEqualTo("encodedPassword");
    }
}
