package com.healthsystem.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Integer getCurrentUserId() {
        // 需结合 SecurityContext 获取当前用户 ID
        return (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}