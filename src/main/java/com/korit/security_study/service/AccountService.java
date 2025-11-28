package com.korit.security_study.service;

import com.korit.security_study.dto.ApiRespDto;
import com.korit.security_study.dto.ModifyPasswordReqDto;
import com.korit.security_study.entity.User;
import com.korit.security_study.repository.UserRepository;
import com.korit.security_study.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiRespDto<?> modifyPassword(ModifyPasswordReqDto modifyPasswordReqDto, Principal principal) {
        System.out.println("AccountService : modifyPassword");
        if(!modifyPasswordReqDto.getUserId().equals(principal.getUserId())) {
            System.out.println("modifyPassword : 인증 실패");
            return new ApiRespDto<>("failed", "잘못된 접근 입니다.",null);
        }

        Optional<User> foundUser = userRepository.findById(modifyPasswordReqDto.getUserId());
        if(foundUser.isEmpty()) {
            System.out.println("modifyPassword : 유저 없음");
            return new ApiRespDto<>("failed","사용자 정보를 다시 확인해주세요.",null);
        }

        User user = foundUser.get();
        if(!bCryptPasswordEncoder.matches(modifyPasswordReqDto.getOldPassword(),user.getPassword())) {
            System.out.println("modifyPassword : 패스워드 틀림");
            return new ApiRespDto<>("failed","사용자 정보를 다시 확인해주세요.",null);
        }
        if(bCryptPasswordEncoder.matches(modifyPasswordReqDto.getNewPassword(),user.getPassword())) {
            System.out.println("modifyPassword : 새 비밀번호가 기존 비밀번호와 같음");
            return new ApiRespDto<>("failed","새 비밀번호는 기존 비밀번호와 달라야 합니다.",null);
        }

        User updateUser = modifyPasswordReqDto.toEntity(bCryptPasswordEncoder);
        int result = userRepository.updatePassword(updateUser);
        if (result != 1) {
            System.out.println("modifyPassword : 업데이트 실패");
            return new ApiRespDto<>("failed","변경에 실패하였습니다.",null);
        }
        return new ApiRespDto<>("success", "비밀번호가 변경되었습니다.",updateUser.getUserId());
    }
}
