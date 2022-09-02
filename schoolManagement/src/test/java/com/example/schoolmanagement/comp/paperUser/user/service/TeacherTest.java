package com.example.schoolmanagement.comp.paperUser.user.service;

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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TeacherTest extends WithUserTest {

    @BeforeEach
    void before(){
        prepareTest();
    }


    @DisplayName("1.선생님 생성 ")
    @Test
    void test_1() {
        userServiceHelper.assertTeacher(school,teacher,"teacher1");
    }


    @DisplayName("2.학교에서 선생님 조회 ")
    @Test
    void test_2() {
        userServiceHelper.createTeacher(school,"teacher2");
        List<User> teacherList = userService.findTeacherListBySchoolId(school.getSchoolId());
        System.out.println(teacherList);
        assertEquals(2,teacherList.size());
    }


    @DisplayName("3.선생님으로 등록한 학생을 조회 ")
    @Test
    void test_3() {
        List<User> studentList = userService.findStudentListByTeacherUserId(teacher.getUserId());
        studentList.stream().forEach(student -> System.out.println(student));
        assertEquals(2,studentList.size());
    }

    @DisplayName("4.모든 선생님 목록을 조회한다. ")
    @Test
    void test_4() {
        List<User> teacherList = userService.findAllByTeacherAuthority();
        assertEquals(1,teacherList.size());

    }


}
