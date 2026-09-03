## project spec

### 개발 spec
 - java21  openjdk
 - springboot 3.5.14
 - gradle 8
 - H2 database / memory mode
 - mybatis

### build
```
gradle build -x test
```

### build 결과 jar 위치
```
point-api/build/libs/point-api.jar
```

### 실행방법
```
java -jar point-api.jar
```

### API 테스트 방법
 - swagger 주소 : http://localhost:7890/swagger-ui/index.html
 - 사용자 잔액 조회 화면 : http://localhost:7890/testview

### H2 콘솔 접근법
 - http://localhost:7890/h2-console 로 접속
 - jdbc url : jdbc:h2:mem:mydata

### 첨부파일 설명
 - schema.sql, data.sql :  DB Schema 생성과 기초데이타 생성 sql, 프로그램 기동시 자동 쿼리 실행
 - aws-아키텍쳐.drawio.png :  AWS 시스템 아키텍쳐 구성도 작성
 - 포인트API-ERD.pdf: E-R Diagram 
 - 포인트API-ERD.mermaid: E-R Diagram source 파일(mermaid 문법)

---

## 프로그램 설명

### ResponseObject
- 본 프로젝트의 구현 API에서 사용되는 기본 응답 객체입니다.
- Rest api에서 단순히 HttpStatus를 이용하는 것과 달리 별도의 결과코드와 결과메시지가 있으며 여러 결과(오류)에 대해서 구분해서 return할 수 있습니다.
- ResultCodeEnum.java에 결과 코드를 정의하였습니다.
- 결과코드(resultCode) 가 '0000'이 성공이며, 그 외에는 오류코드 입니다.
- ResponseObject의 결과 data는 Generic 타입으로 처리하므로 여러 결과 데이타를 return할 수 있습니다.
```
public ResponseObject<BalancePointDto> getBalancePointByUserNo(Long userNo) {
         .......
         .......
         .......
   return new ResponseObject<>(ResultCodeEnum.SUCCESS.getCode(),"OK", balancePointDto);
}
```


### 구현내용 설명
 - WEB API형태로 구현하였으며, swagger로 API DOC을 작성하였습니다.
 - 과제 요구안에 따라 포인트 적립,적립취소,사용,사용취소를 구현하였습니다. 
 - 결과 데이타에 해당 사용자의 포인트 잔액을 표시합니다. 예를 들어 포인트 사용을 하면 사용 이후에 변화된 잔액을 API결과 데이타로 바로 확인할 수 있습니다.
 - 포인트 적립은 일반과 관리자 수기지급 2가지 API로 나누어 구현하였습니다. 
 - 포인트 일반 적립과 사용,사용취소는 주문번호가 필요합니다. 하지만 관리자 수기지급으로 적립할 때는 별도의 주문번호가 필요 없습니다.
 - 포인트 사용 시 , 어떤 적립 포인트와 연결되어 있는지도 기록합니다.(point_order_use_map)
 - 개인별 포인트 잔액 조회 API도 개발하였습니다.
 - 테스트를 위해서 사용자 원장, 포인트 이벤트(관리) 원장, 포인트 적립원장을 조회할 수 있습니다.
 - 원할한 테스트를 위해 임의로 포인트의 유효기한 일자수와 1회 최대 적립 포인트를 수정할 수 있습니다.
 - 역시 원할한 테스트를 위해 이미 적립된 포인트의 만료일자를 수정할 수 있습니다.


