package com.example.hospitalreview.controller;


import com.example.hospitalreview.domain.Response;
import com.example.hospitalreview.domain.UserJoinRequest;
import com.example.hospitalreview.domain.UserJoinResponse;
import com.example.hospitalreview.domain.dto.UserDto;
import com.example.hospitalreview.domain.dto.UserLoginRequest;
import com.example.hospitalreview.domain.dto.UserLoginResponse;
import com.example.hospitalreview.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 정보 가져오는 PostMapping
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserDto userDto = userService.join(userJoinRequest);
        log.info("잘넘어가는지 :{}", userJoinRequest);
        return Response.success(new UserJoinResponse(userDto.getUserName(), userDto.getEmail()));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest.getUserName(), userLoginRequest.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
