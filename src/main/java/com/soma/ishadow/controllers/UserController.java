package com.soma.ishadow.controllers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponse;
import com.soma.ishadow.providers.UserProvider;
import com.soma.ishadow.requests.*;
import com.soma.ishadow.responses.DeleteUserRes;
import com.soma.ishadow.responses.GetUserRes;
import com.soma.ishadow.responses.IsSuccessRes;
import com.soma.ishadow.responses.JwtRes;
import com.soma.ishadow.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api")
public class UserController {


    private final UserService userService;
    private final UserProvider userProvider;

    @Autowired
    public UserController(UserService userService, UserProvider userProvider) {
        this.userService = userService;
        this.userProvider = userProvider;
    }


    @GetMapping("/check")
    public String healthCheck(
    ) {
        return "server connection success";
    }

    /**
     * 회원 가입
     * @param parameters
     * @return
     * @throws BaseException
     */
    @ApiOperation(value = "회원 가입")
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
     */
    @PostMapping("/login")
    public BaseResponse<JwtRes> login(
            @RequestBody PostLoginReq parameters
    ) throws BaseException{

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
    @PatchMapping("/users")
    public BaseResponse<Void> update(
            @RequestBody PatchUserReq patchUserReq
    ) throws BaseException{

        try{
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
     * @param searchPasswordReq
     * @return
     */
    @PatchMapping("users/password")
    public BaseResponse<IsSuccessRes> searchPassword(
        @RequestBody SearchPasswordReq searchPasswordReq
    ) {
        try {
            return BaseResponse.succeed(userService.updatePassword(searchPasswordReq));
        } catch (BaseException exception ) {
            return BaseResponse.failed(exception.getStatus());
        }
    }

    /**
     * 인증 코드 검증
     * @param postAuthenticationCodeReq
     * @return
     */
    @PostMapping(value = "/users/authentication-code")
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
    @PostMapping("users/duplication-email")
    public BaseResponse<IsSuccessRes> DuplicationCheck(
            @RequestBody PostEmailReq postEmailReq
    ) {

        try {
            return BaseResponse.succeed(userService.duplicateEmail(postEmailReq));
        } catch (BaseException baseException) {
            return BaseResponse.failed(baseException.getStatus());
        }
    }

}
