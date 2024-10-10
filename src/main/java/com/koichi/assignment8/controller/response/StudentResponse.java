package com.koichi.assignment8.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "成功した際のレスポンスメッセージ")
public class StudentResponse {
    @Schema(description = "レスポンスメッセージ")
    private String message;

    public StudentResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
