package com.example.hospitalreview.service;

import com.example.hospitalreview.domain.User;
import com.example.hospitalreview.domain.dto.UserDto;
import com.example.hospitalreview.domain.UserJoinRequest;
import com.example.hospitalreview.exception.ErrorCode;
import com.example.hospitalreview.exception.HospitalReviewAppException;
import com.example.hospitalreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 비지니스 로직이 들어가는 곳
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto join(UserJoinRequest request) {
        // 비즈니스 로직 - 회원 가입
        // 회원 userName(id) 중복 check
        // 중복이면 회원가입 불가 -> Exception(예외)발생으로 예외처리 필요

        // 중복되는 UserName이 없으면 에러처리(잘못된코드)
//        userRepository.findByUserName(request.getUserName())
//                .orElseThrow(() -> new RuntimeException("해당 UserName은 중복 됩니다."));
        // 중복되는 UserName이 있으면 에러처리
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user ->{
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("UserName:%s", request.getUserName()));
                });

        // 중복이 아니라면 회원가입 .save()
        // savedUser -> 가입된 User
        User savedUser = userRepository.save(request.toEntity());

        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }
}
