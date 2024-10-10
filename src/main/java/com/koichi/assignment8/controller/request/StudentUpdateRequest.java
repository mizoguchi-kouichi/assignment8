package com.koichi.assignment8.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

/**
 * 指定したidのstudentの name,grade,birthplaceを更新するREAD処理
 * で使用するRequestです。
 */
@Schema(description = "生徒情報更新リクエスト")
public class StudentUpdateRequest {

    @Schema(description = "生徒の名前", example = "山田太郎", required = true)
    @NotBlank(message = "nameを入力してください")
    private String name;

    @Schema(description = "生徒の学年", example = "一年生", required = true)
    @ValidGrade
    private String grade;

    @Schema(description = "生徒の出身地", example = "東京都", required = true)
    @NotBlank(message = "birthPlaceを入力してください")
    private String birthPlace;

    public StudentUpdateRequest(String name, String grade, String birthPlace) {
        this.name = name;
        this.grade = grade;
        this.birthPlace = birthPlace;
    }

    public String getName() {
        return name;
    }

    public String getGrade() {
        return grade;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public enum Grade {
        一年生,
        二年生,
        三年生,
        卒業生;

        public static StudentUpdateRequest.Grade from(String value) {
            return Optional.of(StudentUpdateRequest.Grade.valueOf(value.toUpperCase())).orElseThrow(() -> new IllegalArgumentException("有効な学年を指定してください（一年生, 二年生, 三年生,卒業生のいずれか）。"));
        }
    }

    /**
     * 学年のバリデーション用アノテーション
     */
    @Documented
    @Constraint(validatedBy = StudentUpdateRequest.GradeValidator.class)
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidGrade {
        String message() default "有効な学年を指定してください（一年生, 二年生, 三年生,卒業生のいずれか）。";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * 学年のバリデータ
     */
    public static class GradeValidator implements ConstraintValidator<StudentUpdateRequest.ValidGrade, String> {

        @Override
        public void initialize(StudentUpdateRequest.ValidGrade constraintAnnotation) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // nullの場合はバリデーションしない
            }

            try {
                StudentUpdateRequest.Grade.from(value);
                return true;
            } catch (IllegalArgumentException e) { // 有効な学年でない場合はバリデーションエラー
                return false;
            }
        }
    }
}

