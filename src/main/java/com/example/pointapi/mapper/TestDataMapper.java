package com.example.pointapi.mapper;

import com.example.pointapi.model.PointAccuMstDto;
import com.example.pointapi.model.PointEventMstDto;
import com.example.pointapi.model.UserMstDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TestDataMapper {
    List<UserMstDto> selectAllUsers();
    List<PointEventMstDto> selectAllPointEvents();
    List<PointAccuMstDto> selectAllPointAccuMst();
}
