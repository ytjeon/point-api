package com.example.pointapi.service;

import com.example.pointapi.constants.ResultCodeEnum;
import com.example.pointapi.mapper.PointMapper;
import com.example.pointapi.mapper.UserMapper;
import com.example.pointapi.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointService{
    private final PointMapper pointMapper;
    private final UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    public ResponseObject<BalancePointDto> savePoint(ReqPointDto reqDto, String accuType) {
        // 사용자 조회
        Long userNo = reqDto.getUserNo();
        Long orderNo = reqDto.getOrderNo();

        UserMstDto user = userMapper.getUser(userNo);
        if(user == null){
            return new ResponseObject<>(ResultCodeEnum.ERROR.getCode(), "존재하지 않는 사용자입니다");
        }
        String userName = user.getUserName();

        // 포인트 적립행사 조회
        PointEventMstDto pointEvent = pointMapper.getPointEvent(accuType);
        if(pointEvent == null){
            return new ResponseObject<>(ResultCodeEnum.ERROR.getCode(),"포인트 적립행사가 없습니다");
        }
        Long pointEventKey =  pointEvent.getPointEventKey();
        Long expireDays = pointEvent.getExpireDays();



        // 포인트 적립 최소/최대크기 점검
        BigDecimal point = reqDto.getPoint();
        BigDecimal maxUnit = pointEvent.getMaxUnitPoint();
        if(point.compareTo(BigDecimal.ZERO) <= 0 ){
            return new ResponseObject<>(ResultCodeEnum.LIMITPOINT);
        }
        if(point.compareTo(maxUnit) > 0){
            return new ResponseObject<>(ResultCodeEnum.LIMITPOINT.getCode(),"1회 적립가능 포인트 최대는 " + maxUnit +" 이하 입니다.");
        }

        // 적립(insert)
        PointAccuMstDto insAccu = PointAccuMstDto
                .builder()
                .userNo(userNo)
                .accuCancelYn("N")
                .orderNo(orderNo)
                .expireDays(expireDays)
                .balancePoint(point)
                .accuPoint(point)
                .pointEventKey(pointEventKey)
                .build();
        try {
            pointMapper.insertPointAccuMst(insAccu);
        } catch (DuplicateKeyException dke) {
            return new ResponseObject<>(ResultCodeEnum.DUPLICATE);

        }


        return new ResponseObject<>(ResultCodeEnum.SUCCESS);
    }

    public ResponseObject saveNormalPoint(ReqPointDto reqDto){
        return savePoint(reqDto,"1");
    }

    public ResponseObject saveManualPoint(ReqPointDto reqDto) {
        return savePoint(reqDto,"2");
    }

    // 사용자 번호로 사용자의 잔액 상태 조회
    public ResponseObject<BalancePointDto> getBalancePointByUserNo(Long userNo) {
        UserMstDto user = userMapper.getUser(userNo);
        if(user == null){
            return new ResponseObject<>(ResultCodeEnum.NOTFOUND);
        }
        String userName = user.getUserName();

        PointAccuMstDto selAccu = PointAccuMstDto.builder()
                .userNo(userNo)
                .build();
        List<PointAccuMstDto> accuList = pointMapper.getPointAccuList(selAccu);
        if(accuList.isEmpty()){
            // 사용자는 존재하지만 유효한 적립이 없을 경우
            BalancePointDto emptyDto = BalancePointDto.builder()
                    .userNo(userNo)
                    .userName(userName)
                    .balancePoint(BigDecimal.ZERO)
                    .build();
            return new ResponseObject<>(ResultCodeEnum.SUCCESS.getCode(),"OK",emptyDto);
        }



        // 총 잔액
        BigDecimal totalBalancePoint = accuList.stream().map(PointAccuMstDto::getBalancePoint).reduce(BigDecimal.ZERO, BigDecimal::add);

        BalancePointDto balancePointDto = BalancePointDto.builder()
                .userNo(userNo)
                .userName(userName)
                .balancePoint(totalBalancePoint)
                .pointAccuMstDtoList(accuList)
                .build();

        return new ResponseObject<>(ResultCodeEnum.SUCCESS.getCode(),"OK", balancePointDto);
    }

    public ResponseObject<BalancePointDto> cancelSavePoint(Long pointKey) {
        PointAccuMstDto selAccu = PointAccuMstDto.builder()
                .pointKey(pointKey)
                .build();
        List<PointAccuMstDto> beforeList = pointMapper.getPointAccuList(selAccu);
        if(beforeList.isEmpty()){
            return new ResponseObject<>(ResultCodeEnum.NOTFOUND);
        }
        PointAccuMstDto getDto = beforeList.getFirst();
        Long userNo = getDto.getUserNo();
        String accuCancelYn = getDto.getAccuCancelYn();
        BigDecimal accuPoint = getDto.getAccuPoint();
        BigDecimal balancePoint = getDto.getBalancePoint();

        // 기 적립취소건
        if(accuCancelYn.equals("Y")){
            return new ResponseObject<>(ResultCodeEnum.SUCCESS.getCode()
                    ,"기 적립취소건"
                    ,getBalancePointByUserNo(userNo).getData()
            );
        }

        // 적립금액과 잔액이 다를 경우 (즉 사용한 경우)
        if(!accuPoint.equals(balancePoint)){
            return new ResponseObject<>(ResultCodeEnum.CANCEL_NOT_ALLOWED);
        }


        // 적립취소
        int uptCnt = pointMapper.cancelSavePoint(pointKey);
       if(uptCnt == 0){
           return new ResponseObject<>(ResultCodeEnum.ERROR);
       } else{
           // update 성공하면 남은 잔액이 얼마인지 return
           return getBalancePointByUserNo(userNo);
       }
    }
//
//    public ResponseObject<BalancePointDto> usePoint(ReqPointDto reqDto) {
//    }
//
//    public ResponseObject<BalancePointDto> cancelUsePoint(ReqPointDto reqDto) {
//    }
//
//    public ResponseObject<BalancePointDto> getBalancePointByUserNo(Long userNo) {
//    }
}
