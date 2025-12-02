package com.korit.security_study.controller;

import com.korit.security_study.dto.OAuth2MergeReqDto;
import com.korit.security_study.dto.OAuth2SignupReqDto;
import com.korit.security_study.service.OAuth2AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2AuthService oAuth2AuthService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody OAuth2SignupReqDto oAuth2SignupReqDto) {
        System.out.println("OAuth2Controller : signup");
        return ResponseEntity.ok(oAuth2AuthService.signup(oAuth2SignupReqDto));
    }

    @PostMapping("/merge")
    public ResponseEntity<?> merge(@RequestBody OAuth2MergeReqDto oAuth2MergeReqDto) {
        System.out.println("OAuth2Controller : merge");
        return ResponseEntity.ok(oAuth2AuthService.merge(oAuth2MergeReqDto));
    }

}
