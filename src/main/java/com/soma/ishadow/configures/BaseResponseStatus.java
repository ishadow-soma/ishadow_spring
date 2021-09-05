package com.soma.ishadow.configures;

public enum BaseResponseStatus {

    SUCCESS(true,200, "요청에 성공했습니다."),
    INVALID_JWT( false, 401,"인증에 실패 했습니다."),
    DUPLICATED_USER( false, 1001,"중복 회원이 존재합니다."),
    EXPIRED_JWT( false, 1002,"JWT가 만료되었습니다."),
    EMPTY_JWT( false, 1003,"JWT가 비었습니다."),
    EMPTY_EMAIL( false, 1004,"Email이 비었습니다."),
    NOT_EQUAL_PASSWORD_AND_CONFIRM_PASSWORD( false, 1005,"비밀번호와 인증 비밀번호가 일치하지 않습니다."),
    EXCEED_CONVERSION_COUNT( false, 1006,"하루 가능한 영상 변환 횟수를 초과 했습니다."),
    EMPTY_VIDEO( false, 1007,"영상이 비었습니다."),
    FAILED_TO_TRANSLATE_FILE( false, 1008,"파일 변환에 실패 했습니다."),
    UNSUPPORTED_FORMAT( false, 1009,"지원하지 않는 파일 형식입니다."),
    EMPTY_TOKEN( false, 1010,"토큰이 비었습니다."),
    INVALID_NAVER_TOKEN( false, 1011,"잘못된 토큰입니다."),
    INVALID_AUDIO_TYPE( false, 1012,"잘못된 영상 변환 방식입니다."),
    FAILED_TO_PARSING_SCRIPT( false, 1013,"스크립트 변환에 실패했습니다."),
    INVALID_SNS( false, 1014,"잘못된 SNS 입니다."),
    FAILED_TO_GET_CATEGORY( false, 1015,"잘못된 카테고리 입니다."),





    FAILED_TO_POST_USER( false, 2000,"회원 가입에 실패했습니다."),
    FAILED_TO_GET_USER( false, 2001,"사용자 조회에 실패 했습니다."),
    FAILED_TO_UPDATE_USER( false, 2002,"사용자 수정에 실패 했습니다."),
    FAILED_TO_DELETE_USER (false, 2003,"사용자 삭제에 실패 했습니다."),
    FAILED_TO_GET_USER_BY_SNS(false, 2004, "사용자 정보 조회에 실패 했습니다."),
    FAILED_TO_PASING_USER_BY_NAVER(false, 2005, "사용자 정보 조회에 실패 했습니다."),
    FAILED_TO_POST_AUDIO(false, 2006, "영상 저장에 실패 했습니다."),
    FAILED_TO_POST_USERAUDIO(false, 2007, "사용자와 영상 조인 테이블에 생성에 실패했습니다."),
    FAILED_TO_POST_SENTENCE(false, 2008, "문장 저장에 실패했습니다."),
    FAILED_TO_GET_USERVIDEO(false, 2009, "사용자의 영상 조회에 실패 했습니다."),
    FAILED_TO_GET_VIDEO(false, 2010, "영상 조회에 실패 했습니다."),
    FAILED_TO_GET_SENTENCE_EN(false, 2011, "스크립트 조회에 실패했습니다."),
    FAILED_TO_GET_WORDS(false, 2012, "문장 조회에 실패했습니다."),
    FAILED_TO_GET_VIDEO_YOUTUBE(false, 2013, "영상 변환에 실패 했습니다. 다시 시도해주세요."),
    FAILED_TO_POST_BOOKMARK(false, 2014, "즐겨 찾기 저장에 실패 했습니다."),
    FAILED_TO_GET_BOOKMARK(false, 2015, "즐겨 찾기 조회에 실패 했습니다."),
    FAILED_TO_PASING_USER_BY_SNS(false, 2016, "SNS을 이용해서 사용자 정보 조회에 실패 했습니다."),
    FAILED_TO_GET_SNS(false, 2017, "잘못된 SNS입니다.");

    private final Boolean success;
    private final int code;
    private final String message;

    BaseResponseStatus(Boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    BaseResponseStatus(Boolean success, int code, String User, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
