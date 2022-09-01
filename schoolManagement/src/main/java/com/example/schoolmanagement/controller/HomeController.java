package com.example.schoolmanagement.controller;


import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.domain.School;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.service.SchoolService;
import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final SchoolService schoolService;
    private final UserService userService;
    private RequestCache requestCache;
    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/login")
    public String login(
            @AuthenticationPrincipal User user,
            @RequestParam(value="site",required = false) String site,
            @RequestParam(value="error",defaultValue = "false") Boolean error,
            HttpServletRequest request,
            Model model){

        if(user != null && user.isEnabled()){
            if(user.getAuthorities().contains(Authority.ADMIN_AUTHORITY)){
                return "redirect:/manager";
            }
            else if(user.getAuthorities().contains(Authority.TEACHER_AUTHORITY)){
                return "redirect:/teacher";
            }
            else if(user.getAuthorities().contains(Authority.STUDENT_AUTHORITY)){
                return "redirect:/study";
            }
        }
        if(site ==null){
            SavedRequest savedRequest = requestCache.getRequest(request,null);
            if(savedRequest != null){
                site = estimate(savedRequest.getRedirectUrl());
            }
        }
        model.addAttribute("site",site);
        return "/loginForm";
    }

    private String estimate(String redirectUrl) {
        if(redirectUrl == null){
            return "/study";
        }
        try{
            URL url = new URL(redirectUrl);
            String path = url.getPath();
            if(path != null){
                if(path.startsWith("/teacher")) return "teacher";
                if(path.startsWith("/study")) return "study";
                if(path.startsWith("/manager")) return "manager";
            }

            String query = url.getQuery();
            if( query != null){
                if(path.startsWith("/site=teacher")) return "teacher";
                if(path.startsWith("/site=study")) return "study";
                if(path.startsWith("/site=manager")) return "manager";
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return "study";
    }

    @GetMapping("/schools")
    @ResponseBody
    private List<School> schoolList(@RequestParam(value = "city", required = true) String city){
        return schoolService.findAllByCity(city);
    }

    @GetMapping("/teachers")
    @ResponseBody
    private List<User> TeacherList(@RequestParam(value = "schoolId",required = true) Long schoolId){
        return userService.findTeacherListBySchoolId(schoolId);
    }

    @GetMapping("/access-denied")
    private String accessDenied(){
        return "/AccessDenied";
    }

}
