package com.koichi.assignment8.excption;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class StudentControllerAdvice {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd 'T'HH:mm:ssZ'［'VV'］'");

    @ExceptionHandler(value = StudentNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleStudentNotFoundException(
            StudentNotFoundException e, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                ZonedDateTime.now().format(formatter),
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MultipleMethodsException.class)
    public ResponseEntity<Map<String, String>> handleMultipleMethodsException(
            MultipleMethodsException e, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                ZonedDateTime.now().format(formatter),
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * 以下の二つについての例外処理です。
     * 読み取り処理・登録処理・削除処理のID検索の際に文字列がリクエストされた場合
     * 学生でクエリパラメータの検索をする際に文字列がリクエストされた場合
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                ZonedDateTime.now().format(formatter),
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * 更新処理・削除処理の際に全学生がリクエストされた場合の
     * 例外処理です。
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        ErrorResponse body = new ErrorResponse(
                ZonedDateTime.now().format(formatter),
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "学生のIDを入力してください",
                request.getRequestURI());
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * バリデーションの例外処理です。
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Map<String, String>> errors = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            Map<String, String> error = new HashMap<>();
            error.put("field", fieldError.getField());
            error.put("message", fieldError.getDefaultMessage());
            errors.add(error);
        });

        ValidationErrorResponse validationErrorResponse =
                new ValidationErrorResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), "validation error", ZonedDateTime.now().format(formatter), errors);
        return ResponseEntity.badRequest().body(validationErrorResponse);
    }

    /**
     * エラーレスポンスのクラス
     */
    @Schema(description = "エラーレスポンス")
    public static class ErrorResponse {
        @Schema(description = "エラー発生時刻")
        private String timestamp;

        @Schema(description = "HTTPステータス")
        private String status;

        @Schema(description = "エラータイプ")
        private String error;

        @Schema(description = "エラーメッセージ")
        private String message;

        @Schema(description = "エラーが発生したパス")
        private String path;


        public ErrorResponse(String timestamp, String status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public String getPath() {
            return path;
        }
    }

    /**
     * バリデーションエラーレスポンスのクラス
     */
    @Schema(description = "バリデーションエラーレスポンス")
    public static final class ValidationErrorResponse {
        @Schema(description = "HTTPステータス")
        private final String status;

        @Schema(description = "エラーメッセージ")
        private final String message;

        @Schema(description = "エラー発生時刻")
        private final String timestamp;

        @Schema(description = "バリデーションエラーの詳細リスト")
        private final List<Map<String, String>> errors;


        public ValidationErrorResponse(String status, String message, String timestamp, List<Map<String, String>> errors) {
            this.status = status;
            this.message = message;
            this.timestamp = timestamp;
            this.errors = errors;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public List<Map<String, String>> getErrors() {
            return errors;
        }
    }

}



