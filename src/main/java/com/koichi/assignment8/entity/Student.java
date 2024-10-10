package com.koichi.assignment8.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Schema(description = "学生情報")
public class Student {
    @Schema(description = "学生ID")
    private Integer id;

    @Schema(description = "学生の名前")
    @NotBlank(message = "nameを入力してください")
    private String name;

    @Schema(description = "学年")
    private String grade;

    @Schema(description = "出身地")
    @NotBlank(message = "birthPlaceを入力してください")
    private String birthPlace;


    public Student(Integer id, String name, String grade, String birthPlace) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.birthPlace = birthPlace;
    }

    public Student(String name, String grade, String birthPlace) {
        this.id = null;
        this.name = name;
        this.grade = grade;
        this.birthPlace = birthPlace;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(name, student.name) && Objects.equals(grade, student.grade) && Objects.equals(birthPlace, student.birthPlace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, grade, birthPlace);
    }
}
