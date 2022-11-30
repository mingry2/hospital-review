package com.example.hospitalreview.service;

import com.example.hospitalreview.domain.User;
import com.example.hospitalreview.domain.dto.UserDto;
import com.example.hospitalreview.domain.UserJoinRequest;
import com.example.hospitalreview.exception.ErrorCode;
import com.example.hospitalreview.exception.HospitalReviewAppException;
import com.example.hospitalreview.repository.UserRepository;
import com.example.hospitalreview.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// 비지니스 로직이 들어가는 곳
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}") // yml 에서 가져올 수 있음 key를 숨길 수 있음
    private String secretKey;
    private long expiredTimeMs = 1000 * 60 * 60; // = 1시간

    public String login(String userName, String password) {
        // userName 있는지 여부 확인
        // 없으면 NOT_FOUND 에러 발생
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new HospitalReviewAppException(ErrorCode.NOT_FOUND,String.format("%s는 가입된 적이 없습니다.", userName)));
        // password 일치 하는지 여부 확인
        if (!encoder.matches(password, user.getPassword())) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD,String.format("userName 또는 password가 잘 못 되었습니다."));
        }
        // 두 가지 확인 중 예외 안났으면 Token 발행

        // 소스코드에 토큰 key가 들어가면 절! 대! 안된다
        // return JwtTokenUtil.createToken(userName,"hello", 1000 * 60 * 60);
        return JwtTokenUtil.createToken(userName, secretKey, expiredTimeMs);
    }

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
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }

}
