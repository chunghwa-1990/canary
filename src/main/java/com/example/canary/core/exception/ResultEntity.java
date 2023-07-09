package com.example.canary.core.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * result entity
 *
 * @ClassName ResultEntity
 * @Description result entity
 * @Author zhaohongliang
 * @Date 2023-07-01 11:30
 * @Since 1.0
 */
@Setter
@Getter
public class ResultEntity<T extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = 2507800881538446628L;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应状态
     */
    private Boolean status;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 无参构造器
     */
    private ResultEntity() {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.status = true;
        this.message = ResultCodeEnum.SUCCESS.getMessage();
    }

    /**
     * 带参构造器
     *
     * @param data
     */
    private ResultEntity(T data) {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.status = true;
        this.data = data;
        this.message = ResultCodeEnum.SUCCESS.getMessage();
    }

    /**
     * 带参构造器
     *
     * @param errorEnum
     */
    private ResultEntity(ErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.status = false;
        this.message = errorEnum.getMessage();
    }

    /**
     * 带参构造器
     *
     * @param message
     */
    private ResultEntity(String message) {
        this.code = ResultCodeEnum.ERROR.getCode();
        this.status = false;
        this.message = message;
    }

    /**
     * 带参构造器
     *
     * @param code
     * @param message
     */
    private ResultEntity(Integer code, String message) {
        this.code = code;
        this.status = false;
        this.message = message;
    }

    /**
     * suceees method，but no result
     *
     * @param <T>
     * @return
     */
    public static <T extends Serializable> ResultEntity<T> success() {
        return new ResultEntity<>();
    }

    /**
     * suceees method, and has a return value
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T extends Serializable> ResultEntity<T> success(T data) {
        return new ResultEntity<>(data);
    }

    /**
     * fail method, default is server internal exception
     *
     * @param <T>
     * @return
     */
    public static <T extends Serializable> ResultEntity<T> fail() {
        ErrorEnum errorEnum = ResultCodeEnum.ERROR;
        return new ResultEntity<>(errorEnum);
    }

    /**
     *  fail method, default is server internal exception，but you can customize the code
     *
     * @param errorEnum
     * @param <T>
     * @return
     */
    public static <T extends Serializable> ResultEntity<T> fail(ErrorEnum errorEnum) {
        return new ResultEntity<>(errorEnum);
    }

    /**
     * fail method, default is server internal exception，but you can customize the prompt
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T extends Serializable> ResultEntity<T> fail(String message) {
        return new ResultEntity<>(message);
    }

    /**
     * fail method, default is server internal exception，but you can customize the code and prompt
     *
     * @param code
     * @param message
     * @param <T>
     * @return
     */
    public static <T extends Serializable> ResultEntity<T> fail(Integer code, String message) {
        return new ResultEntity<>(code, message);
    }
}
