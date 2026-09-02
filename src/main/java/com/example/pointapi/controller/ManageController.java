package com.example.pointapi.controller;

import com.example.pointapi.constants.ResultCodeEnum;
import com.example.pointapi.model.PointAccuMstDto;
import com.example.pointapi.model.PointEventMstDto;
import com.example.pointapi.model.ResponseObject;
import com.example.pointapi.model.UserMstDto;
import com.example.pointapi.service.ManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage")
@Tag(name="포인트 Database 관리")
public class ManageController {
    private final ManageService manageService;

    @GetMapping("/getAllUsers")
    @Operation(summary = "사용자 조회" , description = "<ul><li>모든 사용자 정보를 조회합니다.</li></ul>")
    public ResponseObject<List<UserMstDto>> getAllUsers() {
        return new ResponseObject<>("0000","OK", manageService.selectAllUsers());
    }

    @GetMapping("/getAllPointEvents")
    @Operation(summary = "포인트 이벤트 조회" , description = "<ul><li>모든 포인트 이벤트를 조회합니다.</li></ul>")
    public ResponseObject<List<PointEventMstDto>> getAllPointEvents() {
        return new ResponseObject<>("0000","OK", manageService.selectAllPointEvents());
    }

    @GetMapping("/getAllPointAccuMst")
    @Operation(summary = "포인트 적립 원장 조회" , description = "<ul><li>모든 포인트 적립내역을 조회합니다.</li></ul>")
    public ResponseObject<List<PointAccuMstDto>> getAllPointAccuMst() {
        return new ResponseObject<>("0000","OK", manageService.selectAllPointAccuMst());
    }

    @PostMapping("/modifyPointEvent")
    @Operation(summary = "포인트 이벤트 수정" , description = "<ul><li>특정 포인트 이벤트의 유효기간  또는 1회 최대적립 포인트를 수정할 수 있습니다.</li></ul>")
    @Parameter(name="pointEventKey", description = "포인트 이벤트KEY" , example = "1")
    @Parameter(name="expireDays", description = "유효기간(1~365X5년)", example = "365")
    @Parameter(name="maxUnitPoint", description = "1회 최대 적립포인트", example = "50000")
    public ResponseObject modifyPointEvent(
            @RequestParam(name="pointEventKey") Long pointEventKey
            , @RequestParam("expireDays") Long expireDays
            , @RequestParam("maxUnitPoint") BigDecimal maxUnitPoint
    ) {
        PointEventMstDto pointEventMstDto = PointEventMstDto.builder()
                .pointEventKey(pointEventKey)
                .expireDays(expireDays)
                .maxUnitPoint(maxUnitPoint)
                .build();
        manageService.modifyPointEvent(pointEventMstDto);
        return new ResponseObject<>(ResultCodeEnum.SUCCESS );
    }

    @PostMapping("/modifyExpireDate")
    @Operation(summary = "테스트를 위해서 만료일자를 임의로 변경" , description = "<ul><li>테스트를 위해서 만료일자를 임의로 변경합니다.</li></ul>")
    @Parameter(name="pointKey", description = "포인트 KEY - 주문별 포인트 적립KEY. 1~", example = "3")
    @Parameter(name="expireDate", description = "포인트 만료일자", example = "2027-09-01")
    public ResponseObject modifyExpireDate(@RequestParam("pointKey") Long pointKey, @RequestParam("expireDate") String expireDate) {
        PointAccuMstDto pointAccuMstDto = PointAccuMstDto.builder().pointKey(pointKey).expireDate(expireDate).build();
        manageService.modifyExpireDate(pointAccuMstDto);
        return new ResponseObject<>(ResultCodeEnum.SUCCESS );
    }
}
