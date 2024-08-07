package com.koichi.assignment8.mapper;

import com.koichi.assignment8.entity.Student;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface StudentMapper {

    /**
     * SELECT用のMapper
     * 指定したidのstudentのデータを全て取得します。
     */
    @Select("SELECT * FROM students WHERE id = #{id} ")
    Optional<Student> findById(Integer id);

    /**
     * SELECT用のMapper
     * 全てのstudentのデータを全て取得します。
     */
    @Select("SELECT * FROM students")
    List<Student> findAllStudents();

    /**
     * SELECT用のMapper
     * 指定した学年のstudentのデータを全て取得します。
     */
    @Select("SELECT *  from students WHERE grade = #{grade} ")
    List<Student> findByGrade(String grade);

    /**
     * SELECT用のMapper
     * 指定した接頭辞のstudentのデータを全て取得します。
     */
    @Select("SELECT * FROM students WHERE name LIKE CONCAT(#{startsWith}, '%') ")
    List<Student> findByStartWith(String startsWith);

    /**
     * SELECT用のMapper
     * 指定した出身地のstudentのデータを全て取得します。
     */
    @Select("SELECT * FROM students WHERE birth_place = #{birthPlace}")
    List<Student> findByBirthPlace(String birthPlace);

    /**
     * INSERT用のMapper
     */
    @Insert("INSERT INTO students (name,grade,birth_place) VALUES (#{name}, #{grade},#{birthPlace})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertStudent(Student student);

    /**
     * PATCH用のMapper
     * 指定したidのstudentの name,grade,birthplaceを更新します。
     */
    @Update("UPDATE students SET name = #{name}, grade = #{grade},birth_Place = #{birthPlace} WHERE id =#{id}")
    void updateStudent(Student student);

    /**
     * PATCH用のMapper
     * 指定したgradeを進級します。
     */
    @Update("UPDATE students SET grade = #{newGrade} WHERE grade =#{grade} ")
    void updateGrade(String newGrade, String grade);

    /**
     * DELETE用のMapper
     * 指定したidのstudentのデータを削除します。
     */
    @Delete(" DELETE FROM students WHERE id =#{id}")
    void deleteStudent(Integer id);
}
