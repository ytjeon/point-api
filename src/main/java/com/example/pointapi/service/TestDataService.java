package com.example.pointapi.service;

import com.example.pointapi.mapper.TestDataMapper;
import com.example.pointapi.model.PointAccuMstDto;
import com.example.pointapi.model.PointEventMstDto;
import com.example.pointapi.model.UserMstDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestDataService {
    private final TestDataMapper testDataMapper;

    public List<UserMstDto> selectAllUsers() {
        return testDataMapper.selectAllUsers();
    }

    public List<PointEventMstDto> selectAllPointEvents() {
        return testDataMapper.selectAllPointEvents();
    }

    public List<PointAccuMstDto> selectAllPointAccuMst() {
        return testDataMapper.selectAllPointAccuMst();
    }
}
