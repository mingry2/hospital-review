package com.example.hospitalreview.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {

    private String resultCode; // 어떤 에러가 났는지 알려주는 것(어떤 에러코드?)
    private T result; // 성공을 반환할 때 Response<T> 객체로 감싸서 반환한다.

    public static Response<Void> error(String resultCode) {
        return new Response(resultCode, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response("SUCCESS", result);
    }
}
