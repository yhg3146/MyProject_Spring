package com.example.schoolmanagement.web.siteManager.controller;

import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import com.example.schoolmanagement.web.siteManager.controller.vo.StudentData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/study")
@RequiredArgsConstructor
public class StudentMngController {

    private final UserService userService;

    @GetMapping("/list")
    public String studentList(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "size",defaultValue = "10") Integer size,
            Model model)
    {
        model.addAttribute("menu","student");
        Page<StudentData> studentDataPage = userService.findAllStudentList(pageNum, size).map(
                student -> new StudentData(
                        student.getUserId(),
                        student.getSchool().getSchoolName(),
                        student.getGrade(),
                        student.getName(),
                        student.getEmail()
                ));

        model.addAttribute("page",studentDataPage);
        return "manager/study/list";

    }
}
