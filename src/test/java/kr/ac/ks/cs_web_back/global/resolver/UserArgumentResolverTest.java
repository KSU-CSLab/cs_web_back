package kr.ac.ks.cs_web_back.global.resolver;

import kr.ac.ks.cs_web_back.domain.member.fixture.MemberFixture;
import kr.ac.ks.cs_web_back.domain.member.model.Member;
import kr.ac.ks.cs_web_back.global.annotation.IdentifiedUser;
import kr.ac.ks.cs_web_back.global.annotation.ResolvedUser;
import kr.ac.ks.cs_web_back.global.exeption.domain.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserArgumentResolverTest {

    @InjectMocks
    private IdentifiedUserArgumentResolver identifiedUserArgumentResolver;

    @InjectMocks
    private ResolvedUserArgumentResolver resolvedUserArgumentResolver;

    @Mock
    private MethodParameter methodParameter;

    @Mock
    private NativeWebRequest nativeWebRequest;

    @Mock
    private WebDataBinderFactory webDataBinderFactory;

    @Mock
    private ModelAndViewContainer modelAndViewContainer;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = MemberFixture.memberFixture();
        SecurityContextHolder.setContext(securityContext);
    }

    // IdentifiedUserArgumentResolver Tests
    @Test
    @DisplayName("IdentifiedUserArgumentResolver: @IdentifiedUser 어노테이션이 있는 파라미터를 지원한다.")
    void identifiedUserSupportsParameterWithAnnotation() {
        when(methodParameter.hasParameterAnnotation(IdentifiedUser.class)).thenReturn(true);
        assertThat(identifiedUserArgumentResolver.supportsParameter(methodParameter)).isTrue();
    }

    @Test
    @DisplayName("IdentifiedUserArgumentResolver: @IdentifiedUser 어노테이션이 없는 파라미터를 지원하지 않는다.")
    void identifiedUserSupportsParameterWithoutAnnotation() {
        when(methodParameter.hasParameterAnnotation(IdentifiedUser.class)).thenReturn(false);
        assertThat(identifiedUserArgumentResolver.supportsParameter(methodParameter)).isFalse();
    }

    @Test
    @DisplayName("IdentifiedUserArgumentResolver: 인증된 Member 객체를 반환한다.")
    void identifiedUserResolveArgumentReturnsMember() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testMember);

        Object result = identifiedUserArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);

        assertThat(result).isEqualTo(testMember);
    }

    @Test
    @DisplayName("IdentifiedUserArgumentResolver: 인증 정보가 없으면 InvalidTokenException을 발생시킨다.")
    void identifiedUserResolveArgumentThrowsExceptionWhenNoAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThatThrownBy(() -> identifiedUserArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("IdentifiedUserArgumentResolver: Principal이 Member 타입이 아니면 InvalidTokenException을 발생시킨다.")
    void identifiedUserResolveArgumentThrowsExceptionWhenPrincipalIsNotMember() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("notAMember");

        assertThatThrownBy(() -> identifiedUserArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("ResolvedUserArgumentResolver: @ResolvedUser 어노테이션이 있는 파라미터를 지원한다.")
    void resolvedUserSupportsParameterWithAnnotation() {
        when(methodParameter.hasParameterAnnotation(ResolvedUser.class)).thenReturn(true);
        assertThat(resolvedUserArgumentResolver.supportsParameter(methodParameter)).isTrue();
    }

    @Test
    @DisplayName("ResolvedUserArgumentResolver: @ResolvedUser 어노테이션이 없는 파라미터를 지원하지 않는다.")
    void resolvedUserSupportsParameterWithoutAnnotation() {
        when(methodParameter.hasParameterAnnotation(ResolvedUser.class)).thenReturn(false);
        assertThat(resolvedUserArgumentResolver.supportsParameter(methodParameter)).isFalse();
    }

    @Test
    @DisplayName("ResolvedUserArgumentResolver: 인증된 Member 객체를 반환한다.")
    void resolvedUserResolveArgumentReturnsMember() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(testMember);

        Object result = resolvedUserArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);

        assertThat(result).isEqualTo(testMember);
    }

    @Test
    @DisplayName("ResolvedUserArgumentResolver: 인증 정보가 없으면 null을 반환한다.")
    void resolvedUserResolveArgumentReturnsNullWhenNoAuthentication() {
        when(securityContext.getAuthentication()).thenReturn(null);

        Object result = resolvedUserArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("ResolvedUserArgumentResolver: Principal이 'anonymousUser'이면 null을 반환한다.")
    void resolvedUserResolveArgumentReturnsNullWhenPrincipalIsAnonymousUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        Object result = resolvedUserArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("ResolvedUserArgumentResolver: Principal이 Member 타입이 아니면 null을 반환한다.")
    void resolvedUserResolveArgumentReturnsNullWhenPrincipalIsNotMember() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("notAMember");

        Object result = resolvedUserArgumentResolver.resolveArgument(methodParameter, modelAndViewContainer, nativeWebRequest, webDataBinderFactory);

        assertThat(result).isNull();
    }
}
