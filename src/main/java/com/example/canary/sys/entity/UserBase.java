package com.example.canary.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.canary.common.exception.ValidGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;

/**
 * user
 *
 * @since 1.0
 * @author zhaohongliang
 */
@Data
public class UserBase implements Serializable {

    @Serial
    private static final long serialVersionUID = -2424942548747771703L;

    /**
     * id
     */
    @NotBlank(groups = { ValidGroup.Edit.class })
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 账号
     */
    @NotBlank(groups = { ValidGroup.Add.class })
    @Pattern(regexp = "^[0-9a-zA-Z_]{6,18}$", groups = { ValidGroup.Add.class })
    private String account;

    /**
     * 昵称
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @Length(min = 1, max = 20, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String nickName;

    /**
     * 姓名
     */
    @NotBlank(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @Length(min = 1, max = 20, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private String realName;

    /**
     * 性别
     */
    @Range(min = 0, max = 1, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @NotNull(groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    private Integer sex;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$")
    private String mobileNo;

    /**
     * 是否禁用
     * @see com.example.canary.common.enums.StatusEnum.Disabled
     */
    @Range(min = 0, max = 1, groups = { ValidGroup.Add.class, ValidGroup.Edit.class })
    @TableField(value = "is_disabled")
    private Integer disabled;


}
