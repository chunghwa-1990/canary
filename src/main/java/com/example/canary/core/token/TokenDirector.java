package com.example.canary.core.token;

import com.example.canary.sys.entity.UserVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serial;
import java.io.Serializable;

/**
 * token 管理者
 *
 * @since 1.0
 * @author zhaohongliang
 */
public record TokenDirector(TokenBuilder tokenBuilder) implements Serializable {

    @Serial
    private static final long serialVersionUID = 7705274326908657070L;

    /**
     * 生成 token
     *
     * @param tokenProperties 配置文件
     * @param userVo user
     * @return token
     */
    public String createToken(TokenProperties tokenProperties, UserVO userVo) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // claim
        String claim = objectMapper.writeValueAsString(userVo);
        tokenBuilder.setSecret(tokenProperties.getSecret());
        tokenBuilder.setExpires(tokenProperties.getExpires());
        tokenBuilder.setAudience(userVo.getId());
        tokenBuilder.setClaim(claim);
        return tokenBuilder.build();
    }
}
