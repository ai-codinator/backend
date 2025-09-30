package com.aicodinator.backend.domain.common.domain.constant;

public enum CodeType {
    MBTI("MBTI", "MBTI 유형"),
    OCCP("OCCP", "직업 분류"),
    TASK("TASK", "업무 분류");

    private final String code;
    private final String description;

    CodeType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
