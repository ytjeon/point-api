package com.example.pointapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 포인트 이벤트 원장
@Getter
@Setter
@ToString
public class PointEventMstDto {
    private Long                 pointEventNo                             ;  // 포인트 이벤트 번호
    private String               pointEventName                           ;  // 포인트 이벤트 이름
    private String               pointAccuralType                         ;  // 1:일반,2:관리자 수기지급
    private Long                 expireDays                               ;  // 유효기간 일자
    private Long                 maxUnitPoint                             ;  // 1회 최대포인트
}
