package com.example.pointapi.model;

import lombok.*;

import java.math.BigDecimal;

// 포인트 주문 사용 히스토리
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PointOrderUseHstDto{
    private Long                 pointOrderUseNo                          ;  // 포인트 적립/사용 기록 번호
    private Long                 orderNo                                  ;  // 주문번호
    private String               tradeDt                                  ;  // 거래일시
    private String               pointTradeType                           ;  // P:사용  C:사용취소
    private Long                 userNo                                   ;  // 사용자 번호
    private BigDecimal           point                                    ;  // (사용 또는 사용취소) 포인트
}

