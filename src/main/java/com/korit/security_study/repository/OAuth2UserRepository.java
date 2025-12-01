package com.korit.security_study.repository;

import com.korit.security_study.entity.OAuth2User;
import com.korit.security_study.mapper.OAuth2UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OAuth2UserRepository {
    private final OAuth2UserMapper oAuth2UserMapper;

    public Optional<OAuth2User> getOAuth2UserByProviderAndProviderUserID(String provider, String providerUserId) {
        System.out.println("OAuth2UserRepository : getOAuth2UserByProviderAndProviderUserID");
        return oAuth2UserMapper.getOAuth2UserByProviderAndProviderUserID(provider,providerUserId);
    }

    public int addOAuth2User(OAuth2User oAuth2User) {
        System.out.println("OAuth2UserRepository : addOAuth2User");
        return oAuth2UserMapper.addOAuth2User(oAuth2User);
    }
}
