package com.korit.security_study.security.handler;

import com.korit.security_study.entity.OAuth2User;
import com.korit.security_study.entity.User;
import com.korit.security_study.repository.OAuth2UserRepository;
import com.korit.security_study.repository.UserRepository;
import com.korit.security_study.security.jwt.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final OAuth2UserRepository oAuth2UserRepository;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("OAuth2SuccessHandler : onAuthenticationSuccess");
//        OAuth2User 정보를 파싱
        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String provider = defaultOAuth2User.getAttribute("provider");   // 제공자 이름
        String providerUserId = defaultOAuth2User.getAttribute("providerUserId");   // 제공자의 고유 ID
        String email = defaultOAuth2User.getAttribute("email");         // email

        System.out.println("---------------------------------------------------");
        System.out.println(provider);
        System.out.println(providerUserId);
        System.out.println(email);
        System.out.println("---------------------------------------------------");

//        provider, providerUserId로 이미 연동된 사용자 정보가 있는지 db 확인
        Optional<OAuth2User> foundOAuth2USer = oAuth2UserRepository.getOAuth2UserByProviderAndProviderUserID(provider,providerUserId);
//        OAuth2 로그인을 통해 회원가입이 되어 있지 않거나 아직 연동되어 있지 않은 상태
        if (foundOAuth2USer.isEmpty()) {
            System.out.println("http://localhost:3000/auth/oauth2?provider=" + provider + "&providerUserId=" + providerUserId + "&email=" + email);
            response.sendRedirect("http://localhost:3000/auth/oauth2?provider=" + provider + "&providerUserId=" + providerUserId + "&email=" + email);
            return;
        }

//        연동된 사용자가 있다면 => userId를 통해 회원 정보를 조회
        Optional<User> foundUser = userRepository.findById(foundOAuth2USer.get().getUserId());
        String accessToken = null;
        if (foundUser.isPresent()) {
            accessToken = jwtUtils.generateAccessToken(Integer.toString(foundUser.get().getUserId()));

        }

        response.sendRedirect("http://localhost:3000/auth/oauth2/signin?accessToken=" + accessToken);

    }
}
