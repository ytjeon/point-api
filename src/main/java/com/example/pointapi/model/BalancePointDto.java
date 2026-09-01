package com.example.pointapi.model;


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
    private Long userNo;
    private String userName;
    private BigDecimal balancePoint;

    List<PointAccuMstDto> pointAccuMstDtoList;
}
