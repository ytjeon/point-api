package com.example.pointapi.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserMstDto {
    private Long userNo;         // 사용자 번호
    private String userId;         // 사용자 아이디
    private String userName;     // 사용자 이름
}
