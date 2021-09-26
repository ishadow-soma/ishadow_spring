package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponseStatus;
import com.soma.ishadow.domains.enums.IsSuccess;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.domains.user.UserConvertor;
import com.soma.ishadow.repository.user.UserConvertorRepository;
import com.soma.ishadow.repository.user.UserRepository;
import com.soma.ishadow.responses.GetMyroomRes;
import com.soma.ishadow.responses.GetUserRes;
import com.soma.ishadow.responses.IsSuccessRes;
import com.soma.ishadow.responses.YoutubeVideo;
import com.soma.ishadow.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.soma.ishadow.configures.BaseResponseStatus.FAILED_TO_GET_USER;

@Service
@Transactional(readOnly = true)
public class UserProvider {

    private final UserConvertorRepository userConvertorRepository;
    private final UserRepository userRepository;
    private final VideoProvider videoProvider;
    private final JwtService jwtService;

    @Autowired
    public UserProvider(UserConvertorRepository userConvertorRepository, UserRepository userRepository, VideoProvider videoProvider, JwtService jwtService) {
        this.userConvertorRepository = userConvertorRepository;
        this.userRepository = userRepository;
        this.videoProvider = videoProvider;
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
        getMyroomRes.addYoutubeVideos(videoProvider.findYoutubeVideoByUserId(userId));
        getMyroomRes.addUploadVideos(videoProvider.findUploadVideoByUserId(userId));
        //getMyroomRes.addUploadAudios();
        return getMyroomRes;
    }
}
