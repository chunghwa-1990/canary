package com.example.canary.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * json 工具类
 *
 * @author zhaohongliang 2023-10-30 21:36
 * @since 1.0
 */
public class JacksonUtils {
    private JacksonUtils() {
    }

    /**
     * 获取 ObjectMapper
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        // 设置 objectMapper 的访问权限
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 记录序列化之后的数据类型，方便反序列化，保留这行会报错：Unexpected token (VALUE_STRING)
        // objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        // LocalDate 和 LocalDateTime 序列化
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // 关闭日期默认的格式化方式， 默认是UTC日期格式 yyyy-MM-dd ‘T’ HH:mm:ss.SSS
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }

    /**
     * 获取 Jackson2JsonRedisSerializer，json 序列化
     *
     * @return
     */
    public static Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer() {
        return new Jackson2JsonRedisSerializer<>(getObjectMapper(), Object.class);
    }
}
