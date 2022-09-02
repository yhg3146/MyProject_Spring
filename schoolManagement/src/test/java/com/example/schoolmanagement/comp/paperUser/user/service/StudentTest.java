package com.example.schoolmanagement.comp.paperUser.user.service;

import com.example.schoolmanagement.comp.paperUser.user.domain.School;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.service.helper.WithUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace =AutoConfigureTestDatabase.Replace.NONE)
public class StudentTest extends WithUserTest {


    @BeforeEach
    void before(){
        prepareTest();
    }


    @DisplayName("1.학생 생성 ")
    @Test
    void test_1() {
        assertEquals(2,userService.findAllByStudentAuthority().size());

    }


    @DisplayName("2.학교에 있는 학생들 조회 ")
    @Test
    void test_2() {
        School school1 =schoolServiceHelper.createSchool("school1","부산");
        List<User> studentList = userService.findStudentListBySchoolId(school.getSchoolId());
        assertEquals(2,studentList.size());

        studentList = userService.findStudentListBySchoolId(school1.getSchoolId());
        assertEquals(0,studentList.size());
    }


    @DisplayName("3.담당 선생님의 학생들 조회 ")
    @Test
    void test_3() {
        List<User> studentList = userService.findStudentListByTeacherUserId(teacher.getUserId());
        assertEquals(2,studentList.size());

        User teacher1 = userServiceHelper.createTeacher(school,"teacher2");
        studentList = userService.findStudentListByTeacherUserId(teacher1.getUserId());
        assertEquals(0,studentList.size());
    }


}
