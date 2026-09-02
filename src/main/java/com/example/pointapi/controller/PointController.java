package com.example.pointapi.controller;

import com.example.pointapi.model.BalancePointDto;
import com.example.pointapi.model.ReqPointDto;
import com.example.pointapi.model.ResponseObject;
import com.example.pointapi.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
@Tag(name="포인트 API - 적립,적립취소,수기지급(적립),사용,사용취소")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description="SUCCESS"),
        @ApiResponse(responseCode = "400", description="Bad Request"),
        @ApiResponse(responseCode = "404", description="Not Found"),
        @ApiResponse(responseCode = "500", description="Failure"),
})
public class PointController {
    private final PointService pointService;

    @PostMapping("/saveNormalPoint")
    @Tag(name="")
    @Operation(summary = "적립(일반)" , description = "<ul><li>포인트를 적립합니다.</li></ul>")
    public ResponseObject saveNormalPoint(@RequestBody ReqPointDto reqDto) {
        return pointService.saveNormalPoint(reqDto);
    }

    @PostMapping("/saveManualPoint")
    @Tag(name="")
    @Operation(summary = "적립(관리자 수기지급)" , description = "<ul><li>포인트를 적립합니다.</li></ul>")
    public ResponseObject saveManualPoint(@RequestBody ReqPointDto reqDto) {
        return pointService.saveManualPoint(reqDto);
    }

    @GetMapping("/getBalancePointByUserNo")
    @Operation(summary = "사용자 잔액조회" , description = "<ul><li>특정 사용자의 포인트 잔액을 조회합니다.</li></ul>")
    public ResponseObject<BalancePointDto> getBalancePointByUserNo(@RequestParam("userNo") Long userNo) {
        return pointService.getBalancePointByUserNo(userNo);
    }

    @PostMapping("/cancelSavePoint")
    @Operation(summary = "적립취소" , description = "<ul><li>적립을 취소합니다</li><li>취소요청 입력값:pointKey</li></ul>")
    public ResponseObject<BalancePointDto> cancelSavePoint(@RequestBody Long pointKey) {
        return pointService.cancelSavePoint(pointKey);
    }

    @PostMapping("/usePoint")
    @Operation(summary = "포인트 사용" , description = "<ul><li>포인트를 사용(결제) 합니다..</li></ul>")
    public ResponseObject<BalancePointDto> usePoint(@RequestBody ReqPointDto reqDto) {
        return pointService.usePoint(reqDto);
    }
//
    @PostMapping("/cancelUsePoint")
    @Operation(summary = "포인트 사용취소" , description = "<ul><li>사용(결제)했던 포인트를 취소합니다..</li>"
                                                           + "<li>만약 사용한 포인트가 현재 기준으로 유효기간이 만료가 되었다면 포인트를 신규로 적립합니다</li></ul>")
    public ResponseObject<BalancePointDto> cancelUsePoint(@RequestBody ReqPointDto reqDto) {
        return pointService.cancelUsePoint(reqDto);
    }

}
