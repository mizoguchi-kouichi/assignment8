package com.koichi.assignment8;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StudentController {

    private final StudentMapper studentMapper;

    public StudentController(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    @GetMapping("/students1")
    public List<Student>findAll(){
        return  studentMapper.findAll1();
    }

    @GetMapping("/students2")
    public List<Student> findAll2() {
        return studentMapper.findAll2();
    }

}
