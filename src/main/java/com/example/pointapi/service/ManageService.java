package com.example.pointapi.service;

import com.example.pointapi.mapper.ManageMapper;
import com.example.pointapi.model.PointAccuMstDto;
import com.example.pointapi.model.PointEventMstDto;
import com.example.pointapi.model.UserMstDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManageService {
    private final ManageMapper manageMapper;

    public List<UserMstDto> selectAllUsers() {
        return manageMapper.selectAllUsers();
    }

    public List<PointEventMstDto> selectAllPointEvents() {
        return manageMapper.selectAllPointEvents();
    }

    public List<PointAccuMstDto> selectAllPointAccuMst() {
        return manageMapper.selectAllPointAccuMst();
    }

    public void modifyPointEvent(PointEventMstDto pointEventMstDto) {
        manageMapper.modifyPointEvent(pointEventMstDto);
    }

    public void modifyExpireDate(PointAccuMstDto pointAccuMstDto) {
        manageMapper.modifyExpireDate(pointAccuMstDto);
    }
}
