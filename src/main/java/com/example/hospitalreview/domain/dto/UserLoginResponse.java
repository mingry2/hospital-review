package com.example.hospitalreview.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


// 다른 것과 다르게 token을 내려주면됨
@AllArgsConstructor
@Getter
public class UserLoginResponse {
    private String token;
}
