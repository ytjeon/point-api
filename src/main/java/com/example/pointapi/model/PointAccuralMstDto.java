package com.example.pointapi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 포인트 적립원장
//  1) 사용자+주문별로 적립
//  2) 사용자+수지지급 적립
@Getter
@Setter
@ToString
public class PointAccuralMstDto {
    private Long                 pointAccuralNo                           ;  // 포인트 적립 번호
    private Long                 pointEventNo                             ;  // 포인트 이벤트 번호
    private Long                 orderNo                                  ;  // (적립을 하게된 계기의) 주문번호
    private Long                 orgPointAccuralNo                        ;  // 원 포인트 적립번호. 유효기간 경과된 건 사용취소시 적립
    private String               manualRegId                              ;  // 수기지급 담당자 아이디
    private String               accuralDate                              ;  // 적립일자
    private String               expireDate                               ;  // 만료일자
    private Long                 userNo                                   ;  // 사용자 번호
    private Long                 accuralPoint                             ;  // 적립 포인트
    private String               accuralCancelYn                          ;  // 적립 취소 여부
    private Long                 balancePoint                             ;  // 포인트 잔액

}