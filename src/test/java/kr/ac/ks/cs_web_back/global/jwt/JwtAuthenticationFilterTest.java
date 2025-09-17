package kr.ac.ks.cs_web_back.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.domain.member.dto.request.MemberCreateRequest;
import kr.ac.ks.cs_web_back.domain.member.fixture.MemberFixture;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.domain.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtTokenResolver jwtTokenResolver;

    @MockitoBean
    private HttpServletRequest request;

    @MockitoBean
    private HttpServletResponse response;

    @MockitoBean
    private FilterChain filterChain;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        Member member = MemberFixture.memberFixture();

        memberService.createMember(new MemberCreateRequest(
                member.getEmail(), member.getPassword(), member.getUsername()
        ));

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("토큰이 유효한 경우 요청이 필터 체인을 통과하고 SecurityContext에 인증 정보가 저장된다.")
    void shouldProceedWithFilterChainWhenTokenIsValid() throws Exception {
        // Given
        String accessToken = TokenFixture.createAccessToken(jwtUtil);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + accessToken);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, times(1)).doFilter(request, response);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
        assertEquals(TokenFixture.TEST_EMAIL, ((UserDetails) authentication.getPrincipal()).getUsername());
    }

    @Test
    @DisplayName("만료된 토큰인 경우 필터 체인 과정에서 401 Unauthorized 예외가 발생한다.")
    void shouldBe401UnauthorizedWithFilterChainWhenTokenIsExpired() throws Exception {
        // Given
        String expiredAccessToken = TokenFixture.createExpiredAccessToken(secret);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredAccessToken);
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, never()).doFilter(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("토큰 형식이 잘못된 경우 필터 체인 과정에서 401 Unauthorized 예외가 발생한다.")
    void shouldBe401UnauthorizedWithFilterChainWhenTokenIsInvalid() throws Exception {
        // Given
        String malformedAccessToken = "Bearer MalformedAccessToken";
        when(request.getHeader("Authorization")).thenReturn(malformedAccessToken);
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain, never()).doFilter(request, response);
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response, times(1)).getWriter();
    }

    @Test
    @DisplayName("Authorization 헤더가 없거나 'Bearer '로 시작하지 않는 경우 필터 체인이 진행되고 인증 정보가 설정되지 않는다.")
    void shouldProceedWithFilterChainWhenTokenIsNotPresentOrInvalidFormat() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        reset(request, filterChain);
        SecurityContextHolder.clearContext();

        when(request.getHeader("Authorization")).thenReturn("Basic somecredentials");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
