package com.koichi.assignment8.controller;

import com.koichi.assignment8.controller.request.StudentPostRequest;
import com.koichi.assignment8.controller.request.StudentUpdateRequest;
import com.koichi.assignment8.controller.response.StudentResponse;
import com.koichi.assignment8.entity.Student;
import com.koichi.assignment8.excption.MethodArgumentTypeMismatchException;
import com.koichi.assignment8.excption.StudentControllerAdvice;
import com.koichi.assignment8.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = StudentControllerAdvice.ErrorResponse.class)))
@RestController
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * SELECT用のController
     * 指定したidのstudentのデータを全て取得します。
     */
    @Operation(summary = "学生取得API",
            description = "指定したidのstudentのデータを全て取得します"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Student.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentControllerAdvice.ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "student not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentControllerAdvice.ErrorResponse.class))),
            }
    )
    @GetMapping("/students/{id}")
    public Student findById(@PathVariable("id") String id) {
        int intTypeConvertedId;
        try {
            intTypeConvertedId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new MethodArgumentTypeMismatchException("IDは数字で入力してください");
        }
        return studentService.findStudent(intTypeConvertedId);
    }

    /**
     * SELECT用のController
     * 指定した検索パラメータに一致するstudentのデータを取得します。
     */
    @Operation(summary = "詳細検索API",
            description = "このエンドポイントでは全学生の情報を取得できます。また、特定のカラムを指定して学生を検索することも可能です。ただし、検索に使用できるカラムは一度に1つのみです。"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Student.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentControllerAdvice.ErrorResponse.class)))
            }
    )
    @GetMapping("/students")
    public List<Student> getStudents(@RequestParam(required = false) String grade, @RequestParam(required = false) String startsWith, @RequestParam(required = false) String birthPlace) {
        Integer integerTypeConvertedId = null;
        if (Objects.nonNull(grade)) {
            try {
                integerTypeConvertedId = Integer.valueOf(grade);
            } catch (NumberFormatException e) {
                throw new MethodArgumentTypeMismatchException("学年は半角数字で入力してください");
            }
        }
        return studentService.findStudents(integerTypeConvertedId, startsWith, birthPlace);
    }

    /**
     * INSERT用のController
     */
    @Operation(summary = "学生追加API",
            description = "名前、学年、出身地を入力してリクエストすると、新しい学生を追加できます。"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(oneOf = {
                                            StudentControllerAdvice.ErrorResponse.class,
                                            StudentControllerAdvice.ValidationErrorResponse.class
                                    }),
                                    examples = {
                                            @ExampleObject(
                                                    name = "ErrorResponse",
                                                    value = """
                                                            {
                                                                "timestamp": "string" ,
                                                                "status": "string",
                                                                "error": "string",
                                                                "message": "string",
                                                                "path": "string"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "ValidationErrorResponse",
                                                    value = """
                                                            {
                                                                "status": "string",
                                                                "message": "string",
                                                                "timestamp": "string",
                                                                "errors": [
                                                                    {
                                                                        "field": "string",
                                                                        "message": "string"
                                                                    }
                                                                ]
                                                            }
                                                            """
                                            )
                                    }
                            )
                    )
            }
    )
    @PostMapping("/students")
    public ResponseEntity<StudentResponse> insertStudent(@RequestBody @Validated StudentPostRequest studentPostRequest, UriComponentsBuilder uriBuilder) {
        Student student = studentService.insertStudent(studentPostRequest.getName(), studentPostRequest.getGrade(), studentPostRequest.getBirthPlace());
        URI location = uriBuilder.path("/students/{id}").buildAndExpand(student.getId()).toUri();
        StudentResponse body = new StudentResponse("student created");
        return ResponseEntity.created(location).body(body);
    }

    /**
     * PATCH用のController
     * 指定したidのstudentの name,grade,birthplaceを更新します。
     */
    @Operation(summary = "学生更新API",
            description = "指定したidの生徒の情報を更新できます。"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(oneOf = {
                                            StudentControllerAdvice.ErrorResponse.class,
                                            StudentControllerAdvice.ValidationErrorResponse.class
                                    }),
                                    examples = {
                                            @ExampleObject(
                                                    name = "ErrorResponse",
                                                    value = """
                                                            {
                                                                "timestamp": "string" ,
                                                                "status": "string",
                                                                "error": "string",
                                                                "message": "string",
                                                                "path": "string"
                                                            }
                                                            """
                                            ),
                                            @ExampleObject(
                                                    name = "ValidationErrorResponse",
                                                    value = """
                                                            {
                                                                "status": "string",
                                                                "message": "string",
                                                                "timestamp": "string",
                                                                "errors": [
                                                                    {
                                                                        "field": "string",
                                                                        "message": "string"
                                                                    }
                                                                ]
                                                            }
                                                            """
                                            )
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "student not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentControllerAdvice.ErrorResponse.class)))
            }
    )
    @PatchMapping("/students/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable("id") String id, @RequestBody @Validated StudentUpdateRequest studentUpdateRequest) {
        int intTypeConvertedId;
        try {
            intTypeConvertedId = Integer.parseInt(id);
            studentService.updateStudent(intTypeConvertedId, studentUpdateRequest.getName(), studentUpdateRequest.getGrade(), studentUpdateRequest.getBirthPlace());
        } catch (NumberFormatException e) {
            throw new MethodArgumentTypeMismatchException("IDは数字で入力してください");
        }
        StudentResponse body = new StudentResponse("Student updated");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    /**
     * PATCH用のController
     * 指定したgradeを進級します。
     */
    @Operation(summary = "全学年更新API",
            description = "全生徒の学年を更新できます。"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Ok",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StudentResponse.class)))
    @PatchMapping("/students/grade/_batchUpdate")
    public ResponseEntity<StudentResponse> updateGrade() {
        studentService.updateGrade();
        StudentResponse body = new StudentResponse("Grade updated");
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    /**
     * DELETE用のController
     * 指定したidのstudentのデータを削除します。
     */
    @Operation(summary = "学生削除API",
            description = "指定したidの学生を削除出来ます。"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Ok",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentResponse.class))),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentControllerAdvice.ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "student not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = StudentControllerAdvice.ErrorResponse.class))),
            }
    )
    @DeleteMapping("/students/{id}")
    public ResponseEntity<StudentResponse> deleteStudent(@PathVariable("id") String id) {
        int intTypeConvertedId;
        try {
            intTypeConvertedId = Integer.parseInt(id);
            studentService.deleteStudent(intTypeConvertedId);
        } catch (NumberFormatException e) {
            throw new MethodArgumentTypeMismatchException("IDは数字で入力してください");
        }
        StudentResponse body = new StudentResponse("Student deleted");
        return ResponseEntity.ok(body);
    }
}
