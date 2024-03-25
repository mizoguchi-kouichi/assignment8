package com.koichi.assignment8;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students/{id}")
    public Student findById(@PathVariable("id") Integer id) {
        return studentService.findStudent(id);
    }

    @GetMapping("/students")
    public Optional<List<Student>> getAllStudents(@RequestParam(required = false) Integer grade, String startsWith, String birthPlace) {
        if (grade != null) {
            return studentService.GetByGrade(grade);
        } else if (startsWith != null) {
            return studentService.getByStartsWith(startsWith);
        } else if (birthPlace != null) {
            return studentService.getByBirthPlace(birthPlace);
        }
        return studentService.getAllStudent();
    }
}


