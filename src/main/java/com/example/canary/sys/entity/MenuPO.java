package com.example.canary.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * 菜单
 *
 * @author zhaohongliang 2023-09-08 20:38
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("s_sys_menu")
public class MenuPO extends MenuBase {

    @Serial
    private static final long serialVersionUID = 6015800942881921773L;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 是否删除
     * @see com.example.canary.common.enums.StatusEnum.Deleted
     */
    @TableField(value = "is_deleted")
    private String deleted;
}
