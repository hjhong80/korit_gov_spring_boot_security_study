package com.korit.security_study.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class OAuth2User {
    private Integer oAuth2Id;
    private Integer userId;
    private String provider;
    private String providerUserId;
    private LocalDateTime createDt;
}
