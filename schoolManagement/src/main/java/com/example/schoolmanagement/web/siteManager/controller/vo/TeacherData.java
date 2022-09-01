package com.example.schoolmanagement.web.siteManager.controller.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherData {

    private Long userId;
    private String schoolName;
    private String name;
    private String email;
    private Long studentCount;

}
