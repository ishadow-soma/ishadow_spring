package com.soma.ishadow.configures;

import com.fasterxml.jackson.annotation.JsonInclude;

public class BaseResponse<T> {

    private final Boolean success;
    private final int code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    //성공
    public BaseResponse(BaseResponseStatus baseResponseStatus, T data) {
        this.success = baseResponseStatus.getSuccess();
        this.code = baseResponseStatus.getCode();
        this.message = baseResponseStatus.getMessage();
        this.data = data;
    }

    //실패
    public BaseResponse(BaseResponseStatus baseResponseStatus) {
        this.success = baseResponseStatus.getSuccess();
        this.code = baseResponseStatus.getCode();
        this.message = baseResponseStatus.getMessage();
    }



    //제러닉 메소드 양식
    public static <T> BaseResponse<T> succeed(T data) {
        return new BaseResponse<>(BaseResponseStatus.SUCCESS, data);
    }

    public static <T> BaseResponse<T> succeed() {
        return new BaseResponse<>(BaseResponseStatus.SUCCESS);
    }

    /*
        <?>           // 타입 변수에 모든 타입을 사용할 수 있음.
        <? extends T> // T 타입과 T 타입을 상속받는 자손 클래스 타입만을 사용할 수 있음.
        <? super T>   // T 타입과 T 타입이 상속받은 조상 클래스 타입만을 사용할 수 있음.
     */
//    public static BaseResponse<?> failed(Throwable throwable) {
//        return failed(throwable.getMessage());
//    }

    public static <T> BaseResponse<T> failed(BaseResponseStatus baseResponseStatus) {
        return new BaseResponse<>(baseResponseStatus);
    }

    public T getData() { return data; }

    public Boolean getSuccess() {return success;}

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
