package com.example.schoolmanagement.comp.paperUser.user.service;

import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.service.helper.WithUserTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static com.example.schoolmanagement.comp.paperUser.user.service.helper.UserServiceHelper.assertUser;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTest extends WithUserTest {


    @BeforeEach
    void before(){
        prepareTest();
    }


    @DisplayName("1.유저 생성 ")
    @Test
    void test_1() {
        assertUser(school,user,"user1");
        List<User> userList = userRepository.findAll();
        assertEquals(1,userList.size());
    }


    @DisplayName("2.유저 이름 변경 ")
    @Test
    void test_2() {
        userService.updateUserName(user.getUserId(),"user2");
        assertEquals("user2",userRepository.findById(user.getUserId()).get().getName());


    }


    @DisplayName("3.권한 부여 ")
    @Test
    void test_3() {
        User user = userServiceHelper.createUser(school,"user2", Authority.ROLE_STUDENT);
        userService.addAuthority(user.getUserId(),Authority.ROLE_TEACHER);
        assertUser(school,user,"user2",Authority.ROLE_STUDENT,Authority.ROLE_TEACHER);

        User user1 = userServiceHelper.createUser(school,"user3",Authority.ROLE_STUDENT);
        assertUser(school,user1,"user3",Authority.ROLE_STUDENT);

    }


    @DisplayName("4.권한 삭제 ")
    @Test
    void test_4() {
        User user = userServiceHelper.createUser(school,"user1",Authority.ROLE_STUDENT);
        userService.removeAuthority(user.getUserId(),Authority.ROLE_STUDENT);
        assertUser(school,user,"user1");
        assertTrue(user.getAuthorities().isEmpty());
    }


    @DisplayName("5.email 검색 ")
    @Test
    void test_5() {
        User saved = (User) userDetailService.loadUserByUsername("user1@test.com");
        assertUser(school,saved,"user1");
    }


    @DisplayName("6.role이 중복해서 들어가지는가? ")
    @Test
    void test_6() {
        userService.addAuthority(user.getUserId(),Authority.ROLE_STUDENT);
        userService.addAuthority(user.getUserId(),Authority.ROLE_STUDENT);
        userService.addAuthority(user.getUserId(),Authority.ROLE_TEACHER);
        assertEquals(2,userRepository.findById(user.getUserId()).get().getAuthorities().size());
        assertUser(school,user,"user1",Authority.ROLE_STUDENT,Authority.ROLE_TEACHER);

    }


    @DisplayName("7.이메일이 중복해서 들어가는가? ")
    @Test
    void test_7() {
        assertThrows(DataIntegrityViolationException.class,()->userServiceHelper.createUser(school,"user1"));
    }


}
