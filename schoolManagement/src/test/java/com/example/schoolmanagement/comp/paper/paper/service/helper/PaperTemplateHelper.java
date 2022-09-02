package com.example.schoolmanagement.comp.paper.paper.service.helper;

import com.example.schoolmanagement.comp.paper.paper.domain.PaperTemplate;
import com.example.schoolmanagement.comp.paper.paper.service.PaperTemplateService;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import lombok.RequiredArgsConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class PaperTemplateHelper {

    private final PaperTemplateService paperTemplateService;

    public static PaperTemplate makePaperTemplate(String name, User creator){
        return PaperTemplate.builder()
                .name(name)
                .creator(creator)
                .userId(creator.getUserId())
                .build();
    }

    public PaperTemplate createPaperTemplate(String name,User creator){
        PaperTemplate paperTemplate = makePaperTemplate(name,creator);
        return paperTemplateService.save(paperTemplate);
    }




    public static void assertPaperTemplate(PaperTemplate paperTemplate,User creator,String name){
        assertNotNull(paperTemplate.getPaperTemplateId());
        assertNotNull(paperTemplate.getCreated());
        assertNotNull(paperTemplate.getUpdated());

//        assertNull(paperTemplate.getProblemList());
        assertEquals(0,paperTemplate.getCompleteCount());
        assertEquals(0,paperTemplate.getPublishedCount());

        assertEquals(name,paperTemplate.getName());
        assertEquals(creator,paperTemplate.getCreator());


    }

}
