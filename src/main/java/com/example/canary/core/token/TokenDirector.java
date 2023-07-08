package com.example.canary.core.token;

import com.alibaba.fastjson2.JSON;
import com.example.canary.sys.entity.UserVO;

import java.io.Serial;
import java.io.Serializable;

/**
 * token 管理者
 *
 * @ClassName TokenDirector
 * @Description token 管理者
 * @Author zhaohongliang
 * @Date 2023-07-06 17:35
 * @Since 1.0
 */
public record TokenDirector(TokenBuilder tokenBuilder) implements Serializable {

    @Serial
    private static final long serialVersionUID = 7705274326908657070L;

    public String createToken(TokenProperties tokenProperties, UserVO userVo) {
        // claim
        String claim = JSON.toJSONString(userVo);
        tokenBuilder.setSecret(tokenProperties.getSecret());
        tokenBuilder.setExpires(tokenProperties.getExpires());
        tokenBuilder.setAudience(userVo.getId());
        tokenBuilder.setClaim(claim);
        return tokenBuilder.build();
    }
}
