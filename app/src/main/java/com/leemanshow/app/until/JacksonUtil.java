package com.leemanshow.app.until;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonUtil {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        // 添加自定义配置（可选）
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // 添加自定义模块（可选）
        // objectMapper.registerModule(new MyCustomModule());
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
