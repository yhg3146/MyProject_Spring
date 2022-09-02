package com.example.schoolmanagement.comp.paperUser.user.service;

import com.example.schoolmanagement.comp.paperUser.user.domain.School;
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
public class SchoolTest extends WithUserTest {



    @BeforeEach
    void before() {
        prepareTest();
    }



    @DisplayName("1.학교 검색 ")
    @Test
    void test_1() {
        List<School> list = schoolRepository.findAll();
        assertEquals(1,list.size());
        schoolServiceHelper.assertSchool(list.get(0),"테스트 학교","서울");
    }

    @DisplayName("2.학교 이름 수정 ")
    @Test
    void test_2() {

        schoolService.updateName(school.getSchoolId(),"테스트 학교2");
        assertEquals("테스트 학교2",schoolRepository.findById(school.getSchoolId()).get().getSchoolName());

    }


    @DisplayName("3.지역목록을 불러오기 ")
    @Test
    void test_3() {
        schoolServiceHelper.createSchool("테스트 학교3","부산");
        List<String> cityList = schoolService.cities();
        assertEquals(2,cityList.size());
    }


    @DisplayName("4.지역 목록으로 학교 불러오기 ")
    @Test
    void test_4() {
        schoolServiceHelper.createSchool("테스트 학교4","서울");
        schoolServiceHelper.createSchool("테스트 학교4","부산");
        List<School> schoolList = schoolService.findAllByCity("부산");
        assertEquals(1,schoolList.size());
    }


}
