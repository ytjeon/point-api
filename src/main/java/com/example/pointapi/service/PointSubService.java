package com.example.pointapi.service;

import com.example.pointapi.constants.ResultCodeEnum;
import com.example.pointapi.mapper.PointMapper;
import com.example.pointapi.mapper.UserMapper;
import com.example.pointapi.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DB Transaction 처리 때문에 PointService에서 분리
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PointSubService {
    private final PointMapper pointMapper;
    private final UserMapper userMapper;

    /**
     * 포인트 적립
     * @param reqDto
     * @param accuType
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseObject<BalancePointDto> savePoint(ReqPointDto reqDto, String accuType,Long orgPointKey) {
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
                .orgPointKey(orgPointKey)
                .build();

        pointMapper.insertPointAccuMst(insAccu);


        return new ResponseObject<>(ResultCodeEnum.SUCCESS);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUsePoint(ReqPointDto reqPointDto,List<PointAccuMstDto> pointList) {
        Long userNo = reqPointDto.getUserNo();
        Long orderNo = reqPointDto.getOrderNo();
        BigDecimal usePoint = reqPointDto.getPoint();

        // point_order_use_hst의 pk값을 point_order_use_map에 넣기 위해서
        // for문 안에서 바로 insert하지 않고 list로 모은다
        List<PointOrderUseMapDto> mapList = new ArrayList<>(pointList.size());

        // 포인트 사용 => 잔액 차감
        BigDecimal remainUsedPoint = usePoint;
        for(PointAccuMstDto po : pointList){
            if(remainUsedPoint.equals(BigDecimal.ZERO)){
                break;
            }

            Long pointKey = po.getPointKey();
            BigDecimal balancePoint = po.getBalancePoint();
            BigDecimal tmpPoint = balancePoint.subtract(remainUsedPoint);
            UsePointDto usePointDto = null;

            if(tmpPoint.compareTo(BigDecimal.ZERO) < 0){
                remainUsedPoint = tmpPoint.abs();
                usePointDto = new UsePointDto(pointKey, balancePoint);
                PointOrderUseMapDto useMapDto = PointOrderUseMapDto.builder()
                        .pointKey(pointKey)
                        .point(balancePoint)
                        .build();
                mapList.add(useMapDto);
            } else{
                BigDecimal realUsePoint = remainUsedPoint.subtract(tmpPoint);
                usePointDto = new UsePointDto(pointKey, realUsePoint);
                PointOrderUseMapDto useMapDto = PointOrderUseMapDto.builder()
                        .pointKey(pointKey)
                        .point(realUsePoint)
                        .build();
                mapList.add(useMapDto);

                // 위치 중요
                remainUsedPoint = BigDecimal.ZERO;
            }

            int uptCnt = pointMapper.updateBalance4usePoint(usePointDto);
            if(uptCnt == 0){
                throw new RuntimeException("==== point balance update count 0");
            }
        }

        // 사용포인트를 미쳐 다 사용 못하는 상황 => 롤백!!
        // 진행 도중에 다른 프로세스(?)가 잔액을 사용했거나 적립 취소를 했을 때
        if(!remainUsedPoint.equals(BigDecimal.ZERO)){
            throw new RuntimeException("==== remained use point : " + remainUsedPoint);
        }

        // 포인트 주문 사용 insert
        PointOrderUseHstDto hstDto = PointOrderUseHstDto.builder()
                .orderNo(orderNo)
                .userNo(userNo)
                .point(usePoint)
                .pointTradeType("P")
                .build();
        pointMapper.insertPointOrderUseHst(hstDto);

        // 포인트 주문 사용 매핑 insert
        Long hstNo = hstDto.getPointOrderUseNo();
        mapList.forEach( m -> m.setPointOrderUseNo(hstNo));
        for(final PointOrderUseMapDto mapDto : mapList){pointMapper.insertPointOrderUseMap(mapDto);}

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCancelPoint(ReqPointDto reqDto, List<PointAccuMstDto> accuList) {
        Long orderNo = reqDto.getOrderNo();
        Long userNo  = reqDto.getUserNo();
        BigDecimal cancelPoint = reqDto.getPoint();


        // point_order_use_hst의 pk값을 point_order_use_map에 넣기 위해서
        // for문 안에서 바로 insert하지 않고 list로 모은다
        List<PointOrderUseMapDto> mapList = new ArrayList<>();

        BigDecimal remainCanceldPoint = cancelPoint;
        for( PointAccuMstDto po : accuList  ){
            Long pointKey = po.getPointKey();
            String pointAccuType = po.getPointAccuType();

            // 만료일자 확인
            String expireDate =  po.getExpireDate().replaceAll("-", "");
            String today = LocalDate.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            // 만료 되었을 경우
            if(Long.parseLong(expireDate) < Long.parseLong(today)){
                if(remainCanceldPoint.equals(BigDecimal.ZERO)) break;

                // 신규 적립
                ReqPointDto newDto = ReqPointDto.builder()
                        .orderNo(orderNo)
                        .userNo(userNo)
                        .point(cancelPoint)
                        .build();
                savePoint(newDto,pointAccuType,pointKey);
            } else{ // 기간 유효할 경우
                BigDecimal balancePoint = po.getBalancePoint();
                BigDecimal realCancelPoint = balancePoint.subtract(remainCanceldPoint);

                //  -- 1. 잔액 복구
                UsePointDto usePointDto = null;
                if(realCancelPoint.compareTo(BigDecimal.ZERO) < 0){
                    usePointDto = new UsePointDto(pointKey, realCancelPoint);


                    PointOrderUseMapDto useMapDto = PointOrderUseMapDto.builder()
                            .pointKey(pointKey)
                            // 잔액을 계산(합산)하기 편하기 위해 취소는 음수 처리
                            .point(realCancelPoint.multiply(BigDecimal.valueOf(-1)))
                            .build();
                    mapList.add(useMapDto);

                    remainCanceldPoint = realCancelPoint.abs();
                } else {
                    usePointDto = new UsePointDto(pointKey, realCancelPoint);


                    PointOrderUseMapDto useMapDto = PointOrderUseMapDto.builder()
                            .pointKey(pointKey)
                            // 잔액을 계산(합산)하기 편하기 위해 취소는 음수 처리
                            .point(realCancelPoint.multiply(BigDecimal.valueOf(-1)))
                            .build();
                    mapList.add(useMapDto);

                    remainCanceldPoint =  BigDecimal.ZERO;
                }

                int uptCnt = pointMapper.updateBalance4useCancel(usePointDto);
                if(uptCnt == 0){
                    throw new RuntimeException("==== point balance update count 0");
                }
            }
        }


        //  -- 2. hst insert
        //
        PointOrderUseHstDto hstDto = PointOrderUseHstDto.builder()
                .orderNo(orderNo)
                .userNo(userNo)
                // 잔액을 계산(합산)하기 편하기 위해 취소는 음수 처리
                .point(cancelPoint.multiply(BigDecimal.valueOf(-1)))
                .build();
        pointMapper.insertPointOrderUseHst(hstDto);

        //  -- 3. map insert
        Long hstNo = hstDto.getPointOrderUseNo();
        mapList.forEach( m -> m.setPointOrderUseNo(hstNo));
        for(final PointOrderUseMapDto mapDto : mapList){pointMapper.insertPointOrderUseMap(mapDto);}

    }
}
