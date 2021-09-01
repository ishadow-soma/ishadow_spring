package com.soma.ishadow.services;

import com.amazonaws.services.simpleemail.model.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.Constant;
import com.soma.ishadow.domains.enums.IsSuccess;
import com.soma.ishadow.domains.enums.Status;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user.UserConvertor;
import com.soma.ishadow.repository.user.UserConvertorRepository;
import com.soma.ishadow.repository.user.UserRepository;
import com.soma.ishadow.requests.*;
import com.soma.ishadow.responses.DeleteUserRes;
import com.soma.ishadow.responses.IsSuccessRes;
import com.soma.ishadow.responses.JwtRes;
import com.soma.ishadow.utils.EmailSenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static com.soma.ishadow.configures.BaseResponseStatus.*;
import static com.soma.ishadow.utils.NaverLoginUtil.getNaverUserProfile;
import static com.soma.ishadow.utils.PasswordEncoding.passwordEncoding;


@Service
@Transactional
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserConvertorRepository userConvertorRepository;
    private final JwtService jwtService;
    private final EmailSenderUtil emailSenderUtil;
    private final Environment environment;

    @Autowired
    @Qualifier("AuthenticationRepository")
    private Set<String> authenticationCodeRepository;



    @Autowired
    public UserService(UserRepository userRepository, UserConvertorRepository userConvertorRepository, JwtService jwtService, EmailSenderUtil emailSenderUtil, Environment environment) {
        this.userRepository = userRepository;
        this.userConvertorRepository = userConvertorRepository;
        this.jwtService = jwtService;
        this.emailSenderUtil = emailSenderUtil;
        this.environment = environment;
    }

    /**
     * 회원 가입
     * @param parameters
     * @return
     * @throws BaseException
     */
    @Transactional
    public JwtRes createUser(PostSingUpReq parameters) throws BaseException, IOException {


        User user = null;

        if(parameters.getSns().equals("NAVER")) {
            user = requestUserByNaver(parameters);
            if(duplicateCheck(user.getEmail())) {
                throw new BaseException(DUPLICATED_USER);
            }
        }
        if(parameters.getSns().equals("GOOGLE")) {
            user = requestUserByGoogle(parameters);
            if(duplicateCheck(user.getEmail())) {
                throw new BaseException(DUPLICATED_USER);
            }
        }

        if(parameters.getSns().equals("NORMAL")) {
            if(duplicateCheck(parameters.getEmail())) {
                throw new BaseException(DUPLICATED_USER);
            }
            user = createUserConvertor(parameters);
        }

        if(user == null) {
            logger.info(parameters.getSns() + "토큰으로 유저 정보 조회에 실패 했습니다.");
            throw new BaseException(FAILED_TO_GET_USER_BY_SNS);
        }

        User saveUser;
        UserConvertor userConversion = userConvertUserConversion(user);
        try {
            saveUser = userRepository.save(user);
            userConvertorRepository.save(userConversion);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_POST_USER);
        }

        logger.info("새로운 user 등록 성공");

        return JwtRes.builder()
                .email(saveUser.getEmail())
                .jwt(jwtService.createJwt(saveUser.getUserId()))
                .build();
    }



    /**
     * 로그인
     * @param parameters
     * @return
     * @throws BaseException
     */
    @Transactional
    public JwtRes login(PostLoginReq parameters) throws BaseException, IOException {

        User loginUser;

        //바로 return 하는 경우 log는 어떻게 남기지??
        if(parameters.getSns().equals("NAVER")) {
            loginUser = requestLoginUserByNaver(parameters);
            User user = userRepository.findByEmail(loginUser.getEmail())
                    .orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
            logger.info("NAVER 로그인 성공: " +  user.getEmail() + "/" + user.getUserId());
            return JwtRes.builder()
                    .email(user.getEmail())
                    .jwt(jwtService.createJwt(user.getUserId()))
                    .build();
        }

        if(parameters.getSns().equals("GOOGLE")) {
            loginUser = requestLoginUserByGoogle(parameters);
            User user = userRepository.findByEmail(loginUser.getEmail())
                    .orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
            logger.info("Google 로그인 성공: " +  user.getEmail() + "/" + user.getUserId());
            return JwtRes.builder()
                    .email(user.getEmail())
                    .jwt(jwtService.createJwt(user.getUserId()))
                    .build();
        }

        if(parameters.getSns().equals("NORMAL")) {
            return userRepository.findByEmail(parameters.getEmail())
                    .filter(user -> passwordEncoding(parameters.getPassword()).equals(user.getPassword()))
                    .map(user -> JwtRes.builder().email(user.getEmail()).jwt(jwtService.createJwt(user.getUserId())).build())
                    .orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
        }

        throw new BaseException(INVALID_SNS);

    }



    /**
     * 사용자 수정
     * @param patchUserReq
     * @throws BaseException
     */
    @Transactional
    public void updateUser(PatchUserReq patchUserReq) throws BaseException {


        Long userId = jwtService.getUserInfo();

        User user = findById(userId);
        User newUser = user.updateUserConvertor(patchUserReq);

        try {
            userRepository.save(newUser);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_UPDATE_USER);
        }

    }

    /**
     * 사용자 삭제
     * @param parameters
     * @return
     * @throws BaseException
     */
    @Transactional
    public DeleteUserRes deleteUser(DeleteUserReq parameters) throws BaseException {

        Long userId = jwtService.getUserInfo();
        User user = findById(userId);
        User deleteUser = user.deleteUserConvertor(parameters.getPurposeOfUse());
        User deletedUser;

        try {
            deletedUser = userRepository.save(deleteUser);
        } catch (Exception exception) {
            throw new BaseException(FAILED_TO_DELETE_USER);
        }

        if(deletedUser.getStatus() == Status.YES){
            throw new BaseException(FAILED_TO_DELETE_USER);
        }

        return DeleteUserRes.builder()
                .userId(deleteUser.getUserId())
                .status(deletedUser.getStatus())
                .build();
    }

    /**
     * 이메일 인증
     * @param postEmailReq
     * @throws BaseException
     */
    @Transactional
    public void emailAuthenticationSend(PostEmailReq postEmailReq) throws BaseException {

        String email = postEmailReq.getEmail();

        emailCheck(postEmailReq.getEmail());
        ArrayList<String> toEmail = new ArrayList<>();
        toEmail.add(email);
        String authenticationCode = emailSenderUtil.createKey("code");
        String emailContent = emailSenderUtil.createEmailTemplate(authenticationCode);
        SendEmailRequest sendEmailRequest =
                new SendEmailRequest(Constant.sender , new Destination(toEmail), createMessage(emailContent));
        emailSenderUtil.sendEmail(sendEmailRequest);
        if( environment.getActiveProfiles()[0].equals("local") ) {
            authenticationCodeRepository.add(authenticationCode);
        } else {
            //redis
            authenticationCodeRepository.add(authenticationCode);
        }
    }

    /**
     * 인증코드 검증
     * @param postAuthenticationCodeReq
     * @return
     */
    @Transactional
    public IsSuccessRes authenticationCodeCheck(PostAuthenticationCodeReq postAuthenticationCodeReq) {

        String code = postAuthenticationCodeReq.getAuthenticationCode();
        if( environment.getActiveProfiles()[0].equals("local")) {
            if(authenticationCodeRepository.contains(code)) {
                return new IsSuccessRes(IsSuccess.YES);
            }
            return new IsSuccessRes(IsSuccess.NO);
        } else {
            if(authenticationCodeRepository.contains(code)) {
                return new IsSuccessRes(IsSuccess.YES);
            }
            return new IsSuccessRes(IsSuccess.NO);
        }
    }

    /**
     * 비밀번호 수정
     * @param updatePasswordReq
     * @return
     */
    @Transactional
    public IsSuccessRes updatePassword(UpdatePasswordReq updatePasswordReq) throws BaseException {

        String email = updatePasswordReq.getEmail();
        String password = updatePasswordReq.getPassword();
        String confirmPassword = updatePasswordReq.getConfirmPassword();
        emailCheck(email);
        if(!password.equals(confirmPassword)) {
            throw new BaseException(NOT_EQUAL_PASSWORD_AND_CONFIRM_PASSWORD);
        }

        User user = findByEmail(email);
        User newUser = user.updatePasswordConvertor(password);

        try {
            userRepository.save(newUser);
        } catch (Exception exception) {
            return new IsSuccessRes(IsSuccess.NO);
        }
        return new IsSuccessRes(IsSuccess.YES);
    }

    /**
     * 중복 이메일 검증
     * @param postEmailReq
     * @return
     * @throws BaseException
     */
    @Transactional
    public IsSuccessRes duplicateEmail(PostEmailReq postEmailReq) throws BaseException {

        emailCheck(postEmailReq.getEmail());
        Optional<User> user = userRepository.findByEmail(postEmailReq.getEmail());

        return user.map(value -> new IsSuccessRes(IsSuccess.NO, value.getSns())).orElseGet(() -> new IsSuccessRes(IsSuccess.YES, "NONE"));

    }


    private Message createMessage(String emailContent) {
        return new Message()
                .withSubject(new Content().withCharset("UTF-8").withData(Constant.subject))
                .withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(emailContent)));
    }


    private User requestUserByNaver(PostSingUpReq parameters) throws IOException, BaseException {
        String token = parameters.getUserToken(); // 네이버 로그인 접근 토큰;
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        String apiURL = "https://openapi.naver.com/v1/nid/me";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String userInformation = getNaverUserProfile(apiURL,requestHeaders);
        logger.info(userInformation);
        return createUserConvertor(userInformation, parameters.getSns());
    }

    private User requestUserByGoogle(PostSingUpReq parameters) throws IOException, BaseException {
        String token = parameters.getUserToken(); // 구글 로그인 접근 토큰;
        String header = "Bearer " +token; // Bearer 다음에 공백 추가
        String apiURL = "https://www.googleapis.com/oauth2/v3/userinfo";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String userInformation = getNaverUserProfile(apiURL,requestHeaders);
        checkToken(userInformation);
        logger.info(userInformation);
        return createUserConvertor(userInformation, parameters.getSns());
    }

    private User createUserConvertor(String parameters, String sns) throws BaseException {

        JsonElement element = JsonParser.parseString(parameters);
        String name;
        String email;

        try {
            if(sns.equals("NAVER")) {
                email = element.getAsJsonObject().get("response").getAsJsonObject().get("email").getAsString();
                name = element.getAsJsonObject().get("response").getAsJsonObject().get("name").getAsString();
            }
            else if(sns.equals("GOOGLE")) {
                email = element.getAsJsonObject().get("email").getAsString();
                name = element.getAsJsonObject().get("name").getAsString();
            }
            else {
                throw new BaseException(FAILED_TO_GET_SNS);
            }
        } catch (JsonIOException jsonIOException) {
            throw new BaseException(FAILED_TO_PASING_USER_BY_SNS);
        }

        return new User.Builder()
                .name(name)
                .email(email)
                .password("NONE")
                .gender("NONE")
                .myPoint(0L)
                .sns(sns)
                .purposeOfUse("NONE")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    private User requestLoginUserByGoogle(PostLoginReq parameters) throws BaseException, IOException {
        String token = parameters.getUserToken(); // 네이버 로그인 접근 토큰;
        if(token == null) {
            throw new BaseException(EMPTY_TOKEN);
        }
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        String apiURL = "https://www.googleapis.com/oauth2/v3/userinfo";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String userInformation = getNaverUserProfile(apiURL,requestHeaders);
        checkToken(userInformation);
        logger.info("login try by" + parameters.getSns());
        return createUserConvertor(userInformation, parameters.getSns());
    }

    private User requestLoginUserByNaver(PostLoginReq parameters) throws IOException, BaseException {
        String token = parameters.getUserToken(); // 네이버 로그인 접근 토큰;
        if(token == null) {
            throw new BaseException(EMPTY_TOKEN);
        }
        String header = "Bearer " + token; // Bearer 다음에 공백 추가
        String apiURL = "https://openapi.naver.com/v1/nid/me";
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("Authorization", header);
        String userInformation = getNaverUserProfile(apiURL,requestHeaders);
        if(JsonParser.parseString(userInformation).getAsJsonObject().get("resultcode").getAsString().equals("024")) {
            throw new BaseException(INVALID_NAVER_TOKEN);
        }
        logger.info("login try by" + parameters.getSns());
        return createUserConvertor(userInformation, parameters.getSns());
    }

    private User createUserConvertor(PostSingUpReq parameters) {
        return new User.Builder()
                .name(parameters.getName())
                .email(parameters.getEmail())
                .password(passwordEncoding(parameters.getPassword()))
                .age(0)
                .gender("NONE")
                .myPoint(0L)
                .sns(parameters.getSns())
                .purposeOfUse("NONE")
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(Status.YES)
                .build();
    }

    public User findById(Long userId) throws BaseException{
        return  userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
    }

    public User findByEmail(String email) throws BaseException{
        return  userRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
    }

    public boolean duplicateCheck(String email) {
        return  userRepository.findByEmail(email).isPresent();
    }

    public void emailCheck(String email) throws BaseException {

        if( email == null) {
            throw new BaseException(EMPTY_EMAIL);
        }
    }


    private UserConvertor userConvertUserConversion(User user) {
        return new UserConvertor(user.getUserId(), user, 0);
    }

    private void checkToken(String userInformation) throws BaseException {

        if(JsonParser.parseString(userInformation).getAsJsonObject().get("error") == null) {
            return;
        }
        if(JsonParser.parseString(userInformation).getAsJsonObject().get("error").getAsString().equals("invalid_request")) {
            throw new BaseException(INVALID_NAVER_TOKEN);
        }
    }

}

