package com.example.pointapi.model;

import lombok.*;

import java.math.BigDecimal;

// 포인트 이벤트 원장
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointEventMstDto {
    private Long                 pointEventKey                            ;  // 포인트 이벤트 KEY
    private String               pointEventName                           ;  // 포인트 이벤트 이름
    private String               pointAccuType                            ;  // 1:일반,2:관리자 수기지급
    private Long                 expireDays                               ;  // 유효기간 일자
    private BigDecimal           maxUnitPoint                             ;  // 1회 최대포인트

    private String               pointAccuTypeName;
}
