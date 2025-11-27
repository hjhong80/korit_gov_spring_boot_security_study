package com.korit.security_study.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer userId;
    private String username;
    @JsonIgnore                         // Json으로 출력될때 바로 아래 필드는 제외됨
    private String password;
    private String email;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

//    객체지향적 설계를 위함 => 객체를 참조할 수 있는 구조를 선호
    private List<UserRole> userRoleList;

//    권한목록과 유저권한목록을 따로 둔 이유
//    만약에 유저당 하나의 권한만 가질수 있을때,
//    User는 하나의 role만 가질 수 있음.
//    Role은 N명의 사용자에게 부여될 수 있음. -> 1:N 구조
//    관리자이면서 일반 사용자인 경우 동시의 두가지 권한을 가질 수 없는 구조.
//    User가 여러 권한을 가질 수 있을때 Role도 여러 유저에게 부여될 수 있음 -> N:N 구조
//    이러한 권한 관리가 복잡해지므로 권한 목록인 중간 테이블을 따로 분리해서 관리
}
