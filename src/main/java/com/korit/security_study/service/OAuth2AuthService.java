package com.korit.security_study.service;

import com.korit.security_study.dto.ApiRespDto;
import com.korit.security_study.dto.OAuth2SignupReqDto;
import com.korit.security_study.entity.User;
import com.korit.security_study.entity.UserRole;
import com.korit.security_study.repository.OAuth2UserRepository;
import com.korit.security_study.repository.UserRepository;
import com.korit.security_study.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

// oAuth2로 회원가입 연동
@Service
@RequiredArgsConstructor
public class OAuth2AuthService {
    private final OAuth2UserRepository oAuth2UserRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRoleRepository userRoleRepository;

    public ApiRespDto<?> signup(OAuth2SignupReqDto oAuth2SignupReqDto) {
        Optional<User> foundUser = userRepository.findByEmail(oAuth2SignupReqDto.getEmail());
        if(foundUser.isPresent()) {
            System.out.println("signup : email 중복");
            return new ApiRespDto<>("failed", "이미 존재하는 email 입니다.", null);
        }
        Optional<User> foundUserByUsername = userRepository.findByUsername(oAuth2SignupReqDto.getUsername());
        if(foundUserByUsername.isPresent()) {
            System.out.println("signup : username 중복");
            return new ApiRespDto<>("failed", "이미 존재하는 username 입니다.", null);
        }

        Optional<User> optionalUser = userRepository.addUser(oAuth2SignupReqDto.toUserEntity(bCryptPasswordEncoder));

        if (optionalUser.isEmpty()) {
            System.out.println("signup : User 추가 실패");
            return new ApiRespDto<>("failed", "가입에 실패했습니다.", null);
        }

        UserRole userRole = UserRole.builder()
                .userId(optionalUser.get().getUserId())
                .roleId(3)
                .build();

        userRoleRepository.addUserRole(userRole);
        oAuth2UserRepository.addOAuth2User(oAuth2SignupReqDto.toOAuth2UserEntity(optionalUser.get().getUserId()));
        return new ApiRespDto<>("success", oAuth2SignupReqDto.getProvider() + "로 OAuth2 가입에 성공하였습니다.", optionalUser.get());
    }
}
