package com.example.schoolmanagement.web.siteTeacher.controller;

import com.example.schoolmanagement.comp.paper.paper.domain.Paper;
import com.example.schoolmanagement.comp.paper.paper.domain.PaperTemplate;
import com.example.schoolmanagement.comp.paper.paper.domain.Problem;
import com.example.schoolmanagement.comp.paper.paper.service.PaperService;
import com.example.schoolmanagement.comp.paper.paper.service.PaperTemplateService;
import com.example.schoolmanagement.comp.paper.paper.service.ProblemService;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import com.example.schoolmanagement.web.siteTeacher.controller.vo.ProblemInput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final UserService userService;
    private final PaperTemplateService paperTemplateService;
    private final ProblemService problemService;
    private final PaperService paperService;

    @GetMapping({"", "/"})
    public String index(
            @AuthenticationPrincipal User user,
            Model model) {

        model.addAttribute("studentCount", userService.countStudentByTeacherUserId(user.getUserId()));
        model.addAttribute("paperTemplateCount", paperTemplateService.countTemplateByUserId(user.getUserId()));
//        model.addAttribute("paperTemplateCount","1");
        return "/teacher/index";

    }


    @GetMapping("/study/list")
    public String studentList(
            @AuthenticationPrincipal User user,
            Model model) {
        model.addAttribute("menu", "study");

        model.addAttribute("studyList", userService.findStudentListByTeacherUserId(user.getUserId()));

        return "/teacher/study/list";
    }


    @GetMapping("/study/results")
    public String studyResults(@RequestParam Long paperTemplateId, Model model) {
        model.addAttribute("menu", "paper");

        paperTemplateService.findProblemTemplate(paperTemplateId).map(paperTemplate -> {
            List<Paper> papers = paperService.getPapers(paperTemplateId);
            Map<Long, User> userMap = userService.getUsers(papers.stream().map(paper -> paper.getStudyUserId()).collect(Collectors.toList()));
            papers.stream().forEach(paper -> paper.setStudy(userMap.get(paper.getStudyUserId())));
            model.addAttribute("template", paperTemplateService.findById(paperTemplateId).get());
            model.addAttribute("papers", papers);
            return "/teacher/study/results";
        }).orElseThrow(() -> new AccessDeniedException("시험지가 존재하지 않습니다."));

        model.addAttribute("papers", paperService.getPapers(paperTemplateId));
        return "/teacher/study/results";
    }

    @GetMapping("/paperTemplate/list")
    public String paperTemplateList(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @AuthenticationPrincipal User user,
            Model model) {
        model.addAttribute("menu", "paper");
        Page<PaperTemplate> templatePages = paperTemplateService.paperTemplateListPage(user.getUserId(), pageNum, size).map(
                template -> {
                    return template;
                });

        model.addAttribute("page", templatePages);
        return "/teacher/paperTemplate/list";
    }

    @GetMapping("/paperTemplate/edit")
    public String paperTemplateEdit(
            @RequestParam Long paperTemplateId,
            @RequestParam(value = "problemId", required = false) Long problemId,
            Model model) {
        if (problemId != null) {
            model.addAttribute("last_problem", problemService.findById(problemId).get());

        }
        PaperTemplate paperTemplate = paperTemplateService.findProblemTemplate(paperTemplateId).orElseThrow(() -> new IllegalArgumentException(paperTemplateId + "시험지 템플릿이 존재하지 않습니다."));
        model.addAttribute("template", paperTemplate);
        return "/teacher/paperTemplate/edit";
    }

    @GetMapping("/paperTemplate/delete")
    public String paperTemplateDelete(
            @AuthenticationPrincipal User user,
            @RequestParam Long paperTemplateId,
            @RequestParam(value = "problemId", required = false) Long problemId,
            Model model) {
        if (problemId != null) {
            paperTemplateService.removeProblem(paperTemplateId, problemId);
            userService.findStudentListByTeacherUserId(user.getUserId()).stream().forEach(
                    study -> {
                        paperService.findAllByStudyUserId(study.getUserId()).stream().forEach(
                                paper -> {
                                    paperService.resetPaperRemove(paper.getPaperId(),paperTemplateId);
                                }
                        );
                    }

            );
        }
        return "redirect:/teacher/paperTemplate/edit?paperTemplateId=" + paperTemplateId;
    }


    @PostMapping(value = "/paperTemplate/problem/add", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String paperTemplateProblemAdd(
            Long problemId,
            ProblemInput problemInput,
            @AuthenticationPrincipal User user,
            Model model
    ) {

        if (problemId != null) {
            Problem update_problem = problemService.findById(problemId).get();
            update_problem.setPaperTemplateId(problemInput.getPaperTemplateId());
            update_problem.setContent(problemInput.getContent());
            update_problem.setAnswer(problemInput.getAnswer());
            problemService.save(update_problem);


        } else {
            Problem problem = Problem.builder()
                    .paperTemplateId(problemInput.getPaperTemplateId())
                    .content(problemInput.getContent())
                    .answer(problemInput.getAnswer())
                    .build();
            paperTemplateService.addProblem(problemInput.getPaperTemplateId(), problem);

        }
        userService.findStudentListByTeacherUserId(user.getUserId()).stream().forEach(
                study -> {
                    paperService.findAllByStudyUserId(study.getUserId()).stream().forEach(
                            paper -> {
                                paperService.resetPaper(paper.getPaperId(),problemInput.getPaperTemplateId());
                            }
                    );
                }

        );


        PaperTemplate paperTemplate = paperTemplateService.findById(problemInput.getPaperTemplateId()).get();
        model.addAttribute("template", paperTemplate);
        return "/teacher/paperTemplate/edit";
    }

    @GetMapping("/paperTemplate/publish")
    public String publishPaper(
            @RequestParam Long paperTemplateId,
            @AuthenticationPrincipal User user
    ) {
        List<User> studentList = userService.findStudentListByTeacherUserId(user.getUserId());
        paperService.publishPaper(paperTemplateId, studentList.stream().map(u -> u.getUserId()).collect(Collectors.toList()));


        return "redirect:/teacher/paperTemplate/list";
    }

    @GetMapping("/signup")
    public String signUp() {

        return "/teacher/signup";

    }
/*
    @PostMapping(value = "/study/save",consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    private String studySave(User user){
        userService.save(user);
        return "redirect:/teacher/study/list";
    }
    */

    @GetMapping("/paperTemplate/create")
    public String PaperTemplateCreate(@AuthenticationPrincipal User user, Model model) {

        return "/teacher/paperTemplate/create";
    }


    @PostMapping(value = "/paperTemplate/create", consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String PaperTemplateCreateForm(@AuthenticationPrincipal User user, @RequestParam String paperName, Model model) {
        PaperTemplate template = PaperTemplate.builder()
                .name(paperName)
                .userId(user.getUserId())
                .enableRemAndUpd(true)
                .build();

        PaperTemplate paperTemplate = paperTemplateService.save(template);
        model.addAttribute("template", paperTemplate);
        return "/teacher/paperTemplate/edit";
    }


}
