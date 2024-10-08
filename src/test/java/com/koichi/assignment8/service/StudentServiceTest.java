package com.koichi.assignment8.service;

import com.koichi.assignment8.entity.Student;
import com.koichi.assignment8.excption.MultipleMethodsException;
import com.koichi.assignment8.excption.StudentNotFoundException;
import com.koichi.assignment8.mapper.StudentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    StudentService studentService;

    @Mock
    StudentMapper studentMapper;

    @Test
    public void IDに該当する学生が一件取得できること() {
        doReturn(Optional.of(new Student(1, "溝口光一", "一年生", "大分県"))).when(studentMapper).findById(1);
        Student actual = studentService.findStudent(1);
        assertThat(actual).isEqualTo(new Student(1, "溝口光一", "一年生", "大分県"));
    }

    @Test
    public void IDに該当する学生がいない時にstudentnotfoundというメッセージが返却されること() {
        doReturn(Optional.empty()).when(studentMapper).findById(1);
        assertThatThrownBy(() -> studentService.findStudent(1))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("student not found");
    }

    @Test
    public void 全ての学生を取得すること() {
        List<Student> findAllStudents = List.of(new Student(1, "溝口光一", "一年生", "大分県"),
                new Student(2, "中野乃蒼", "二年生", "福岡県"),
                new Student(3, "安藤健", "三年生", "熊本県"));
        doReturn(findAllStudents).when(studentMapper).findAllStudents();
        List<Student> actualList = studentService.findStudents(null, null, null);
        assertThat(actualList).isEqualTo(findAllStudents);
    }

    @Test
    public void 一年生の学生をクエリパラメータの検索を使用して取得すること() {
        List<Student> getByGrade = List.of(new Student(1, "溝口光一", "一年生", "大分県"));
        doReturn(getByGrade).when(studentMapper).findByGrade("一年生");
        List<Student> actualList = studentService.findStudents(1, null, null);
        assertThat(actualList).isEqualTo(getByGrade);
    }

    @Test
    public void 人名の頭文字が溝である学生をクエリパラメータの検索を使用して複数取得すること() {
        List<Student> getByStartWith = studentMapper.findByStartWith("溝");
        doReturn(getByStartWith).when(studentMapper).findByStartWith("溝");
        List<Student> actualList = studentService.findStudents(null, "溝", null);
        assertThat(actualList).isEqualTo(getByStartWith);
    }

    @Test
    public void 大分県出身の学生をクエリパラメータの検索を使用して取得すること() {
        List<Student> getByBirthPlace = List.of(new Student(1, "溝口光一", "一年生", "大分県"));
        doReturn(getByBirthPlace).when(studentMapper).findByBirthPlace("大分県");
        List<Student> actualList = studentService.findStudents(null, null, "大分県");
        assertThat(actualList).isEqualTo(getByBirthPlace);
    }

    @Test
    public void クエリパラメータで複数のカラムを検索する時にカラムはgradestartsWithbirthPlaceの一つを選んでくださいというメッセージを返却すること() {
        List<Student> findAllStudents = List.of(new Student(1, "溝口光一", "一年生", "大分県"));
        doReturn(findAllStudents).when(studentMapper).findAllStudents();
        assertThatThrownBy(() -> studentService.findStudents(null, "溝", "大分県"))
                .isInstanceOf(MultipleMethodsException.class)
                .hasMessage("カラムはgrade・startsWith・birthPlaceの一つを選んでください");
    }

    @Test
    public void 新しい学生を登録すること() {
        Student newStudent = new Student("溝口光一", "一年生", "大分県");
        studentService.insertStudent("溝口光一", "一年生", "大分県");
        verify(studentMapper, times(1)).insertStudent(newStudent);
    }

    @Test
    public void IDに該当する学生のデータを更新出来ること() {

        String name = "溝上航";
        String grade = "一年生";
        String birthPlace = "大分県";

        Student expectedStudents = new Student(1, "内藤友美", "一年生", "福岡県");
        doReturn(Optional.of(expectedStudents)).when(studentMapper).findById(1);
        studentService.updateStudent(1, name, grade, birthPlace);

        assertThat(expectedStudents.getName()).isEqualTo(name);
        assertThat(expectedStudents.getGrade()).isEqualTo(grade);
        assertThat(expectedStudents.getBirthPlace()).isEqualTo(birthPlace);
        verify(studentMapper, times(1)).updateStudent(expectedStudents);
    }

    @Test
    public void 学生のデータを更新する際にIDに該当する学生がいない場合studentnotfoundというメッセージが返却されること() {

        String name = "溝上航";
        String grade = "一年生";
        String birthPlace = "大分県";

        doReturn(Optional.empty()).when(studentMapper).findById(999);
        assertThatThrownBy(() -> studentService.updateStudent(999, "溝上航", "一年生", "福岡県"))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("student not found");
    }

    @Test
    public void 学生の学年を進級させること() {

        studentService.updateGrade();
        verify(studentMapper, times(1)).updateGrade("卒業生", "三年生");
        verify(studentMapper, times(1)).updateGrade("三年生", "二年生");
        verify(studentMapper, times(1)).updateGrade("二年生", "一年生");
    }

    @Test
    public void IDに該当する学生のデータを削除出来ること() {

        Student expectedStudents = new Student(1, "内藤友美", "一年生", "福岡県");
        doReturn(Optional.of(expectedStudents)).when(studentMapper).findById(1);
        studentService.deleteStudent(1);
        verify(studentMapper, times(1)).deleteStudent(1);
    }

    @Test
    public void 学生のデータを削除する際にIDに該当する学生がいない場合studentnotfoundというメッセージが返却されること() {

        doReturn(Optional.empty()).when(studentMapper).findById(999);
        assertThatThrownBy(() -> studentService.deleteStudent(999))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("student not found");

    }
}
