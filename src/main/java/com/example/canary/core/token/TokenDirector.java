package com.example.canary.core.token;

import com.example.canary.sys.entity.UserBase;
import com.example.canary.sys.entity.UserPO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public String createToken(TokenProperties tokenProperties, UserBase userBase) throws JsonProcessingException {
        // claim
        String claim = new ObjectMapper().writeValueAsString(userBase);
        tokenBuilder.setSecret(tokenProperties.getSecret());
        tokenBuilder.setExpires(tokenProperties.getExpires());
        tokenBuilder.setAudience(userBase.getId());
        tokenBuilder.setClaim(claim);
        return tokenBuilder.build();
    }
}
