package com.example.canary.sys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.io.Serial;

/**
 * user
 *
 * @ClassName UserAO
 * @Description user
 * @Author zhaohongliang
 * @Date 2023-07-06 12:34
 * @Since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAO extends UserBase {

    @Serial
    private static final long serialVersionUID = -5266978191980847566L;

    /**
     * 密码
     */
    private String password;

    public UserPO convertToPo() {
        UserPO userPo = new UserPO();
        BeanUtils.copyProperties(this, userPo);
        return userPo;
    }
}
