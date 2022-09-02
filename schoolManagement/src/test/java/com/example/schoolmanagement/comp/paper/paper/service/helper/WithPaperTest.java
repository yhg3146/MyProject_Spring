package com.example.schoolmanagement.comp.paper.paper.service.helper;

import com.example.schoolmanagement.comp.paper.paper.domain.PaperTemplate;
import com.example.schoolmanagement.comp.paper.paper.domain.Problem;
import com.example.schoolmanagement.comp.paper.paper.repository.PaperAnswerRepository;
import com.example.schoolmanagement.comp.paper.paper.repository.PaperRepository;
import com.example.schoolmanagement.comp.paper.paper.repository.PaperTemplateRepository;
import com.example.schoolmanagement.comp.paper.paper.repository.ProblemRepository;
import com.example.schoolmanagement.comp.paper.paper.service.PaperService;
import com.example.schoolmanagement.comp.paper.paper.service.PaperTemplateService;
import com.example.schoolmanagement.comp.paper.paper.service.ProblemService;
import com.example.schoolmanagement.comp.paperUser.user.service.helper.WithUserTest;
import org.springframework.beans.factory.annotation.Autowired;

public class WithPaperTest extends WithUserTest {

    @Autowired
    protected PaperTemplateRepository paperTemplateRepository;

    @Autowired
    protected ProblemRepository problemRepository;

    @Autowired
    protected PaperRepository paperRepository;

    @Autowired
    protected PaperAnswerRepository paperAnswerRepository;

    protected PaperTemplateService paperTemplateService;
    protected ProblemService problemService;
    protected PaperService paperService;

    protected PaperTemplateHelper paperTemplateHelper;

    protected PaperTemplate paperTemplate;
    protected Problem problem1;
    protected Problem problem2;


    protected void preparePaperTest(){
        prepareTest();
        paperTemplateRepository.deleteAll();

        this.problemService = new ProblemService(problemRepository);
        this.paperTemplateService = new PaperTemplateService(paperTemplateRepository,problemService);
        this.paperService = new PaperService(userRepository,paperRepository,paperAnswerRepository,paperTemplateService);

        this.paperTemplateHelper = new PaperTemplateHelper(paperTemplateService);


        this.paperTemplate = paperTemplateHelper.createPaperTemplate("template1",teacher);
        this.problem1 = paperTemplateService.addProblem(paperTemplate.getPaperTemplateId(),problem("문제1","답1"));
        this.problem2 = paperTemplateService.addProblem(paperTemplate.getPaperTemplateId(),problem("문제2","답2"));


    }

    protected Problem problem( String content, String answer){
        return Problem.builder()
                .content(content)
                .answer(answer)
                .build();
    }

}
