package com.example.pointapi;

import com.example.pointapi.model.BalancePointDto;
import com.example.pointapi.model.PointAccuMstDto;
import com.example.pointapi.model.ResponseObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PointApiApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("적립테스트1. 대상 사용자번호:101")
    void TestCase01() {

        Integer point1 = 1000;
        Integer point2 = 2000;
        Integer point3 = 1500;

        ResponseObject<BalancePointDto> res1 =  savePoint("1",101,0, point1);
        ResponseObject<BalancePointDto> res2 =  savePoint("1",101,0, point2);
        ResponseObject<BalancePointDto> res3 =  savePoint("1",101,0, point3);

        Integer totalPoint = point1 + point2 + point3;

        // 4. Then: 결과 검증
        // HTTP 통신이나 기타 exception 오류 문제 없는 걸로...

        Assertions.assertEquals("0000", res1.getResultCode());
        Assertions.assertEquals("0000", res2.getResultCode());
        Assertions.assertEquals("0000", res3.getResultCode());

    }


    @Test
    @DisplayName("적립테스트2(적립취소). 대상 사용자번호:202")
    void TestCase02() {

        Integer point1 = 1000;
        Integer point2 = 2000;
        Integer point3 = 1500;

        ResponseObject<BalancePointDto> res1 =  savePoint("1",202,0, point1);
        ResponseObject<BalancePointDto> res2 =  savePoint("2",202,0, point2);
        ResponseObject<BalancePointDto> res3 =  savePoint("1",202,0, point3);

        Assertions.assertEquals("0000", res1.getResultCode());
        Assertions.assertEquals("0000", res2.getResultCode());
        Assertions.assertEquals("0000", res3.getResultCode());

        BigDecimal beforeBalance = getBalancePointByUserNo(202).getData().getBalancePoint();
        System.out.println("before balance:" + beforeBalance);


        PointAccuMstDto dto2 = getBalancePointByUserNo(202).getData().getPointAccuMstDtoList().get(1);
        Long pointKey2 = dto2.getPointKey();
        cancelSavePoint(pointKey2);

        BigDecimal afterBalance = getBalancePointByUserNo(202).getData().getBalancePoint();
        System.out.println("after balance:" + afterBalance);

    }


    @Test
    @DisplayName("포인트 사용 테스트. 대상 사용자번호:202")
    void TestCase03() {
        Integer point1 = 1000;
        Integer point2 = 2000;
        Integer point3 = 1500;

        ResponseObject<BalancePointDto> res1 =  savePoint("1",202,0, point1);
        ResponseObject<BalancePointDto> res2 =  savePoint("2",202,0, point2);
        ResponseObject<BalancePointDto> res3 =  savePoint("1",202,0, point3);

        Assertions.assertEquals("0000", res1.getResultCode());
        Assertions.assertEquals("0000", res2.getResultCode());
        Assertions.assertEquals("0000", res3.getResultCode());

        BigDecimal beforeBalance = getBalancePointByUserNo(202).getData().getBalancePoint();
        System.out.println("before balance:" + beforeBalance);

        usePoint(202, 300003333, 2700);


        BigDecimal afterBalance = getBalancePointByUserNo(202).getData().getBalancePoint();
        System.out.println("after balance:" + afterBalance);
    }


    @Test
    @DisplayName("포인트 사용 취소 테스트. 대상 사용자번호:202")
    void TestCase04() {
        Integer point1 = 1000;
        Integer point2 = 2000;
        Integer point3 = 1500;

        ResponseObject<BalancePointDto> res1 =  savePoint("1",202,0, point1);
        ResponseObject<BalancePointDto> res2 =  savePoint("2",202,0, point2);
        ResponseObject<BalancePointDto> res3 =  savePoint("1",202,0, point3);

        Assertions.assertEquals("0000", res1.getResultCode());
        Assertions.assertEquals("0000", res2.getResultCode());
        Assertions.assertEquals("0000", res3.getResultCode());

        BigDecimal beforeBalance = getBalancePointByUserNo(202).getData().getBalancePoint();
        System.out.println("before balance:" + beforeBalance);

        usePoint(202, 300003334, 2700);


        BigDecimal afterBalance = getBalancePointByUserNo(202).getData().getBalancePoint();
        System.out.println("after balance:" + afterBalance);


        cancelUsePoint(202, 300003334, 1400);
        BigDecimal afterBalanceCancel = getBalancePointByUserNo(202).getData().getBalancePoint();
        System.out.println("after cancel balance:" + afterBalanceCancel);
    }


    // 적립
    private ResponseObject<BalancePointDto>  savePoint(String accuType,Integer userNo, Integer orderNo, Integer lpoint){
        BigDecimal point = BigDecimal.valueOf(lpoint);

        // given
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("userNo", userNo.toString());
        params1.add("orderNo", orderNo.toString());
        params1.add("point", point.toString());

        // 2. Header 설정 (Content-Type: application/x-www-form-urlencoded)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Header와 Parameter를 묶어 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params1, headers);

        String apiUrl = "/api/point/saveNormalPoint";
        if(accuType.equals("2")) apiUrl = "/api/point/saveManualPoint";

        // 3. When: POST API 호출
        ResponseEntity<ResponseObject<BalancePointDto>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResponseObject<BalancePointDto>>() {}
        );

        return response.getBody();
    }

    // 적립취소
    private ResponseObject<BalancePointDto>  cancelSavePoint(Long pointKey){
        // given
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("pointKey", pointKey.toString());

        // 2. Header 설정 (Content-Type: application/x-www-form-urlencoded)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Header와 Parameter를 묶어 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params1, headers);

        String apiUrl = "/api/point/cancelSavePoint";

        // 3. When: POST API 호출
        ResponseEntity<ResponseObject<BalancePointDto>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResponseObject<BalancePointDto>>() {}
        );

        return response.getBody();
    }

    // 사용
    private ResponseObject<BalancePointDto>  usePoint(Integer userNo, Integer orderNo, Integer point){
        // given
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("userNo", userNo.toString());
        params1.add("orderNo", orderNo.toString());
        params1.add("point", point.toString());

        // 2. Header 설정 (Content-Type: application/x-www-form-urlencoded)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Header와 Parameter를 묶어 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params1, headers);

        String apiUrl = "/api/point/usePoint";

        // 3. When: POST API 호출
        ResponseEntity<ResponseObject<BalancePointDto>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResponseObject<BalancePointDto>>() {}
        );

        return response.getBody();
    }


    // 사용취소
    private ResponseObject<BalancePointDto>  cancelUsePoint(Integer userNo, Integer orderNo, Integer point){
        // given
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("userNo", userNo.toString());
        params1.add("orderNo", orderNo.toString());
        params1.add("point", point.toString());

        // 2. Header 설정 (Content-Type: application/x-www-form-urlencoded)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Header와 Parameter를 묶어 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params1, headers);

        String apiUrl = "/api/point/cancelUsePoint";

        // 3. When: POST API 호출
        ResponseEntity<ResponseObject<BalancePointDto>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<ResponseObject<BalancePointDto>>() {}
        );

        return response.getBody();
    }

    // 적립취소
    private ResponseObject<BalancePointDto>  getBalancePointByUserNo(Integer userNo){
        // given
        MultiValueMap<String, String> params1 = new LinkedMultiValueMap<>();
        params1.add("userNo", userNo.toString());

        // 2. Header 설정 (Content-Type: application/x-www-form-urlencoded)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Header와 Parameter를 묶어 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params1, headers);

        String apiUrl = "/api/point/getBalancePointByUserNo?userNo="+userNo;

        // 3. When: POST API 호출
        ResponseEntity<ResponseObject<BalancePointDto>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseObject<BalancePointDto>>() {}
        );

        return response.getBody();
    }
}
