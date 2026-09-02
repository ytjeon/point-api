package com.example.pointapi.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsePointDto {
    private Long pointKey;
    private BigDecimal usePoint;
}
