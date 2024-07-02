package com.koichi.assignment8.mapper;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.koichi.assignment8.entity.Student;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentMapperTest {

    @Autowired
    StudentMapper studentMapper;

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    public void IDに該当する学生が一件取得できること() {

        Optional<Student> findById = studentMapper.findById(1);
        assertThat(findById).contains(
                new Student(1, "清⽔圭吾", "一年生", "大分県")
        );
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    public void IDに該当する学生がいない場合空のOptionalを返すこと() {

        Optional<Student> findById = studentMapper.findById(999);
        assertThat(findById).isEmpty();
    }

    @Test
    @Transactional
    public void 全ての学生を取得すること() {

        List<Student> findAllStudents = studentMapper.findAllStudents();
        assertThat(findAllStudents).contains(
                new Student(1, "清⽔圭吾", "一年生", "大分県"),
                new Student(2, "田中圭", "一年生", "福岡県"),
                new Student(3, "岡崎徹", "二年生", "大分県"),
                new Student(4, "溝口光一", "二年生", "熊本県"),
                new Student(5, "溝谷望", "三年生", "熊本県"),
                new Student(6, "安藤孝弘", "三年生", "福岡県")
        );
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    public void 一年生の学生をクエリパラメータの検索を使用して取得すること() {

        List<Student> findByGrade = studentMapper.findByGrade("一年生");
        assertThat(findByGrade).contains(
                new Student(1, "清⽔圭吾", "一年生", "大分県"),
                new Student(2, "田中圭", "一年生", "福岡県")
        );
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    public void 人名の頭文字が溝である学生をクエリパラメータの検索を使用して複数取得すること() {

        List<Student> getByStartWith = studentMapper.findByStartWith("溝");
        assertThat(getByStartWith).contains(
                new Student(4, "溝口光一", "二年生", "熊本県"),
                new Student(5, "溝谷望", "三年生", "熊本県")
        );
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    public void 大分県出身の学生をクエリパラメータの検索を使用して取得すること() {

        List<Student> findByBirthPlace = studentMapper.findByBirthPlace("大分県");
        assertThat(findByBirthPlace).contains(
                new Student(1, "清⽔圭吾", "一年生", "大分県"),
                new Student(3, "岡崎徹", "二年生", "大分県")
        );
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/studentsToRegister.yml", ignoreCols = "id")
    @Transactional
    public void 新しい学生を登録すること() {

        Student insertStudent = new Student("中田健太", "一年生", "福岡県");
        studentMapper.insertStudent(insertStudent);
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/studentsToRenewing.yml")
    @Transactional
    public void IDに該当する学生のデータを更新出来ること() {

        Student renewingStudent = new Student(1, "城野健一", "二年生", "福岡県");
        studentMapper.updateStudent(renewingStudent);
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/gradeAdvancement.yml")
    @Transactional
    public void 学生の学年を進級させること() {

        studentMapper.updateGrade("卒業生", "三年生");
        studentMapper.updateGrade("三年生", "二年生");
        studentMapper.updateGrade("二年生", "一年生");
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/studentsToRemoved.yml")
    @Transactional
    public void IDに該当する学生のデータを削除出来ること() {

        studentMapper.deleteStudent(1);
    }
}
