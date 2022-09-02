package com.example.schoolmanagement.comp.paper.paper.service;

import com.example.schoolmanagement.comp.paper.paper.domain.Paper;
import com.example.schoolmanagement.comp.paper.paper.service.helper.WithPaperTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaperServiceTest extends WithPaperTest {

    private Paper paper;

    @BeforeEach
    void before(){
        preparePaperTest();
        this.paper = paperService.publishPaper(paperTemplate.getPaperTemplateId(), List.of(student1.getUserId())).get(0);
    }




    @DisplayName("1.2문제 풀어서 100점을 맞는다, ")
    @Test
    void test_1() {
        paperService.answer(paper.getPaperId(),problem1.getProblemId(),1,"답1");
        paperService.answer(paper.getPaperId(),problem2.getProblemId(),2,"답2");
        Paper paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(2,paper1.getPaperAnswerList().size());
        assertEquals(2,paper1.getAnswered());
        assertEquals(0,paper1.getCorrected());
        assertEquals(Paper.PaperState.START,paper1.getState());

        paperService.submit(paper.getPaperId());
        paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(2,paper1.getPaperAnswerList().size());
        assertEquals(2,paper1.getAnswered());
        assertEquals(2,paper1.getCorrected());
        assertEquals(Paper.PaperState.END,paper1.getState());
        assertNotNull(paper1.getStartTime());
        assertNotNull(paper1.getEndTime());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getCompleteCount());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getPublishedCount());


    }


    @DisplayName("2. 두문제 중 1문제만 맞는다. ")
    @Test
    void test_2() {

        paperService.answer(paper.getPaperId(),problem1.getProblemId(),1,"답1");
        paperService.answer(paper.getPaperId(),problem2.getProblemId(),2,"답1");
        Paper paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(2,paper1.getPaperAnswerList().size());
        assertEquals(2,paper1.getAnswered());
        assertEquals(0,paper1.getCorrected());
        assertEquals(Paper.PaperState.START,paper1.getState());

        paperService.submit(paper.getPaperId());
        paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(2,paper1.getPaperAnswerList().size());
        assertEquals(2,paper1.getAnswered());
        assertEquals(1,paper1.getCorrected());
        assertEquals(Paper.PaperState.END,paper1.getState());
        assertNotNull(paper1.getStartTime());
        assertNotNull(paper1.getEndTime());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getCompleteCount());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getPublishedCount());

    }

    @DisplayName("1. 2문제 다 틀린다.")
    @Test
    void test_3() {

        paperService.answer(paper.getPaperId(),problem1.getProblemId(),1,"몰라요");
        paperService.answer(paper.getPaperId(),problem2.getProblemId(),2,"몰라요");
        Paper paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(2,paper1.getPaperAnswerList().size());
        assertEquals(2,paper1.getAnswered());
        assertEquals(0,paper1.getCorrected());
        assertEquals(Paper.PaperState.START,paper1.getState());

        paperService.submit(paper.getPaperId());
        paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(2,paper1.getPaperAnswerList().size());
        assertEquals(2,paper1.getAnswered());
        assertEquals(0,paper1.getCorrected());
        assertEquals(Paper.PaperState.END,paper1.getState());
        assertNotNull(paper1.getStartTime());
        assertNotNull(paper1.getEndTime());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getCompleteCount());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getPublishedCount());
    }

    @DisplayName("1. 1문제 풀다가 중간에 제출")
    @Test
    void test_4() {

        paperService.answer(paper.getPaperId(),problem1.getProblemId(),1,"답1");

        Paper paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(2,paper1.getTotal());
        assertEquals(1,paper1.getPaperAnswerList().size());
        assertEquals(1,paper1.getAnswered());
        assertEquals(0,paper1.getCorrected());
        assertEquals(Paper.PaperState.START,paper1.getState());


        paperService.submit(paper.getPaperId());
        paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(1,paper1.getPaperAnswerList().size());
        assertEquals(1,paper1.getAnswered());
        assertEquals(1,paper1.getCorrected());
        assertEquals(Paper.PaperState.END,paper1.getState());
        assertNotNull(paper1.getStartTime());
        assertNotNull(paper1.getEndTime());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getCompleteCount());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getPublishedCount());
    }

    @DisplayName("5. 문제 안풀고 바로 제출")
    @Test
    void test_5() {


        Paper paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertEquals(2,paper1.getTotal());
        assertNull(paper1.getPaperAnswerList());
        assertEquals(0,paper1.getAnswered());
        assertEquals(0,paper1.getCorrected());
        assertEquals(Paper.PaperState.READY,paper1.getState());

        paperService.submit(paper.getPaperId());
        paper1 = paperRepository.findById(paper.getPaperId()).get();
        assertNull(paper1.getPaperAnswerList());
        assertEquals(0,paper1.getAnswered());
        assertEquals(0,paper1.getCorrected());
        assertEquals(Paper.PaperState.END,paper1.getState());
        assertNotNull(paper1.getStartTime());
        assertNotNull(paper1.getEndTime());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getCompleteCount());
        assertEquals(1,paperTemplateRepository.findById(paper1.getPaperId()).get().getPublishedCount());
    }


    @DisplayName("6.상태에 따라 시험지가 조회된다. ")
    @Test
    void test_6() {
        System.out.println("--------------------------문제 풀기 전--------------------------");
        List<Paper> paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.READY));
        assertEquals(1,paperList.size());
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.START));
        assertEquals(0,paperList.size());
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.END));
        assertEquals(0,paperList.size());
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.READY,Paper.PaperState.END));
        assertEquals(1,paperList.size());

        System.out.println("--------------------------문제 푸는 중--------------------------");

        paperService.answer(paper.getPaperId(),problem1.getProblemId(),1,"답1");
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.READY));
        assertEquals(0,paperList.size());
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.START));
        assertEquals(1,paperList.size());
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.END));
        assertEquals(0,paperList.size());

        System.out.println("--------------------------답안 제출--------------------------");

        paperService.submit(paper.getPaperId());
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.READY));
        assertEquals(0,paperList.size());
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.START));
        assertEquals(0,paperList.size());
        paperList = paperService.findByState(student1.getUserId(), List.of(Paper.PaperState.END));
        assertEquals(1,paperList.size());

    }

}