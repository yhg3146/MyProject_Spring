package com.example.schoolmanagement.web.siteStudy.controller;

import com.example.schoolmanagement.comp.paper.paper.domain.Paper;
import com.example.schoolmanagement.comp.paper.paper.domain.PaperAnswer;
import com.example.schoolmanagement.comp.paper.paper.domain.PaperTemplate;
import com.example.schoolmanagement.comp.paper.paper.domain.Problem;
import com.example.schoolmanagement.comp.paper.paper.service.PaperService;
import com.example.schoolmanagement.comp.paper.paper.service.PaperTemplateService;
import com.example.schoolmanagement.comp.paper.paper.service.ProblemService;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.web.siteStudy.controller.vo.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

    private final PaperTemplateService paperTemplateService;

    private final PaperService paperService;
    private final ProblemService problemService;


    @GetMapping({"","/"})
    public String index(
            @AuthenticationPrincipal User user,
            Model model){
        model.addAttribute("menu","study");
        model.addAttribute("paperCount",paperService.countPapersIng(user.getUserId()));
        model.addAttribute("resultCount",paperService.countPapersResult(user.getUserId()));


        return "/study/index";
    }



    @GetMapping("/papers")
    public String papers(
            @AuthenticationPrincipal User user,
            Model model){
        model.addAttribute("menu","papers");
        model.addAttribute("papers",paperService.getPapersByUserIng(user.getUserId()));
        return "/study/paper/papers";
    }

    @GetMapping("/paper/apply")
    public String paperApply(@RequestParam Long paperId,
                             @AuthenticationPrincipal User user,
                             Model model){


        model.addAttribute("menu","paper");
        Paper paper= paperService.findById(paperId).get();
        if(paper.getState() == Paper.PaperState.END){
            return "redirect:/study/paper/result?paperId="+paperId;
        }

        Map<Integer, PaperAnswer>  answerMap = paper.answerMap();

        PaperTemplate template = paperTemplateService.findById(paper.getPaperTemplateId()).orElseThrow(()->new IllegalArgumentException(paper.getPaperTemplateId()+"시험지가 없습니다."));
        Optional<Problem> notAnsweredProblem = template.getProblemList().stream().filter(problem->!answerMap.containsKey(problem.getIndexNum())).findFirst();

        model.addAttribute("paperId", paperId);
        model.addAttribute("paperName", paper.getName());
        if(notAnsweredProblem.isPresent()){
            model.addAttribute("problem",notAnsweredProblem.get());
            model.addAttribute("alldone",false);
        }
        else{
            model.addAttribute("alldone",true);
        }
        return "/study/paper/apply";
    }

    @PostMapping(value = "/paper/answer",consumes = {"application/x-www-form-urlencoded;charset=UTF-8", MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String paperAnswer(
            Answer answer,
            @AuthenticationPrincipal User user,
            @RequestParam Long paperId
            ){

        paperService.answer(paperId,answer.getProblemId(),answer.getIndexNum(),answer.getAnswer());
        return "redirect:/study/paper/apply?paperId="+answer.getPaperId();
    }

    @GetMapping("/paper/done")
    public String paperDone(
            @AuthenticationPrincipal User user,
            @RequestParam Long paperId,
            Model model
    ){
        paperService.submit(paperId);
        return "redirect:/study/paper/apply?paperId="+paperId;
    }

    @GetMapping("/paper/result")
    public String paperResult(
            @AuthenticationPrincipal User user,
            Long paperId,
            Model model){
        model.addAttribute("menu","result");
        Paper  paper = paperService.findPaper(paperId).orElseThrow(()->new IllegalArgumentException(paperId+"없는 시험지입니다."));
        model.addAttribute("paper",paper);
        return "/study/paper/result";
    }

    @GetMapping("/results")
    public String results(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageNum",defaultValue = "10") Integer size,
            Model model){
        model.addAttribute("menu","result");
        model.addAttribute("page",paperService.getPapersByUserResult(user.getUserId(),pageNum,size));
        return "/study/paper/results";
    }



    @GetMapping("/signup")
    public String signUp(){
        return "/study/signup";
    }


}
