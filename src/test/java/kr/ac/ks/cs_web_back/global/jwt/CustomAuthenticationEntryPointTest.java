package kr.ac.ks.cs_web_back.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.ks.cs_web_back.domain.auth.controller.code.AuthExceptionCode;
import kr.ac.ks.cs_web_back.global.exeption.dto.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {

    @InjectMocks
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

    }

    @Test
    @DisplayName("commence 메서드 호출 시 401 Unauthorized 응답과 JSON 에러 메시지를 반환한다.")
    void commenceReturns401UnauthorizedAndJsonError() throws IOException {
        // Given
        AuthExceptionCode expectedCode = AuthExceptionCode.UNAUTHORIZED_INVALID_TOKEN;
        ExceptionResponse expectedResponse = new ExceptionResponse(expectedCode.getCode(), expectedCode.getMessage());
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);

        // When
        customAuthenticationEntryPoint.commence(request, response, authException);

        // Then
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response).getWriter();
        printWriter.flush();
        assertThat(stringWriter.toString()).isEqualTo(expectedJson);
    }
}
