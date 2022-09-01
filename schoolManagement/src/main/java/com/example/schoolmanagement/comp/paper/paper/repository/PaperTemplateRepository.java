package com.example.schoolmanagement.comp.paper.paper.repository;

import com.example.schoolmanagement.comp.paper.paper.domain.PaperTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaperTemplateRepository extends JpaRepository<PaperTemplate,Long> {

    Long countByUserId(Long userId);


    @Query("select p from PaperTemplate p where p.userId=?1")
    Page<PaperTemplate> paperTemplateListPage(Long userId,Pageable pageable);
}
