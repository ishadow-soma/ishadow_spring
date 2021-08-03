package com.soma.ishadow.providers;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.BaseResponseStatus;
import com.soma.ishadow.domains.user.User;
import com.soma.ishadow.repository.user.UserRepository;
import com.soma.ishadow.responses.GetUserRes;
import com.soma.ishadow.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProvider {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public UserProvider(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * 내 프로필 조회
     * @return
     * @throws BaseException
     */
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
}
