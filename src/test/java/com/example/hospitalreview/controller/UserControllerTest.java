package com.example.hospitalreview.controller;

import com.example.hospitalreview.domain.UserJoinRequest;
import com.example.hospitalreview.domain.dto.UserDto;
import com.example.hospitalreview.exception.ErrorCode;
import com.example.hospitalreview.exception.HospitalReviewAppException;
import com.example.hospitalreview.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// test 코드에서는 카멜코드를 잘 사용하지않음 -> 가독성 떨어짐 (_언더바를 통해 사용)
@WebMvcTest
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;
    UserJoinRequest userJoinRequest = UserJoinRequest.builder()
            .userName("mingyeong")
            .password("1q2w3e4r")
            .email("mingyeong@naver.com")
            .build();

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser // 가상의 유효한 User 설정
    void join_success() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("mingyeong")
                .password("12a34b")
                .email("mingyeong@naver.com")
                .build();
        when(userService.join(any())).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf()) // csrf() token 인증 통과
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패 - id 중복")
    @WithMockUser
    void join_fail() throws Exception {

        String password = "1q2w3e4r";

        when(userService.join(any())).thenThrow(new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 실패 - id 없음")
    @WithMockUser // 가상의 유효한 User 설정
    void login_fail1() throws Exception {

//        String id = "mingyeong";
//        String password = "1q2w3e4r";

        // 무엇을 보내서? : id, pw
        when(userService.login(any(), any())).thenThrow(new HospitalReviewAppException(ErrorCode.NOT_FOUND, ""));
        // 무엇을 받을까? : NOT_FOUND
        // NOT_FOUND가 나오면 잘 나온 것
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("로그인 실패 - password 잘 못 입력")
    @WithMockUser // 가상의 유효한 User 설정
    void login_fail2() throws Exception {

    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser // 가상의 유효한 User 설정
    void login_success() throws Exception {

    }
}
