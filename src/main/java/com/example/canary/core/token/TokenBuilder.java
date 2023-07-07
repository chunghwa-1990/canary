package com.example.canary.core.token;

import com.example.canary.sys.entity.UserBase;
import com.example.canary.sys.entity.UserPO;

/**
 * jwt
 *
 * @ClassName TokenBuilder
 * @Description jwt
 * @Author zhaohongliang
 * @Date 2023-07-06 15:06
 * @Since 1.0
 */
public interface TokenBuilder {

    void setSecret(String secret);

    void setExpires(Long expires);

    void setAudience(String audience);

    void setClaim(String claim);

    String build();
}
