package com.soma.ishadow.controllers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponse;
import com.soma.ishadow.providers.UserProvider;
import com.soma.ishadow.requests.*;
import com.soma.ishadow.responses.*;
import com.soma.ishadow.services.JwtService;
import com.soma.ishadow.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    @Autowired
    public UserController(UserService userService, UserProvider userProvider, JwtService jwtService) {
        this.userService = userService;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }


    @PutMapping("/check")
    public BaseResponse<String> healthCheck(
    ) {
        logger.info("server health Check");
        return BaseResponse.succeed("server connection success");
    }

    @PatchMapping("/check")
    public BaseResponse<String> healthCheck1(
    ) {
        logger.info("server health Check1");
        return BaseResponse.succeed("server connection success1");
    }

    /**
     * JWT에서 userID 추출
     * @return
     */
    @GetMapping("/user-id")
    public BaseResponse<GetUserIdRes> getUserId(
    ) {

        try {
            Long userId = jwtService.getUserInfo();
            return BaseResponse.succeed(new GetUserIdRes(userId));
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }
    }


    //TODO 이메일 인증할 때 이메일도 같이 넘기고 회원가입할 때 email을 받아서 비교한다.
    /**
     * 회원 가입
     * @param parameters
     * @return
     * @throws BaseException
     */
    @ApiOperation(value = "회원 가입", notes = "sns -> 일반 = NORMAL, naver = NAVER, google = GOOGLE")
    @PostMapping(path = "/users")
    public BaseResponse<JwtRes> signUp(
            @RequestBody PostSingUpReq parameters
    ) throws BaseException, IOException {

        try {
            return BaseResponse.succeed(userService.createUser(parameters));
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }

    }

    /**
     * 로그인
     * @param parameters
     * @return
     * @throws BaseException
     */
    @PostMapping("/login")
    @ApiOperation(value = "로그인")
    public BaseResponse<JwtRes> login(
            @RequestBody PostLoginReq parameters
    ) throws BaseException, IOException {

        try{
            return BaseResponse.succeed(userService.login(parameters));
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }
    }


    /**
     * 내 프로필 조회
     * @return
     */
    @GetMapping("/users")
    @ApiOperation(value = "내 프로필 조회")
    public BaseResponse<GetUserRes> myProfile() {

        try {
            return BaseResponse.succeed(userProvider.getProfile());
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }
    }

    /**
     * 사용자 정보 수정
     * @param patchUserReq
     * @return
     * @throws BaseException
     */
    @PatchMapping(value = "/users")
    @ApiOperation(value = "내 프로필 수정")
    public BaseResponse<Void> update(
            @RequestBody PatchUserReq patchUserReq
    ) throws BaseException{

        try{
            logger.info("update user start");
            userService.updateUser(patchUserReq);
            return BaseResponse.succeed();
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }

    }




    /**
     * 사용자 정보 삭제
     * @param deleteUserReq
     * @return
     */
    @DeleteMapping("/users")
    @ApiOperation(value = "유저 삭제")
    public BaseResponse<DeleteUserRes> delete(
            @RequestBody DeleteUserReq deleteUserReq
    ) {
        try {
            return BaseResponse.succeed(userService.deleteUser(deleteUserReq));
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }
    }

    /**
     * 이메일 인증
     * @param postEmailReq
     * @return
     * @throws BaseException
     */
    @PostMapping("users/authentication-email")
    @ApiOperation(value = "이메일 인증")
    public BaseResponse<Void> emailAuthentication(
            @RequestBody PostEmailReq postEmailReq
    ) throws BaseException {

        try {
            userService.emailAuthenticationSend(postEmailReq);
            return BaseResponse.succeed();
        } catch (BaseException exception) {
            return BaseResponse.failed(exception.getStatus());
        }

    }

    /**
     * 비밀번호 수정
     * @param updatePasswordReq
     * @return
     */
    @PatchMapping("/users/password")
    @ApiOperation(value = "비밀번호 수정")
    public BaseResponse<IsSuccessRes> updatePassword(
        @RequestBody UpdatePasswordReq updatePasswordReq
    ) {
        try {
            return BaseResponse.succeed(userService.updatePassword(updatePasswordReq));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }
    }

    //TODO 비밀번호 수정
    /**
     * 비밀번호 변경 전 확인
     */
    @GetMapping("/users/password")
    @ApiOperation(value = "비밀번호 변경 전 확인")
    public BaseResponse<IsSuccessRes> checkPassword(
            @RequestBody GetPasswordReq getPasswordReq
    ) {
        try {
            return BaseResponse.succeed(userProvider.checkPassword(getPasswordReq));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }
    }

    /**
     * 비밀번호 찾기
     */
    //TODO 비밀번호 찾기


    /**
     * 인증 코드 검증
     * @param postAuthenticationCodeReq
     * @return
     */
    @PostMapping(value = "/users/authentication-code")
    @ApiOperation(value = "인증 코드 검증")
    public BaseResponse<IsSuccessRes> AuthenticationCodeCheck(
            @RequestBody PostAuthenticationCodeReq postAuthenticationCodeReq) {
        return BaseResponse.succeed(userService.authenticationCodeCheck(postAuthenticationCodeReq));
    }

    /**
     * 이메일 중복 검증
     * @param postEmailReq
     * @return
     */
    @ApiOperation(value = "이메일 중복 검증")
    @PostMapping("/users/duplication-email")
    public BaseResponse<IsSuccessRes> DuplicationCheck(
            @RequestBody PostEmailReq postEmailReq
    ) {

        try {
            return BaseResponse.succeed(userService.duplicateEmail(postEmailReq));
        } catch (BaseException baseException) {
            return BaseResponse.failed(baseException.getStatus());
        }
    }

    @GetMapping("/users/my-room")
    @ApiOperation(value = "마이룸 조회")
    public BaseResponse<GetMyroomRes> getMyRoom() {

        try {
            return BaseResponse.succeed(userProvider.getMyConversion());
        } catch (BaseException baseException) {
            return BaseResponse.failed(baseException.getStatus());
        }

    }


    @GetMapping("/users/media-convertor-limit")
    @ApiOperation(value = "오늘 영상 변환 횟수 조회")
    public BaseResponse<IsSuccessRes> ConversionCountCheck() {

        try {
            return BaseResponse.succeed(userProvider.getConversionCount());
        } catch (BaseException baseException) {
            return BaseResponse.failed(baseException.getStatus());
        }

    }

}
