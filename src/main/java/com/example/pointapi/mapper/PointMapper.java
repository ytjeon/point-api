package com.example.pointapi.mapper;

import com.example.pointapi.model.PointAccuMstDto;
import com.example.pointapi.model.PointEventMstDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PointMapper {
    PointEventMstDto getPointEvent(@Param("pointAccuType") String pointAccuType);

    int insertPointAccuMst(PointAccuMstDto pointAccuMstDto);

    List<PointAccuMstDto> getPointAccuList(PointAccuMstDto pointAccuMstDto);
    int cancelSavePoint(@Param("pointKey") Long pointKey);
}
