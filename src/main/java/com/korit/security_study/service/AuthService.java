package com.korit.security_study.service;

import com.korit.security_study.dto.ApiRespDto;
import com.korit.security_study.dto.SigninReqDto;
import com.korit.security_study.dto.SignupReqDto;
import com.korit.security_study.entity.User;
import com.korit.security_study.entity.UserRole;
import com.korit.security_study.repository.UserRepository;
import com.korit.security_study.repository.UserRoleRepository;
import com.korit.security_study.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtils jwtUtils;

    public ApiRespDto<?> addUser(SignupReqDto signupReqDto) {
        System.out.println("AuthService : addUser");
        if (signupReqDto.getUsername().contains(" ")) {
            System.out.println("addUser : username 공백 존재");
            return new ApiRespDto<>("failed","username에 공백이 존재합니다.",signupReqDto.getUsername());
        }
        // 먼저 username 중복 확인
        Optional<User> foundUser = userRepository.findByUsername(signupReqDto.getUsername());
        if (foundUser.isPresent()) {
            System.out.println("addUser : username 존재");
            return new ApiRespDto<>("failed","username이 존재합니다.",signupReqDto.getUsername());
        }

        // User추가
        Optional<User> user = userRepository.addUser(signupReqDto.toEntity(bCryptPasswordEncoder));

        // 추가후 userId 로 userRole 추가
        if (user.isEmpty()) {
            System.out.println("addUser : 가입 실패");
            return new ApiRespDto<>("failed","가입이 실패하였습니다.",null);
        }
        UserRole userRole = UserRole.builder()
                .userId(user.get().getUserId())
                .roleId(3)
                .build();
        int result = userRoleRepository.addUserRole(userRole);
        if (result != 1) {
            System.out.println("addUserRole : UserRole 추가 실패");
            return new ApiRespDto<>("failed","권한 부여에 실패하였습니다.",null);
        }
        return new ApiRespDto<>("success","회원 가입이 완료되었습니다.", user.get());
    }

    public ApiRespDto<?> getUserAll() {
        System.out.println("AuthService : getUserAll");
        List<User> foundUser = userRepository.findAll();
        if (foundUser.isEmpty()) {
            System.out.println("getUserAll : 리스트 없음");
            return new ApiRespDto<>("failed","User 목록이 없습니다.",null);
        }
        return new ApiRespDto<>("success", "전체 조회에 성공하였습니다.",foundUser);
    }

    public ApiRespDto<?> getUserByUsername(String username) {
        System.out.println("AuthService : getUserByUsername");
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isEmpty()) {
            System.out.println("getUserByUsername : 조회 실패");
            return new ApiRespDto<>("failed","username 조회에 실패하였습니다.",null);
        }
        return new ApiRespDto<>("success", "username 조회에 성공하였습니다.",foundUser.get());
    }

    public ApiRespDto<?> getUserByUserId(Integer userId) {
        System.out.println("AuthService : getUserByUserId");
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            System.out.println("getUserByUserId : 조회 실패");
            return new ApiRespDto<>("failed","userId 조회에 실패하였습니다.",null);
        }
        return new ApiRespDto<>("success", "userId 조회에 성공하였습니다.",foundUser.get());
    }

    public ApiRespDto<?> signin(SigninReqDto signinReqDto) {
        Optional<User> foundUser = userRepository.findByUsername(signinReqDto.getUsername());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed","사용자 정보를 다시 확인해주세요.",null);
        }
        User user = foundUser.get();
        if(!bCryptPasswordEncoder.matches(signinReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed","사용자 정보를 다시 확인해주세요.",null);
        }
        String token = jwtUtils.generateAccessToken(user.getUserId().toString());
        return new ApiRespDto<>("success","로그인에 성공하였습니다.",token);
    }

//    public ApiRespDto<?> editUser(Dto dto) {
//        Optional<User> foundUser = userRepository.findById(userId);
//        if (foundUser.isEmpty()) {
//            System.out.println("addUser : userId 없음");
//            return new ApiRespDto<>("failed","userId 조회에 실패하였습니다.",null);
//        }
//        return null;
//    }
//
//    public ApiRespDto<?> removeUser(Integer userId) {
//        return null;
//    }

}
