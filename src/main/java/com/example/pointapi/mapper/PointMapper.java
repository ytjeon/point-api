package com.example.pointapi.mapper;

import com.example.pointapi.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PointMapper {
    PointEventMstDto getPointEvent(@Param("pointAccuType") String pointAccuType);

    int insertPointAccuMst(PointAccuMstDto pointAccuMstDto);

    List<PointAccuMstDto> getPointAccuList(PointAccuMstDto pointAccuMstDto);
    int cancelSavePoint(@Param("pointKey") Long pointKey);

    int updateBalance4usePoint(UsePointDto usePointDto);
    int updateBalance4useCancel(UsePointDto usePointDto);
    int insertPointOrderUseHst(PointOrderUseHstDto pointOrderUseHstDto);
    int insertPointOrderUseMap(PointOrderUseMapDto dto);


    BigDecimal getPointOrderBalanceByOrderNo(@Param("orderNo")  Long orderNo);
    List<PointAccuMstDto> getPointAccuStatusByOrderNo(@Param("orderNo")  Long orderNo);
}
