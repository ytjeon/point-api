package com.example.pointapi.service;

import com.example.pointapi.constants.ResultCodeEnum;
import com.example.pointapi.mapper.PointMapper;
import com.example.pointapi.mapper.UserMapper;
import com.example.pointapi.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PointService{
    private final PointMapper pointMapper;
    private final UserMapper userMapper;
    private final PointSubService pointSubService;


    /**
     * 포인트 적립(일반)
     * @param reqDto
     * @return
     */
    public ResponseObject saveNormalPoint(ReqPointDto reqDto){
        return pointSubService.savePoint(reqDto,"1",0L);
    }

    /**
     * 포인트 적립(관리자 수기지급)
     * @param reqDto
     * @return
     */
    public ResponseObject saveManualPoint(ReqPointDto reqDto) {
        return pointSubService.savePoint(reqDto,"2",0L);
    }

    /**
     * 사용자 잔액 조회
     * @param userNo
     * @return
     */
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

    /**
     *
     * 적립취소
     * @param pointKey
     * @return
     */
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

        // 적립금액과 잔액이 다를 경우 =>즉 포인트를 사용했을 때는 취소불가
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

    /**
     * 포인트 사용
     * @param reqDto
     * @return
     */
    public ResponseObject<BalancePointDto> usePoint(ReqPointDto reqDto) {
        Long userNo = reqDto.getUserNo();
        Long orderNo = reqDto.getOrderNo();
        BigDecimal usePoint = reqDto.getPoint();


        ResponseObject<BalancePointDto> bobj =  getBalancePointByUserNo(userNo);
        ResultCodeEnum resultCode = ResultCodeEnum.findByCode(bobj.getResultCode());
        if( resultCode == ResultCodeEnum.SUCCESS ){
            BalancePointDto balancePointDto = bobj.getData();
            BigDecimal totalBalancePoint = balancePointDto.getBalancePoint();
            List<PointAccuMstDto> pointList = balancePointDto.getPointAccuMstDtoList();
            if(totalBalancePoint.compareTo(usePoint) < 0){
                // 잔액부족
                return new ResponseObject<>(ResultCodeEnum.BALANCELIMIT);
            } else{
                // 이미 sql 쿼리로 정렬 했지만 혹시 모르니..
                pointList.sort(
                        Comparator.comparing(PointAccuMstDto::getPointAccuType).reversed() // 포인트 적립 타입: 내림차순
                                .thenComparing(PointAccuMstDto::getExpireDate)           // 만료일자: 오름차순
                );

                // 포인트 사용 => 잔액 차감 update 작업
                try {
                    pointSubService.updateUsePoint(reqDto, pointList);
                }catch (Exception e){
                    log.error(e.getMessage());
                    return new ResponseObject<>(ResultCodeEnum.ERROR);
                }
            }

        } else{
            return new ResponseObject<>(resultCode);
        }

        // 성공 결과로 잔액 조회
        return getBalancePointByUserNo(userNo);
    }


    /**
     * 포인트 사용취소
     * @param reqDto
     * @return
     */
    public ResponseObject<BalancePointDto> cancelUsePoint(ReqPointDto reqDto){
        Long orderNo = reqDto.getOrderNo();
        Long userNo  = reqDto.getUserNo();
        BigDecimal cancelPoint = reqDto.getPoint();

        // 포인트 사용취소할  포인트 주문 사용내역 잔액 조회
        BigDecimal orderBalance = pointMapper.getPointOrderBalanceByOrderNo(orderNo);
        if(orderBalance.compareTo(cancelPoint) < 0){
            // 포인트 취소할 만큼의 금액이 없음
            return new ResponseObject<>(ResultCodeEnum.BALANCELIMIT);
        }

        // 포인트 적립별(pointKey)  정보 조회
        List<PointOrderUseMapDto> accuList = pointMapper.getPointAccuListByOrderNo(orderNo);
        if(accuList.isEmpty()){
            return new ResponseObject<>(ResultCodeEnum.NOTFOUND);
        }



        try {
            pointSubService.updateCancelPoint(reqDto,accuList);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseObject<>(ResultCodeEnum.ERROR);
        }

        return getBalancePointByUserNo(userNo);
    }


}
