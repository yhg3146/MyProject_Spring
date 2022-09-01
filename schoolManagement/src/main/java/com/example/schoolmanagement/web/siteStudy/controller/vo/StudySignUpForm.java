package com.example.schoolmanagement.web.siteStudy.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudySignUpForm {
    private Long schoolId;
    private Long teacherId;
    private String grade;
    private String name;
    private String email;
    private String password;
    private String rePassword;
}
