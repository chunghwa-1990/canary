package com.example.canary.common.enums;

/**
 * 禁用状态
 *
 * @ClassName DisabledStatusEnum
 * @Description 禁用状态
 * @Author zhaohongliang
 * @Date 2023-06-26 17:28
 * @Since 1.0
 */
public enum DisabledStatusEnum {
    FALSE(0, "否"),
    TRUE(1, "是");


    /**
     * code
     */
    private Integer code;

    /**
     * desc
     */
    private String desc;


    DisabledStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 获取 code
     *
     * @return code code
     */
    public Integer getCode() {
        return this.code;
    }

    /**
     * 设置 code
     *
     * @param code code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * 获取 desc
     *
     * @return desc desc
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * 设置 desc
     *
     * @param desc desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
