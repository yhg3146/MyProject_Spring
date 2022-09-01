package com.example.schoolmanagement.web.siteManager.controller;

import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.domain.School;
import com.example.schoolmanagement.comp.paperUser.user.service.SchoolService;
import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/manager/school")
@RequiredArgsConstructor
public class SchoolMngController {

    private final SchoolService schoolService;
    private final UserService userService;

    @GetMapping("/list")
    public String schoolList(
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "size",defaultValue = "10") Integer size,
            Model model) {

        model.addAttribute("menu","school");
        Page<School> schoolPage = schoolService.pageSchool(pageNum,size);
        schoolPage.getContent().stream().forEach(
                school -> {
                    school.setTeacherCount(userService.countAllBySchoolIdAndAuthorities(school.getSchoolId(), Authority.ROLE_TEACHER));
                    school.setStudentCount(userService.countAllBySchoolIdAndAuthorities(school.getSchoolId(),Authority.ROLE_STUDENT));
                }
        );
        model.addAttribute("page",schoolPage);
        return "/manager/school/list";
    }

    @GetMapping("/edit")
    public String schoolEdit(
            @RequestParam(value = "schoolId",required = false) Long schoolId,
            Model model) {

        model.addAttribute("menu","school");

        if(schoolId != null){
            model.addAttribute("school",schoolService.findById(schoolId).get());
        }
        else{
            model.addAttribute("school",School.builder().build());
        }
        return "manager/school/edit";
    }

    @PostMapping(value = "/save",consumes ={"application/x-www-form-urlencoded;charset=UTF-8",MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String schoolSave(School school){
        schoolService.save(school);
        return "redirect:/manager/school/list";
    }
}
