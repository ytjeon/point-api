package com.example.pointapi.mapper;

import com.example.pointapi.model.UserMstDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMstDto getUser(Long userNo);
}
