package com.koichi.assignment8.itegrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class studentApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void IDに該当する学生が一件取得できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "id": 1,
                            "name": "清⽔圭吾",
                            "grade": "一年生",
                            "birthPlace": "大分県"
                        }                                                          
                        """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void IDに該当する学生がいない時にStudentNotFoundExceptionのレスポンスボティが返却されること() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);
            mockMvc.perform(MockMvcRequestBuilders.get("/students/999"))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "error": "Not Found",
                                "path": "/students/999",
                                "status": "404",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "message": "student not found"
                            }                                                      
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void 全ての学生を取得すること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "name": "清⽔圭吾",
                                "grade": "一年生",
                                "birthPlace": "大分県"
                            },
                            {
                                "id": 2,
                                "name": "田中圭",
                                "grade": "一年生",
                                "birthPlace": "福岡県"
                            },
                            {
                                "id": 3,
                                "name": "岡崎徹",
                                "grade": "二年生",
                                "birthPlace": "大分県"
                            },
                            {
                                "id": 4,
                                "name": "溝口光一",
                                "grade": "二年生",
                                "birthPlace": "熊本県"
                            },
                            {
                                "id": 5,
                                "name": "溝谷望",
                                "grade": "三年生",
                                "birthPlace": "熊本県"
                            },
                            {
                                "id": 6,
                                "name": "安藤孝弘",
                                "grade": "三年生",
                                "birthPlace": "福岡県"
                            }
                        ]
                         """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void 一年生の学生をクエリパラメータの検索を使用して取得すること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students?grade=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "name": "清⽔圭吾",
                                "grade": "一年生",
                                "birthPlace": "大分県"
                            },
                            {
                                "id": 2,
                                "name": "田中圭",
                                "grade": "一年生",
                                "birthPlace": "福岡県"
                            }
                        ]
                         """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void 人名の頭文字が溝である学生をクエリパラメータの検索を使用して複数取得すること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students?startsWith=溝"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 4,
                                "name": "溝口光一",
                                "grade": "二年生",
                                "birthPlace": "熊本県"
                            },
                            {
                                "id": 5,
                                "name": "溝谷望",
                                "grade": "三年生",
                                "birthPlace": "熊本県"
                            }
                        ]
                         """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void 大分県出身の学生をクエリパラメータの検索を使用して取得すること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students?birthPlace=大分県"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "name": "清⽔圭吾",
                                "grade": "一年生",
                                "birthPlace": "大分県"
                            },
                            {
                                "id": 3,
                                "name": "岡崎徹",
                                "grade": "二年生",
                                "birthPlace": "大分県"
                            }
                        ]
                         """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void 実際にないカラムでクエリパラメータの検索を使用して取得する際に全ての学生を取得すること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students?gender=男性"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        [
                            {
                                "id": 1,
                                "name": "清⽔圭吾",
                                "grade": "一年生",
                                "birthPlace": "大分県"
                            },
                            {
                                "id": 2,
                                "name": "田中圭",
                                "grade": "一年生",
                                "birthPlace": "福岡県"
                            },
                            {
                                "id": 3,
                                "name": "岡崎徹",
                                "grade": "二年生",
                                "birthPlace": "大分県"
                            },
                            {
                                "id": 4,
                                "name": "溝口光一",
                                "grade": "二年生",
                                "birthPlace": "熊本県"
                            },
                            {
                                "id": 5,
                                "name": "溝谷望",
                                "grade": "三年生",
                                "birthPlace": "熊本県"
                            },
                            {
                                "id": 6,
                                "name": "安藤孝弘",
                                "grade": "三年生",
                                "birthPlace": "福岡県"
                            }
                        ]
                         """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void クエリパラメータの検索を使用してカラムを2つ以上を選んだ時にMultipleMethodsExceptionのレスポンスボティが返却されること() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);
            mockMvc.perform(MockMvcRequestBuilders.get("/students?grade=1&birthPlace=大分県"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "message": "カラムはgrade・startsWith・birthPlaceの一つを選んでください",
                                "status": "400",
                                "path": "/students",
                                "error": "Bad Request",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］"
                            }
                             """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void 学年でクエリパラメータの検索を使用する際に文字列を入力した時にhandleMethodArgumentTypeMismatchExceptionのレスポンスボティが返却されること() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);
            mockMvc.perform(MockMvcRequestBuilders.get("/students?grade=一年生"))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "path": "/students",
                                "status": "400",
                                "message": "IDまたは学年を入力する際は、半角の数字で入力してください",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "error": "Bad Request"
                            }
                             """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void 実際にいない人名の頭文字でクエリパラメータの検索を使用したらEmptyを返すこと() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students?startsWith=阿"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        []
                         """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @Transactional
    void 実際にいない出身地でクエリパラメータの検索を使用したらEmptyを返すこと() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students?birthPlace=大阪府"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        []
                         """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/studentsToRegister.yml", ignoreCols = "id")
    @Transactional
    void 新しい学生を登録すること() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"中田健太",
                                    "grade":"一年生",
                                    "birthPlace":"福岡県"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                            "message": "student created"
                         }
                        """));

    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 新しい学生を登録する際に名前がない時にValidationErrorのレスポンスボティを返すこと() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.post("/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"",
                                        "grade":"一年生",
                                        "birthPlace":"福岡県"
                                    } 
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "validation error",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "errors": [
                                     {
                                         "field": "name",
                                         "message": "nameを入力してください"
                                     }
                                ]
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 新しい学生を登録する際に学年がない時にValidationErrorのレスポンスボティを返すこと() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.post("/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"中田健太",
                                        "grade":"",
                                        "birthPlace":"福岡県"
                                    }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "validation error",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "errors": [
                                    {
                                        "field": "grade",
                                        "message": "有効な学年を指定してください（一年生, 二年生, 三年生のいずれか）。"
                                    }
                                ]
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 新しい学生を登録する際に学年が関係ない文字の時にValidationErrorのレスポンスボティを返すこと() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.post("/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                         "name":"中田健太",
                                         "grade":"1",
                                         "birthPlace":"福岡県"
                                     }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "validation error",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "errors": [
                                    {
                                        "field": "grade",
                                        "message": "有効な学年を指定してください（一年生, 二年生, 三年生のいずれか）。"
                                    }
                                ]
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 新しい学生を登録する際に出身地がない時にValidationErrorのレスポンスボティを返すこと() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.post("/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"中田健太",
                                        "grade":"一年生",
                                        "birthPlace":""
                                    }                                    
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "validation error",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "errors": [
                                    {
                                        "field": "birthPlace",
                                        "message": "birthPlaceを入力してください"
                                    }
                                ]
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 新しい学生を登録する際に全てのカラムがない時にValidationErrorのレスポンスボティを返すこと() throws Exception {


        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.post("/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"",
                                        "grade":"",
                                        "birthPlace":""
                                    }                                    
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "validation error",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "errors": [
                                    {
                                        "field": "name",
                                        "message": "nameを入力してください"
                                    },
                                    {
                                        "field": "grade",
                                        "message": "有効な学年を指定してください（一年生, 二年生, 三年生のいずれか）。"
                                    },
                                    {
                                        "field": "birthPlace",
                                        "message": "birthPlaceを入力してください"
                                    }
                                ]
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/studentsToRenewing.yml", ignoreCols = "id")
    @Transactional
    void IDに該当する学生のデータを更新出来ること() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name":"城野健一",
                                    "grade":"二年生",
                                    "birthPlace":"福岡県"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("""
                        {
                             "message": "Student updated"
                        }
                        """));
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 学生のデータを更新する際に該当するIDの学生がいない場合StudentNotFoundExceptionのレスポンスボティが返却されること() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.patch("/students/0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"城野健一",
                                        "grade":"二年生",
                                        "birthPlace":"福岡県"
                                    }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isNotFound())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "path": "/students/0",
                                "status": "404",
                                "message": "student not found",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "error": "Not Found"
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 学生のデータを更新する際にリクエストされたIDが文字列の場合handleMethodArgumentTypeMismatchExceptionのレスポンスボティが返却されること() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.patch("/students/あ")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"城野健一",
                                        "grade":"二年生",
                                        "birthPlace":"福岡県"
                                    }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "IDまたは学年は半角の数字で入力してください",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "error": "Bad Request",
                                "path": "/students/%E3%81%82"
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 学生のデータを更新する際にリクエストされたIDが空白の場合handleMissingPathVariableExceptionのレスポンスボティが返却されること() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.patch("/students/ ")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"城野健一",
                                        "grade":"二年生",
                                        "birthPlace":"福岡県"
                                    }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "error": "Bad Request",
                                "path": "/students/%20",
                                "status": "400",
                                "message": "学生のIDを入力してください"
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 学生のデータを更新する際に全学生をリクエストされたの場合handleHttpRequestMethodNotSupportedExceptionのレスポンスボティが返却されること() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.patch("/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"城野健一",
                                        "grade":"二年生",
                                        "birthPlace":"福岡県"
                                    }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "学生のIDを入力してください",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "error": "Bad Request",
                                "path": "/students"
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 学生のデータを更新する際に名前がない場合ValidationErrorのレスポンスボティを返すこと() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.patch("/students/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"",
                                        "grade":"二年生",
                                        "birthPlace":"福岡県"
                                    }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "validation error",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "errors": [
                                    {
                                        "field": "name",
                                        "message": "nameを入力してください"
                                    }
                                ]
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 学生のデータを更新する際に学年がない時にValidationErrorのレスポンスボティを返すこと() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.patch("/students/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"城野健一",
                                        "grade":"",
                                        "birthPlace":"福岡県"
                                    }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "validation error",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "errors": [
                                    {
                                        "field": "grade",
                                        "message": "有効な学年を指定してください（一年生, 二年生, 三年生,卒業生のいずれか）。"
                                    }
                                ]
                            }
                            """));
        }
    }

    @Test
    @DataSet(value = "datasets/students.yml")
    @ExpectedDataSet(value = "datasets/students.yml")
    @Transactional
    void 学生のデータを更新する際に学年が関係ない文字の時にValidationErrorのレスポンスボティを返すこと() throws Exception {

        final ZonedDateTime fixedClock = ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Tokyo"));

        try (MockedStatic<ZonedDateTime> mockClock = Mockito.mockStatic(ZonedDateTime.class)) {
            mockClock.when(ZonedDateTime::now).thenReturn(fixedClock);

            mockMvc.perform(MockMvcRequestBuilders.patch("/students/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "name":"城野健一",
                                        "grade":"1",
                                        "birthPlace":"福岡県"
                                    }
                                    """))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest())
                    .andExpect(MockMvcResultMatchers.content().json("""
                            {
                                "status": "400",
                                "message": "validation error",
                                "timestamp": "2024/01/01 T00:00:00+0900［Asia/Tokyo］",
                                "errors": [
                                    {
                                        "field": "grade",
                                        "message": "有効な学年を指定してください（一年生, 二年生, 三年生,卒業生のいずれか）。"
                                    }
                                ]
                            }
                            """));
        }
    }

}
