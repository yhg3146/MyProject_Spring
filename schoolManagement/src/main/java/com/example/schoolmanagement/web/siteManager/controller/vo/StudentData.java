package com.example.schoolmanagement.web.siteManager.controller.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentData {

    private Long userId;
    private String schoolName;
    private String grade;
    private String name;
    private String email;
}
