package com.korit.security_study.dto;

import com.korit.security_study.entity.OAuth2User;
import com.korit.security_study.entity.User;
import io.jsonwebtoken.security.Password;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@AllArgsConstructor
public class OAuth2SignupReqDto {
    private String email;
    private String username;
    private String password;
    private String provider;
    private String providerUserId;

    public User toUserEntity (BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .email(email)
                .build();
    }
    public OAuth2User toOAuth2UserEntity(Integer userId) {
        return OAuth2User.builder()
                .userId(userId)
                .provider(provider)
                .providerUserId(providerUserId)
                .build();
    }
}
