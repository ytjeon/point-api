package com.example.pointapi.model;

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
}