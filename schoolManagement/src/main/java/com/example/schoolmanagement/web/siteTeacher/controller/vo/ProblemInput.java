package com.example.schoolmanagement.web.siteTeacher.controller.vo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemInput {

    private Long paperTemplateId;

    private Long problemId;

    private String content;
    private String answer;

}
