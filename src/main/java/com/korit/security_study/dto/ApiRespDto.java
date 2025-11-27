package com.korit.security_study.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ApiRespDto<T> {
    private String status;
    private String message;
    private T data;
}
