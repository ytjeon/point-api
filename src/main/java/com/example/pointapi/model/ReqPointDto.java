package com.example.pointapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqPointDto {
    @Schema(description="사용자번호", example="303" , required = true)
    private Long userNo;

    @Schema(description="주문번호", example="3000041219 or 0")
    private Long orderNo;

    @Schema(description="포인트", example="300",required = true)
    private BigDecimal point;
}
