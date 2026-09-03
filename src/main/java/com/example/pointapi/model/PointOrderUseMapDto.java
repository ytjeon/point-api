package com.example.pointapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

// 포인트 주문사용-적립 매핑
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PointOrderUseMapDto {
    private Long        pointOrderUseMapNo;  // 번호
    private Long        pointOrderUseNo;     // 포인트 사용 기록 번호
    private Long        pointKey;            // 포인트KEY
    private BigDecimal  point;              // 포인트


    private Long                 pointEventKey                             ;  // 포인트 이벤트 KEY
    private Long                 orderNo                                  ;  //  주문번호
    private String               expireDate                               ;  // 만료일자
    private Long                 userNo                                   ;  // 사용자 번호
    private BigDecimal           accuPoint                             ;  // 적립 포인트
    private BigDecimal           balancePoint                             ;  // 포인트 잔액
    private Long                 expireDays;
    private String               pointAccuType;

}