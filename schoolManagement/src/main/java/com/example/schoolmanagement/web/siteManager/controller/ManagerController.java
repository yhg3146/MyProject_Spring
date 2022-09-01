package com.example.schoolmanagement.web.siteManager.controller;

import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.service.SchoolService;
import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final SchoolService schoolService;
    private final UserService userService;

    @GetMapping({"", "/"})
    public String index(Model model) {
        model.addAttribute("schoolCount", schoolService.countAllBySchool());
        model.addAttribute("teacherCount", userService.countAllByAuthorities(Authority.ROLE_TEACHER));
        model.addAttribute("studyCount", userService.countAllByAuthorities(Authority.ROLE_STUDENT));
        return "manager/index";
    }

}
