package com.example.pointapi.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {
    SUCCESS("0000","성공"),
    NOTFOUND("0404","NOT FOUND"),
    ERROR("9999","error"),
    LIMITPOINT("8888","1회 적립포인트 제한"),
    //DUPLICATE("7777","중복적립 불가"),
    EXPIRED("6666","유효기간 경과"),
    BALANCELIMIT("5555","잔액부족"),
    CANCEL_NOT_ALLOWED("4444","취소불가")
    ;

    private String code;
    private String name;

    public static ResultCodeEnum findByCode(String code) {
        for (ResultCodeEnum resultCodeEnum : values()) {
            if (resultCodeEnum.getCode().equals(code)) {
                return resultCodeEnum;
            }
        }
        return null;
    }
}
