package com.example.pointapi.model;

import lombok.*;

import java.math.BigDecimal;

// 포인트 적립원장
//  1) 사용자+주문별로 적립
//  2) 사용자+수지지급 적립
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointAccuMstDto {
    private Long                 pointKey                                 ;  // 포인트KEY
    private Long                 pointEventKey                             ;  // 포인트 이벤트 KEY
    private Long                 orderNo                                  ;  // (적립을 하게된 계기의) 주문번호
    private Long                 orgPointKey                              ;  // 원 포인트 적립번호. 유효기간 경과된 건 사용취소시 적립
    private String               manualRegId                              ;  // 수기지급 담당자 아이디
    private String               accuDate                              ;  // 적립일자
    private String               expireDate                               ;  // 만료일자
    private Long                 userNo                                   ;  // 사용자 번호
    private BigDecimal           accuPoint                             ;  // 적립 포인트
    private String               accuCancelYn                          ;  // 적립 취소 여부
    private BigDecimal           balancePoint                             ;  // 포인트 잔액

    private Long                 expireDays;
    private String               pointAccuType;

}