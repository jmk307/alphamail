package com.osanvalley.moamail.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /* 공통 오류 */
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "C000", "서버 에러, 관리자에게 문의 바랍니다"),
    _BAD_REQUEST(BAD_REQUEST, "C001", "잘못된 요청입니다"),
    _UNAUTHORIZED(UNAUTHORIZED, "C002", "권한이 없습니다"),

    _METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED, "C003", "지원하지 않는 Http Method 입니다"),
    _UNSUPPORTED_MEDIA_TYPE(UNSUPPORTED_MEDIA_TYPE, "C004", "지원하지 않는 Http Media PendingType 입니다"),
    _INVALID_REQUEST_PARAMETER(BAD_REQUEST, "C005", "유효하지 않은 Request Parameter 입니다"),

    NOT_MULTIPART_HEADER(BAD_REQUEST, "C008", "Multipart 헤더가 아닙니다"),
    AMAZON_ACCESS_DENIED(FORBIDDEN, "C009", "Amazon S3 접근이 거부되었습니다"),

    /* Validation 오류 */
    PARAMETER_NOT_VALID(BAD_REQUEST, "P000", "인자가 유효하지 않습니다"),
    PASSWORD_CONFIRM_NOT_VALID(BAD_REQUEST, "P001", "입력된 비밀번호와 일치하지 않습니다."),

    /* Database 관련 오류 */
    DUPLICATE_RESOURCE(CONFLICT, "D001", "데이터가 이미 존재합니다"),

    /* Auth 관련 오류 */
    NO_TOKEN(UNAUTHORIZED, "AUTH000", "토큰이 존재하지 않습니다"),
    EXPIRED_TOKEN(UNAUTHORIZED, "AUTH001", "만료된 엑세스 토큰입니다"),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "AUTH002", "리프레시 토큰이 유효하지 않습니다"),
    MISMATCH_REFRESH_TOKEN(UNAUTHORIZED, "AUTH003", "리프레시 토큰의 유저 정보가 일치하지 않습니다"),
    EXPIRED_REFRESH_TOKEN(UNAUTHORIZED, "AUTH004", "만료된 리프레시 토큰입니다"),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "AUTH005", "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_USER(UNAUTHORIZED, "AUTH006", "로그인이 필요합니다"),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "AUTH007", "로그아웃 된 사용자입니다"),
    FORBIDDEN_USER(FORBIDDEN, "AUTH008", "권한이 없는 유저입니다"),
    UNSUPPORTED_TOKEN(UNAUTHORIZED, "AUTH009", "지원하지 않는 토큰입니다"),
    INVALID_SIGNATURE(UNAUTHORIZED, "AUTH010", "잘못된 JWT 서명입니다"),
    MISMATCH_VERIFICATION_CODE(UNAUTHORIZED, "AUTH011", "인증번호가 일치하지 않습니다"),
    EXPIRED_VERIFICATION_CODE(UNAUTHORIZED, "AUTH012", "인증번호가 만료되었습니다"),
    INVALID_USER_TOKEN(UNAUTHORIZED, "AUTH013", "서버에 토큰과 일치하는 정보가 없습니다"),

    LOGIN_FAILED(UNAUTHORIZED, "AUTH013", "로그인에 실패했습니다"),
    SOCIAL_ALREADY_EXIST(BAD_REQUEST, "AUTH014", "소셜로 가입된 회원입니다"),
    INVALID_ACCESS_TOKEN(UNAUTHORIZED, "AUTH015", "유효하지 않은 엑세스 토큰입니다"),
    COMMON_ALREADY_EXIST(BAD_REQUEST, "AUTH016", "일반으로 가입된 회원입니다"),
    JOIN_NOT_ACCEPTED(BAD_REQUEST, "AUTH017", "개인정보 수집 및 이용에 동의를 하여야 회원가입이 진행됩니다."),
    KAKAO_BAD_REQUEST(BAD_REQUEST, "AUTH018", "카카오 회원가입에 실패했습니다."),
    GOOGLE_BAD_REQUEST(BAD_REQUEST, "AUTH019", "구글 회원가입에 실패했습니다."),
    APPLE_BAD_REQUEST(BAD_REQUEST, "AUTH020", "애플 회원가입에 실패했습니다."),

    /* 이미지 관련 오류 */
    INVALID_FILE_EXTENSION(BAD_REQUEST, "FILE000", "잘못된 파일 확장자명입니다"),
    FILE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "FILE001", "파일 업로드에 실패했습니다"),

    /* Member 관련 오류 */
    MEMBER_ALREADY_EXIST(BAD_REQUEST, "M002","이미 가입된 유저입니다"),
    MEMBER_NOT_FOUND(NOT_FOUND, "M003","해당 유저 정보를 찾을 수 없습니다"),
    COUNTRY_NOT_FOUND(NOT_FOUND, "M004", "해당 국가를 찾을 수 없습니다"),
    USER_ALREADY_LOGGED_OUT(BAD_REQUEST, "M005", "이미 로그아웃된 유저입니다"),

    /* 알림 관련 오류 */
    CANNOT_CREATE_TUPLE(INTERNAL_SERVER_ERROR, "DB000", "새로운 인스턴스 생성을 실패했습니다"),

    /* 스크랩 관련 오류 */
    SCRAP_FOLDER_NOT_FOUND(NOT_FOUND, "S000", "해당 스크랩 폴더를 찾을 수 없습니다"),
    ALREADY_SCRAPED(BAD_REQUEST, "S001", "이미 스크랩한 콘텐츠입니다"),

    /* 홈 관련 오류 */
    COURSE_NOT_FOUND(NOT_FOUND, "C000", "해당 코스를 찾을 수 없습니다"),
    PLACE_NOT_FOUND(NOT_FOUND, "C002", "해당 장소를 찾을 수 없습니다"),

    /* 공지사항 관련 오류 */
    NOTICE_NOT_FOUND(NOT_FOUND, "N000", "해당 공지사항을 찾을 수 없습니다"),

    /* 팔로우 관련 오류 */
    FOLLOW_NOT_FOUND(NOT_FOUND, "F000", "해당 팔로우를 찾을 수 없습니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String detail;
}
