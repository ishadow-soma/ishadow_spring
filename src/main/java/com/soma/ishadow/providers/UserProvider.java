package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponseStatus;
import com.soma.ishadow.domains.enums.IsSuccess;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user.UserConvertor;
import com.soma.ishadow.repository.user.UserConvertorRepository;
import com.soma.ishadow.repository.user.UserRepository;
import com.soma.ishadow.requests.GetPasswordReq;
import com.soma.ishadow.responses.GetMyroomRes;
import com.soma.ishadow.responses.GetUserRes;
import com.soma.ishadow.responses.IsSuccessRes;
import com.soma.ishadow.services.JwtService;
import com.soma.ishadow.services.UserService;
import com.soma.ishadow.utils.PasswordEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_GET_USER;
import static com.soma.ishadow.configures.BaseResponseStatus.INVALID_PASSWORD;

@Service
@Transactional(readOnly = true)
public class UserProvider {

    private final Logger logger = LoggerFactory.getLogger(UserProvider.class);
    private final UserConvertorRepository userConvertorRepository;
    private final UserRepository userRepository;
    private final VideoProvider videoProvider;
    private final UserService userService;
    private final JwtService jwtService;

    @Autowired
    public UserProvider(UserConvertorRepository userConvertorRepository, UserRepository userRepository, VideoProvider videoProvider, UserService userService, JwtService jwtService) {
        this.userConvertorRepository = userConvertorRepository;
        this.userRepository = userRepository;
        this.videoProvider = videoProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 내 프로필 조회
     * @return
     * @throws BaseException
     */
    @Transactional(readOnly = true)
    public GetUserRes getProfile() throws BaseException {

        Long userId = jwtService.getUserInfo();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAILED_TO_GET_USER));
        return GetUserRes.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .myPoint(user.getMyPoint())
                .gender(user.getGender())
                .sns(user.getSns())
                .build();
    }

    /**
     * 비밀번호 확인
     * @param getPasswordReq
     * @return
     * @throws BaseException
     */
    @Transactional(readOnly = true)
    public IsSuccessRes checkPassword(GetPasswordReq getPasswordReq) throws BaseException {
        String email = getPasswordReq.getEmail();
        String password = getPasswordReq.getPassword();
        String encodingPassword = PasswordEncoding.passwordEncoding(password);
        User user = userService.findByEmail(email);
        if(!user.getPassword().equals(encodingPassword)) {
            throw new BaseException(INVALID_PASSWORD);
        }
        return IsSuccessRes.builder()
                .isSuccess(IsSuccess.YES)
                .sns(user.getSns())
                .build();
    }


    @Transactional(readOnly = true)
    public IsSuccessRes getConversionCount() throws BaseException {
        Long userId = jwtService.getUserInfo();
        if( conversionCheck(userId) ) {
            return new IsSuccessRes(IsSuccess.YES);
        }
        return new IsSuccessRes(IsSuccess.NO);
    }

    public boolean conversionCheck(Long userId) throws BaseException {

        UserConvertor userConvertor = userConvertorRepository.findById(userId)
                .orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
        int count = userConvertor.getConvertorCount();
        if(count < 0 || count > 3) {
            return false;
        }
        return true;
    }

    public User findById(Long userId) throws BaseException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(FAILED_TO_GET_USER));
    }

    public GetMyroomRes getMyConversion() throws BaseException {

        Long userId = jwtService.getUserInfo();
        GetMyroomRes getMyroomRes = new GetMyroomRes();
        logger.info("start " + "userId: " + userId);
        getMyroomRes.addYoutubeVideos(videoProvider.findYoutubeVideoByUserId(userId));
        logger.info("getMyRoom: YoutubeSuccess - " + "userId: " + userId);
        getMyroomRes.addUploadVideos(videoProvider.findUploadVideoByUserId(userId));
        logger.info("getMyRoom: UploadSuccess - " + "userId: " + userId);
        getMyroomRes.addUploadAudios(videoProvider.findUploadAudioByUserId(userId));
        logger.info("getMyRoom: AudioSuccess - " + "userId: " + userId);
        return getMyroomRes;

    }

}
