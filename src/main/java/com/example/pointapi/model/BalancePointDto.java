package com.example.pointapi.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalancePointDto {
    @Schema(description="사용자 번호", example="303")
    private Long userNo;

    @Schema(description="사용자 이름", example="김삼순")
    private String userName;

    @Schema(description="(전체) 포인트 잔액", example="3400")
    private BigDecimal balancePoint;

    @Schema(description="포인트 적립 리스트")
    List<PointAccuMstDto> pointAccuMstDtoList;
}
