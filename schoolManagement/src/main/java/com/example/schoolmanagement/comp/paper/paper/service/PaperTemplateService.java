package com.example.schoolmanagement.comp.paper.paper.service;

import com.example.schoolmanagement.comp.paper.paper.domain.PaperTemplate;
import com.example.schoolmanagement.comp.paper.paper.domain.Problem;
import com.example.schoolmanagement.comp.paper.paper.repository.PaperTemplateRepository;
import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional
@Service
public class PaperTemplateService {

    private final PaperTemplateRepository paperTemplateRepository;
    private final ProblemService problemService;



    public Optional<PaperTemplate> findById(Long paperTemplateId){
            return paperTemplateRepository.findById(paperTemplateId);
    }

    public PaperTemplate save(PaperTemplate paperTemplate){
        if(paperTemplate.getPaperTemplateId()==null){
            paperTemplate.setCreated(LocalDateTime.now());
        }
        paperTemplate.setUpdated(LocalDateTime.now());
        return paperTemplateRepository.save(paperTemplate);
    }


    public Problem addProblem(Long paperTemplateId, Problem newProblem){
        return paperTemplateRepository.findById(paperTemplateId).map(
                paperTemplate -> {
                    paperTemplate.setEnableRemAndUpd(true);
                    if(paperTemplate.getProblemList() == null){
                        paperTemplate.setProblemList(new ArrayList<>());
                    }
                    newProblem.setPaperTemplateId(paperTemplate.getPaperTemplateId());
                    paperTemplate.getProblemList().add(newProblem);
                    IntStream.rangeClosed(1,paperTemplate.getProblemList().size()).forEach(
                            n -> {
                                paperTemplate.getProblemList().get(n-1).setIndexNum(n);
                            }
                    );
                    Problem saved =problemService.save(newProblem);
                    paperTemplate.setTotal(paperTemplate.getProblemList().size());
                    save(paperTemplate);
                    return saved;
                    }

        ).orElseThrow(()->new IllegalArgumentException(paperTemplateId+"가 존재하지 않습니다."));

    }

    public void removeProblem(Long paperTemplateId,Long problemId){
        paperTemplateRepository.findById(paperTemplateId).ifPresentOrElse(
                paperTemplate -> {
                    Problem targetProblem = problemService.findById(problemId).get();
                 if (paperTemplate.getProblemList().contains(targetProblem))
                 {
                     paperTemplate.getProblemList().remove(targetProblem);
                     IntStream.rangeClosed(1,paperTemplate.getProblemList().size()).forEach(
                             n->paperTemplate.getProblemList().get(n-1).setIndexNum(n)
                     );
                     problemService.delete(targetProblem);
                     paperTemplate.setTotal(paperTemplate.getProblemList().size());
                     save(paperTemplate);
                 }

                }


                ,()-> new IllegalArgumentException(paperTemplateId+"가 없는 시험지 입니다."));
    }

    public PaperTemplate removeProblem2(Long paperTemplateId,Long problemId){
        return paperTemplateRepository.findById(paperTemplateId).map(
        paperTemplate -> {
            if(paperTemplate.getProblemList() == null){
                return paperTemplate;
            }

            Optional<Problem> problem = paperTemplate.getProblemList().stream().filter(p -> p.getProblemId().equals(problemId)).findFirst();
            if(problem.isPresent()){
                paperTemplate.setProblemList(paperTemplate.getProblemList().stream().filter(
                        p -> !p.getProblemId().equals(problemId)
                ).collect(Collectors.toList()));

                IntStream.rangeClosed(1,paperTemplate.getProblemList().size()).forEach(
                        n -> paperTemplate.getProblemList().get(n-1).setIndexNum(n)
                );
                problemService.delete(problem.get());
                paperTemplate.setTotal(paperTemplate.getProblemList().size());
                save(paperTemplate);
            }
            return paperTemplate;
            }

        ).orElseThrow(()-> new IllegalArgumentException(paperTemplateId+"가 없는 시험지 입니다."));
    }


    public void updatePublishedCount(Long paperTemplateId, int size) {
        paperTemplateRepository.findById(paperTemplateId).ifPresentOrElse(
                paperTemplate ->{
                    paperTemplate.setPublishedCount(size);
                    save(paperTemplate);
                }
                ,()-> new IllegalArgumentException(paperTemplateId+"가 없는 시험지입니다."));

    }


    public Map<Integer, String> getPaperAnswerSheet(Long paperTemplateId) {
        PaperTemplate paperTemplate = paperTemplateRepository.findById(paperTemplateId).orElseThrow(()-> new IllegalArgumentException("없는 시험지입니다."));
        Map<Integer,String> answerSheet = paperTemplate.getProblemList().stream().collect(Collectors.toMap(Problem::getIndexNum,Problem::getAnswer));
        return  answerSheet;
    }

    public void updateCompleteCount(Long paperTemplateId) {
        PaperTemplate paperTemplate =findById(paperTemplateId).get();
        paperTemplate.setCompleteCount(paperTemplate.getCompleteCount()+1);
        paperTemplateRepository.save(paperTemplate);
    }

    public Long countTemplateByUserId(Long userId) {
        return paperTemplateRepository.countByUserId(userId);
    }

    public Page<PaperTemplate> paperTemplateListPage(Long userId,Integer pageNum,Integer size) {
        return paperTemplateRepository.paperTemplateListPage(userId,PageRequest.of(pageNum-1,size));
    }

    @PostAuthorize("returnObject.isEmpty() || returnObject.get().userId == principal.userId")
    public Optional<PaperTemplate> findProblemTemplate(Long paperTemplateId){
        return paperTemplateRepository.findById(paperTemplateId).map(pt->{
                    if(pt.getProblemList().size() != pt.getTotal()){
                        pt.setTotal(pt.getProblemList().size());
                    }
                    if(pt.getUserId() != ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId()){
                        if(!((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().contains(Authority.ADMIN_AUTHORITY)){
                            throw new AccessDeniedException("access denied");
                        }
                    }
                    return pt;

        });
    }
}
