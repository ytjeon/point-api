package com.example.pointapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description="포인트KEY. 주문별 적립KEY", example="3")
    private Long                 pointKey                                 ;  // 포인트KEY

    @Schema(description="포인트 이벤트 KEY(pointKey). 포인트 적립 종류", example="1")
    private Long                 pointEventKey                             ;  // 포인트 이벤트 KEY

    @Schema(description="주문번호. 적립을 하계 된 주문번호", example="3000003121")
    private Long                 orderNo                                  ;  // (적립을 하게된 계기의) 주문번호

    @Schema(description="원주문번호. 유효기간 만료 이후 사용취소 건일 때, 이전 포인트KEY", example="6")
    private Long                 orgPointKey                              ;  // 원 포인트 적립번호. 유효기간 경과된 건 사용취소시 적립

    @Schema(description="적립일자", example="2026-09-01")
    private String               accuDate                              ;  // 적립일자

    @Schema(description="만료일자", example="2027-09-01")
    private String               expireDate                               ;  // 만료일자

    @Schema(description="사용자 번호", example="303")
    private Long                 userNo                                   ;  // 사용자 번호

    @Schema(description="(원래)적립포인트", example="3400")
    private BigDecimal           accuPoint                             ;  // 적립 포인트

    @Schema(description="적립취소 여부", example="N or Y")
    private String               accuCancelYn                          ;  // 적립 취소 여부

    @Schema(description="포인트 잔액. 포인트 사용 전까지는 적립포인트(accuPoint)와 같다", example="2400")
    private BigDecimal           balancePoint                             ;  // 포인트 잔액

    @Schema(description="유효기간", example="365")
    private Long                 expireDays;

    @Schema(description="포인트 적립방식", example="1:일반, 2:관리자 수기지급")
    private String               pointAccuType;

}