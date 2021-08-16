package com.soma.ishadow.services;

import com.soma.ishadow.configures.BaseException;
import com.soma.ishadow.configures.Secret;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.soma.ishadow.configures.BaseResponseStatus.*;
import static com.soma.ishadow.configures.Constant.ACCESS_TOKEN_VALID_TIME;

@Service
@Transactional
public class JwtService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Jwt 생성
     * @param userId
     * @return
     */
    public String createJwt(Long userId) {
        Date now = new Date();
        long curTime = System.currentTimeMillis();
        return Jwts.builder()
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(new Date(curTime + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    /**
     *
     * @return
     */
    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("ACCESS-TOKEN");
    }

    /**
     * Jwt 만료시간 추출
     * @return String
     */
    public Date getExpireDate(String accessToken) throws BaseException {

        try {

            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);

            return claims.getBody().getExpiration();

        } catch (ExpiredJwtException exception) {
            logger.info("JWT가 만료되었습니다.");
            throw new BaseException(EXPIRED_JWT);
        }catch (Exception e){
            logger.info("유효하지 않은 JWT 입니다.");
            throw new BaseException(INVALID_JWT);
        }

    }

    /**
     * JWT에서 userId 및 Role 추출
     * @return int
     * @throws BaseException
     */
    public Long getUserInfo() throws BaseException {
        // 1. JWT 추출
        logger.debug("JWT 검증 시작");
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(EMPTY_JWT);
        }

        // 1.5 logout 확인
//        if(redisTemplate.opsForValue().get(accessToken) != null){
//            throw new BaseException(ALREADY_LOGOUT);
//        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try {

            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);

        } catch (ExpiredJwtException exception) {
            logger.debug("JWT가 만료되었습니다.");
            throw new BaseException(EXPIRED_JWT);
        }catch (Exception e){
            logger.debug("유효하지 않은 JWT 입니다.");
            throw new BaseException(INVALID_JWT);
        }

        // 3. userInfo 추출
        return (long)(claims.getBody().get("userId",Integer.class));
    }


}
