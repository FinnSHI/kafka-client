package com.aiit.kafkaclient.utils;

import com.aiit.kafkaclient.enums.ResultCodeEnum;
import com.aiit.kafkaclient.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class TokenUtils {

    private static final String BEARER = "Bearer ";
    private static final String[] SECRET_KEY = new String[]{"549cda887788d80b"};

    public static void verifyToken(String token) {
        if (Objects.isNull(token)) {
            log.error("token为空");
            throw new ApiException(ResultCodeEnum.NO_TOKEN_IN_REQUEST);
        }
        // 先检查token是否有前缀
        if (!token.startsWith(BEARER)) {
            log.error("token格式错误");
            throw new ApiException(ResultCodeEnum.TOKEN_FORMAT_ERROR);
        }
    }

    public static String getMd5(String str) {
        String md5 = "";
        md5 = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
        return md5;
    }

    public static Long getLocalTimeStamp() {
        return new Date().getTime() / 1000;
    }

    public static Boolean verifyMd5Token(String token) {
        if (Objects.isNull(token)) {
            log.error("token为空");
            throw new ApiException(ResultCodeEnum.NO_TOKEN_IN_REQUEST);
        }
        Long localTimeStamp = getLocalTimeStamp();
        for (String secretKey : SECRET_KEY) {
            for (int i = 0; i < 10; i++) {
                if (getMd5(secretKey + (localTimeStamp + i)).equals(token) ||
                        getMd5(secretKey + (localTimeStamp - i)).equals(token))
                    return true;
            }
        }

        return false;
    }
}
