package com.example.pointapi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 포인트 주문사용-적립 매핑
@Getter
@Setter
@ToString
public class PointOrderUseMapDto {
    private Long pointOrderUseMapNo;  // 번호
    private Long pointOrderUseNo;  // 포인트 사용 기록 번호
    private Long pointAccuralNo;  // 포인트 적립 번호
    private Long userNo;  // 사용자 번호
    private Long point;  // 포인트
}