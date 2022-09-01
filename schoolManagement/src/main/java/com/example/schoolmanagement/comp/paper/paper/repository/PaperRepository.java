package com.example.schoolmanagement.comp.paper.paper.repository;

import com.example.schoolmanagement.comp.paper.paper.domain.Paper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaperRepository extends JpaRepository<Paper,Long> {
    List<Paper> findAllByPaperTemplateId(Long paperTemplateId);


    void deleteByPaperTemplateIdAndStudyUserIdIn(Long paperTemplateId, List<Long> ids);

    List<Paper> findByStudyUserIdAndStateIn(Long studyUserId, List<Paper.PaperState> states);

    Long countByStudyUserIdAndStateIn(Long userId, List<Paper.PaperState> states);

    Page<Paper> findAllByStudyUserIdAndStateIn(Long userId,List<Paper.PaperState> states, Pageable pageable);

    List<Paper> findAllByStudyUserIdAndStateIn(Long userId,List<Paper.PaperState> states);

    List<Paper> findAllByStudyUserId(Long userId);

    int countByPaperTemplateId(Long paperTemplateId);

    int countByPaperTemplateIdAndStateIn(Long paperTemplateId, List<Paper.PaperState> states);

    Optional<Paper> findByPaperIdAndPaperTemplateId(Long paperId,Long paperTemplateId);
}
