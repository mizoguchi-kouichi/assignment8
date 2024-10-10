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
 * INSERTの際に使用するRequestです。
 */
@Schema(description = "学生登録リクエスト")
public class StudentPostRequest {
    @Schema(description = "学生の名前", example = "山田太郎", required = true)
    @NotBlank(message = "nameを入力してください")
    private String name;

    @Schema(description = "学年", example = "一年生", required = true)
    @ValidGrade
    private String grade;

    @Schema(description = "出身地", example = "東京", required = true)
    @NotBlank(message = "birthPlaceを入力してください")
    private String birthPlace;

    public StudentPostRequest(String name, String grade, String birthPlace) {
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

    /**
     * 学年のEnum
     */
    public enum Grade {
        一年生,
        二年生,
        三年生;

        public static Grade from(String value) {
            return Optional.of(Grade.valueOf(value.toUpperCase())).orElseThrow(() -> new IllegalArgumentException("有効な学年を指定してください（一年生, 二年生, 三年生のいずれか）。"));
        }
    }

    /**
     * 学年のバリデーション用アノテーション
     */
    @Documented
    @Constraint(validatedBy = GradeValidator.class)
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidGrade {
        String message() default "有効な学年を指定してください（一年生, 二年生, 三年生のいずれか）。";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    /**
     * 学年のバリデータ
     */
    public static class GradeValidator implements ConstraintValidator<ValidGrade, String> {

        @Override
        public void initialize(ValidGrade constraintAnnotation) {
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (value == null) {
                return true; // nullの場合はバリデーションしない
            }

            try {
                Grade.from(value);
                return true;
            } catch (IllegalArgumentException e) { // 有効な学年でない場合はバリデーションエラー
                return false;
            }
        }
    }
}
