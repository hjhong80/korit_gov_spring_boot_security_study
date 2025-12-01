package com.korit.security_study.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OAuth2PrincipalService extends DefaultOAuth2UserService {
//    정보를 파싱하여 Attribute에 담아둠.

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("OAuth2PrincipalService : loadUser");
//        Spring security가 oAuth2 provider에게 (구글에서 발급한)AccessToken으로 사용자 정보를 요청
//        그 결과로 받아온 사용자 정보(JSON)을 파싱(loadUser)한 객체 oAuth2User를 리턴 받는다
        OAuth2User oAuth2User = super.loadUser(userRequest);

//        사용자 정보 추출
        Map<String,Object> attributes = oAuth2User.getAttributes();

//        어떤 provider 인지 확인
//        provider => 공급처(구글, 네이버, 카카오 등)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
//        공급처에서 발행한 사용자 식별자
        String providerUserId = null;

//        switch문을 사용하여 provider마다 다른 로직 적용
        switch(provider) {
            case "google" :
                providerUserId = attributes.get("sub").toString();
                email = (String) attributes.get("email");
                break;
            case "naver" :
//                break;
            case "kakao" :
//                break;
            default :
                break;
        }

        Map<String,Object> newAttributes = Map.of(
          "providerUserId", providerUserId,
          "provider", provider,
          "email", email
        );
//        임시 권한 부여(ROLE_TEMPORARY)
//        실제 권한은 OAuth2SuccessHandler에서 판단
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TEMPORARY"));

//        Spring Security가 사용할 OAuth2User객체 생성해서 반환
//        id => principal.getName() 했을때 사용할 이름
        return new DefaultOAuth2User(authorities, newAttributes,"providerUserId");
    }
}
