package com.fanbo.user_service.service;

import com.fanbo.user_service.Security.JwtUtils;
import com.fanbo.user_service.client.MailServiceClient;
import com.fanbo.user_service.dto.LoginRequest;
import com.fanbo.user_service.dto.RegisterRequest;
import com.fanbo.user_service.entity.User;
import com.fanbo.user_service.event.OtpEventPublisher;
import com.fanbo.user_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private OtpEventPublisher eventPublisher;
    @Mock
    private MailServiceClient mailServiceClient;

    @InjectMocks
    private AuthService authService;

    //REGISTER TESTS
    @Test
    public void register_shouldThrowException_whenEmailAlreadyExists() {
        //arrange
        RegisterRequest request = new RegisterRequest("test","testtest","test@gmail.com");
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        //act and assert
        //metoda juinit assertions mniej czytelne
        //assertThrows(IllegalStateException.class, () -> authService.register(request));
        //AssertJ
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void register_shouldSaveUser_whenEmailIsNew() {
        //arrange
        RegisterRequest request = new RegisterRequest("test","testtest","test@gmail.com");
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.getPassword())).thenReturn("succes");

        //act
        authService.register(request);

        //assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void register_shouldEncodePassword_whenPasswordIsNew() {
        //arrange
        RegisterRequest request = new RegisterRequest("test","testtest","test@gmail.com");
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);
        //act
        authService.register(request);

        //assert
        verify(passwordEncoder).encode(request.getPassword());
    }

    //LOGIN TESTS

    @Test
    public void login_shouldThrowException_whenPasswordDoesNotMatch() {
        //arrange
        LoginRequest request = new LoginRequest("test","testtest");
        //tutaj wyjasnienie bo sie wywalilem
        //testowac moja logike a nie framweork tak ja k wcenisj mielame niestety
        //wrzucam dla frameworka cos nie wazne co ale ma wyrzucic bad credentials i esjt git
        when(authenticationManager.authenticate(any()))
                .thenThrow(BadCredentialsException.class);

        //act and assert
        //teraz sprawdzam loginservice czy sie logujac wyrzuci wyjatek z wyzej
        //ktory powinno wyrzicic zawsze
        assertThatThrownBy(() -> authService.login(request));
    }

    @Test
    public void login_shouldPublishOtpEvent_whenEmailAndPasswordMatch() {
        //arrange
        LoginRequest request = new LoginRequest("test","testtest");

        //act
        //wywoluje loign
        authService.login(request);

        //assert
        //spradzam czy sie odzywa publisher
        verify(eventPublisher).publishOtpEvent(any());
    }
}
