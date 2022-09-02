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
public class PaperTest extends WithPaperTest {

    @BeforeEach
    void before(){
        preparePaperTest();
    }


    @DisplayName("1. 학생1에게 시험지를 낸다.")
    @Test
    void test_1() {
        List<Paper> papers =paperService.publishPaper(paperTemplate.getPaperTemplateId(), List.of(student1.getUserId()));
        assertEquals(1,papers.size());

        Paper paper1= papers.get(0);
        assertEquals(paperTemplate.getName(),paper1.getName());
        assertEquals(1,paper1.getPaperTemplateId());
        assertEquals(student1.getUserId(),paper1.getStudyUserId());
        assertEquals(Paper.PaperState.READY,paper1.getState());


        assertNotNull(paper1.getCreated());

        assertNull(paper1.getStartTime());
        assertNull(paper1.getEndTime());
        assertEquals(0,paper1.getAnswered());
        assertEquals(0,paper1.getCorrected());
        assertEquals(3,paper1.getTotal());

    }

    @DisplayName("2.학습자1과 학습자2의 시험지를 가져온다. ")
    @Test
    void test_2() {
        paperService.publishPaper(paperTemplate.getPaperTemplateId(),List.of(student1.getUserId(),student2.getUserId()));

        List<Paper> papers = paperService.getPapers(paperTemplate.getPaperTemplateId());
        System.out.println(papers.get(0));
        System.out.println(papers.get(1));
        assertEquals(2,papers.size());
    }


    @DisplayName("3.시험지 삭제 기능")
    @Test
    void test_3() {
        paperService.publishPaper(paperTemplate.getPaperTemplateId(),List.of(student1.getUserId(),student2.getUserId()));
        assertEquals(2,paperRepository.findAll().size());
        paperService.removePaper(paperTemplate.getPaperTemplateId(),List.of(student1.getUserId()));
        assertEquals(1,paperRepository.findAll().size());
        paperService.removePaper(paperTemplate.getPaperTemplateId(),List.of(student1.getUserId()));
        assertEquals(1,paperRepository.findAll().size());
        paperService.removePaper(paperTemplate.getPaperTemplateId(),List.of(student2.getUserId()));
        assertEquals(0,paperRepository.findAll().size());

    }


}
