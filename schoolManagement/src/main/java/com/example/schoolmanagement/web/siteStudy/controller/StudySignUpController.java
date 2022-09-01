package com.example.schoolmanagement.web.siteStudy.controller;

import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.service.SchoolService;
import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import com.example.schoolmanagement.web.siteStudy.controller.vo.StudySignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class StudySignUpController {

    private final SchoolService schoolService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup/study")
    private String studentSignUp(Model model){
        model.addAttribute("site","study");
        model.addAttribute("cityList",schoolService.cities());
        return "/study/signup";
    }


    @PostMapping("/signUp/study")
    private String signupStudy(StudySignUpForm form,Model model){
        model.addAttribute("site","study");
        User student = User.builder()
                .name(form.getName())
                .email(form.getEmail())
                .password(passwordEncoder.encode(form.getPassword()))
                .enabled(true)
                .grade(form.getGrade())
                .build();

        schoolService.findById(form.getSchoolId()).ifPresent(
                school -> student.setSchool(school)
        );
        userService.findById(form.getTeacherId()).ifPresent(
                teacher->{
                    student.setTeacher(teacher);
                }
        );
        User saved =userService.save(student);
        userService.addAuthority(saved.getUserId(), Authority.ROLE_STUDENT);
        return "/loginForm";
    }

}
