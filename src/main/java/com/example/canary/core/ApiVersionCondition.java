package com.example.canary.core;

import com.example.canary.common.constant.HeaderConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * api 接口版本要求
 *
 * @ClassName ApiVersionCondition
 * @Description api 接口版本要求
 * @Author zhaohongliang
 * @Date 2023-07-04 22:28
 * @Since 1.0
 */
@Slf4j
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    /**
     * default version
     */
    private static final String DEFAULT_VERSION = "v1.0";

    /**
     * 请求头-版本号匹配规则
     */
    private static final Pattern HEAD_VERSION_PREFIX_PATTERN = Pattern.compile("v(\\d\\.\\d)");

    /**
     * URL路径-版本号匹配规则
     */
    private static final Pattern URL_VERSION_PREFIX_PATTERN = Pattern.compile("/v(\\d\\.\\d)/");

    /**
     * 版本号
     */
    private String apiVersion;

    /**
     * 全参构造器
     *
     * @param apiVersion
     */
    public ApiVersionCondition(String apiVersion) {
        this.apiVersion = apiVersion;
    }


    @Override
    public @NotNull ApiVersionCondition combine(ApiVersionCondition other) {
        // 最近优先原则，方法定义的@ApiVersioin优先于类定义的@ApiVersion
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {

        /*
         * 获取请求头中的版本号信息，如果是路径方式，则使用 String versionStr = request.getRequestURI()
         * 并在设置 controller 的 @RequestMapping() 为 @RequestMapping("{api-version}/xxx"）
         */
        String versionStr = request.getHeader(HeaderConstant.HEADER_KEY_VERSION);


        if (!StringUtils.hasText(versionStr)) {
            versionStr = DEFAULT_VERSION;
        }

        // 增则匹配
        Matcher matcher = HEAD_VERSION_PREFIX_PATTERN.matcher(versionStr);
        if (matcher.find()) {
            // 获得符合条件的apiVersionCondition
            int version = getVersionNumber(matcher.group());
            if (version >= getVersionNumber(getApiVersion())) {
                return this;
            }
        }

        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        int versionPre = getVersionNumber(other.getApiVersion());
        int versionNext = getVersionNumber(getApiVersion());
        // 当出现多个符合匹配条件的 ApiVersionCondition，优先匹配版本号较大的
        return versionPre - versionNext;
    }

    /**
     * 获取版本号
     *
     * @param versionStr
     * @return
     */
    private int getVersionNumber(String versionStr) {
        if (!StringUtils.hasText(versionStr)) {
            return 0;
        }
        // 如果是“URL路径-版本号匹配规则”，先替换/
        if (versionStr.contains("/")) {
            versionStr = versionStr.replace("/", "");
        }
        if (versionStr.contains("v")) {
            versionStr = versionStr.replace("v", "");
        }
        if (versionStr.contains(".")) {
            versionStr = versionStr.replace(".", "");
        }
        return Integer.parseInt(versionStr);
    }

    /**
     * 获取 版本号
     *
     * @return apiVersion 版本号
     */
    public String getApiVersion() {
        return this.apiVersion;
    }

    /**
     * 设置 版本号
     *
     * @param apiVersion 版本号
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
