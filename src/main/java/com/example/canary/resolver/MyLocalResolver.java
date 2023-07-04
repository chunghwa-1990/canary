package com.example.canary.resolver;

import com.example.canary.common.constant.BaseConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * 自定义区域解析器
 *
 * @ClassName MyLocalResolver
 * @Description 自定义区域解析器
 * @Author zhaohongliang
 * @Date 2023-07-03 16:59
 * @Since 1.0
 */
public class MyLocalResolver implements LocaleResolver {

    @Override
    public @NotNull Locale resolveLocale(HttpServletRequest request) {
        // header: String lang = request.getHeader(BaseConstant.PARAM_KEY_LANG)
        String lang = request.getParameter(BaseConstant.PARAM_KEY_LANG);
        Locale locale = Locale.getDefault();
        if (StringUtils.hasText(lang)) {
            String[] array = lang.split("_");
            locale = new Locale(array[0], array[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(@NotNull HttpServletRequest request, HttpServletResponse response, Locale locale) {
        // TODO document why this method is empty
    }
}
