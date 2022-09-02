package com.example.schoolmanagement.comp.paperUser.user.service.helper;

import com.example.schoolmanagement.comp.paperUser.user.domain.School;
import com.example.schoolmanagement.comp.paperUser.user.service.SchoolService;
import lombok.RequiredArgsConstructor;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RequiredArgsConstructor
public class SchoolServiceHelper {

    private final SchoolService schoolService;

    public static School makeSchool(String schoolName, String city){
        return School.builder()
                .schoolName(schoolName)
                .city(city)
                .build();
    }

    public School createSchool(String schoolName,String city){
        School school = makeSchool(schoolName,city);
         return schoolService.save(school);

    }



    public void assertSchool(School school,String schoolName,String city){
        assertNotNull(school.getSchoolId());
        assertNotNull(school.getCreated());
        assertNotNull(school.getUpdated());

        assertEquals(schoolName,school.getSchoolName());
        assertEquals(city,school.getCity());

    }
}
