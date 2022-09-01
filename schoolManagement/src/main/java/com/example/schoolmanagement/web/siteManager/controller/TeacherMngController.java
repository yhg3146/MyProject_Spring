package com.example.schoolmanagement.web.siteManager.controller;

import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import com.example.schoolmanagement.web.siteManager.controller.vo.TeacherData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/teacher")
@RequiredArgsConstructor
public class TeacherMngController {

    private final UserService userService;

    @GetMapping("/list")
    public String teacherList(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "size",defaultValue = "10") Integer size,
            Model model
    ){
        model.addAttribute("menu","teacher");
        Page<TeacherData> teacherList = userService.findAllTeacherList(pageNum,size)
                .map(teacher -> new TeacherData(
                        teacher.getUserId(),
                        teacher.getSchool().getSchoolName(),
                        teacher.getName(),
                        teacher.getEmail(),
                        userService.countStudentByTeacherUserId(teacher.getUserId())
                ));

        model.addAttribute("page",teacherList);
        return "manager/teacher/list";

    }

}
