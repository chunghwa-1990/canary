package com.example.canary.common.mybatis;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源
 *
 * @author zhaohongliang 2023-10-17 15:44
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum ReadWriteEnum {

    /**
     * master
     */
    MASTER("master"),

    /**
     * slave1
     */
    SLAVE1("slave1"),

    /**
     * slave2
     */
    SLAVE2("slave2");

    /**
     * key
     */
    private final String key;

    public static List<ReadWriteEnum> getSlaveValues() {
        List<ReadWriteEnum> slaveValues = new ArrayList<>();
        slaveValues.add(SLAVE1);
        slaveValues.add(SLAVE2);
        return slaveValues;
    }

}
