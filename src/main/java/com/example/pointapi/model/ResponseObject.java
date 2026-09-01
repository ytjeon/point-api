package com.example.pointapi.model;

import com.example.pointapi.constants.ResultCodeEnum;
import lombok.*;
import org.springframework.stereotype.Repository;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject<T> {
    // 결과코드. 기본 성공코드 :0000
    private String resultCode;

    // 결과 메시지
    private String resultMessage;

    // 결과 데이타
    private T data;


    // 단순 실행결과 또는 오류 일때
    public ResponseObject(String resultCode, String resultMessage){
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.data = null;
    }

    // 단순 실행결과 또는 오류 일때
    public ResponseObject(ResultCodeEnum resultCodeEnum){
        this.resultCode = resultCodeEnum.getCode();
        this.resultMessage = resultCodeEnum.getName();
        this.data = null;
    }
}