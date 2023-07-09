package com.example.canary.core.context;

/**
 * Canary 上下文
 *
 * @ClassName CanaryContext
 * @Description Canary 上下文
 * @Author zhaohongliang
 * @Date 2023-07-09 23:23
 * @Since 1.0
 */
public class CanaryContext {

    private CanaryContext() {}

    private static final ThreadLocal<CurrentUser<?>> LOGIN_THREAD_LOCAL = new ThreadLocal<>();

    public static void setCurrentUser(CurrentUser<?> currentUser) {
        if (currentUser != null) {
            LOGIN_THREAD_LOCAL.set(currentUser);
        }
    }

    public static CurrentUser<?> getCurrentUser() {
        return LOGIN_THREAD_LOCAL.get();
    }

    /**
     * 清除当前用户信息
     */
    public static void removeCurrentUser() {
        LOGIN_THREAD_LOCAL.remove();
    }


}
