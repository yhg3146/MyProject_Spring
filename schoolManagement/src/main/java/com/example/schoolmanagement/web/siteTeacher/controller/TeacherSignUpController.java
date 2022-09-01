package com.example.schoolmanagement.web.siteTeacher.controller;

import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.service.SchoolService;
import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import com.example.schoolmanagement.web.siteTeacher.controller.vo.TeacherSignForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class TeacherSignUpController {

    private final SchoolService schoolService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/signup/teacher")
    private String teacherSignUp(Model model){
        model.addAttribute("site","study");
        model.addAttribute("cityList",schoolService.cities());
        return "teacher/signup";
    }

    @PostMapping(value = "/signUp/teacher",consumes ={"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    private String signUp(TeacherSignForm form, Model model){
        User teacher = User.builder()
                .name(form.getName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .enabled(true)
                .build();

        schoolService.findById(form.getSchoolId()).ifPresent(
        school -> teacher.setSchool(school));
        User saved =userService.save(teacher);
        userService.addAuthority(saved.getUserId(), Authority.ROLE_TEACHER);
        model.addAttribute("site","teacher");
        return "loginForm";
    }

}
