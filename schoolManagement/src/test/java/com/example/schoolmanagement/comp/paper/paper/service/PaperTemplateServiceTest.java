package com.example.schoolmanagement.comp.paper.paper.service;

import com.example.schoolmanagement.comp.paper.paper.domain.PaperTemplate;
import com.example.schoolmanagement.comp.paper.paper.service.helper.WithPaperTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.example.schoolmanagement.comp.paper.paper.service.helper.PaperTemplateHelper.assertPaperTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaperTemplateServiceTest extends WithPaperTest {

    @BeforeEach
    void before(){
        preparePaperTest();
    }



    @DisplayName("1.템플렛 만들기 ")
    @Test
    void test_1() {

        assertPaperTemplate(paperTemplate,teacher,"template1");
    }


    @DisplayName("2.문제 추가 ")
    @Test
    void test_2() {

        PaperTemplate paperTemplate1 = paperTemplateRepository.findById(paperTemplate.getPaperTemplateId()).get();
        paperTemplate1.getProblemList().forEach(problem -> System.out.println(problem));
        assertEquals(3,paperTemplate1.getProblemList().size());
    }


    @DisplayName("3.문제 제거")
    @Test
    void test_3() {

        paperTemplateService.addProblem(paperTemplate.getPaperTemplateId(),problem("문제3","답3"));
        PaperTemplate paperTemplate1 = paperTemplateRepository.findById(paperTemplate.getPaperTemplateId()).get();
        paperTemplate1.getProblemList().forEach(problem -> System.out.println(problem));
        assertEquals(3,paperTemplate1.getProblemList().size());
        assertEquals(1,paperTemplate1.getProblemList().get(0).getIndexNum());
        assertEquals(2,paperTemplate1.getProblemList().get(1).getIndexNum());


        System.out.println("-----------------------2번 문제 제거-------------------------");

        paperTemplateService.removeProblem(paperTemplate.getPaperTemplateId(),problem2.getProblemId());
        paperTemplate1 = paperTemplateRepository.findById(paperTemplate.getPaperTemplateId()).get();
        paperTemplate1.getProblemList().forEach(problem -> System.out.println(problem));
        assertEquals(2,paperTemplate1.getProblemList().size());
        assertEquals(1,paperTemplate1.getProblemList().get(0).getIndexNum());

        System.out.println("-----------------------2번 문제 한 번더 제거-------------------------");
        paperTemplateService.removeProblem(paperTemplate.getPaperTemplateId(),problem2.getProblemId());
        paperTemplate1 = paperTemplateRepository.findById(paperTemplate.getPaperTemplateId()).get();
        paperTemplate1.getProblemList().forEach(problem -> System.out.println(problem));
        assertEquals(2,paperTemplate1.getProblemList().size());
        assertEquals(1,paperTemplate1.getProblemList().get(0).getIndexNum());
        assertEquals("문제3",paperTemplate1.getProblemList().get(1).getContent());

        System.out.println("-----------------------1번 문제 제거-------------------------");

        paperTemplateService.removeProblem(paperTemplate.getPaperTemplateId(),problem1.getProblemId());
        paperTemplate1 = paperTemplateRepository.findById(paperTemplate.getPaperTemplateId()).get();
        paperTemplate1.getProblemList().forEach(problem -> System.out.println(problem));
        assertEquals(1,paperTemplate1.getProblemList().size());


    }




}
