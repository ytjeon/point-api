package com.example.pointapi.controller;

import com.example.pointapi.model.PointAccuMstDto;
import com.example.pointapi.model.PointEventMstDto;
import com.example.pointapi.model.ResponseObject;
import com.example.pointapi.model.UserMstDto;
import com.example.pointapi.service.TestDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestDataController {
    private final TestDataService testDataService;

    @GetMapping("/getAllUsers")
    public ResponseObject<List<UserMstDto>> getAllUsers() {
        return new ResponseObject<>("0000","OK",testDataService.selectAllUsers());
    }

    @GetMapping("/getAllPointEvents")
    public ResponseObject<List<PointEventMstDto>> getAllPointEvents() {
        return new ResponseObject<>("0000","OK",testDataService.selectAllPointEvents());
    }

    @GetMapping("/getAllPointAccuMst")
    public ResponseObject<List<PointAccuMstDto>> getAllPointAccuMst() {
        return new ResponseObject<>("0000","OK",testDataService.selectAllPointAccuMst());
    }
}
