package com.example.schoolmanagement.comp.paper.paper.service;

import com.example.schoolmanagement.comp.paper.paper.domain.Paper;
import com.example.schoolmanagement.comp.paper.paper.domain.PaperAnswer;
import com.example.schoolmanagement.comp.paper.paper.domain.PaperTemplate;
import com.example.schoolmanagement.comp.paper.paper.repository.PaperAnswerRepository;
import com.example.schoolmanagement.comp.paper.paper.repository.PaperRepository;
import com.example.schoolmanagement.comp.paperUser.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Transactional
@Service
@RequiredArgsConstructor
public class PaperService {

    private final UserRepository userRepository;
    private final PaperRepository paperRepository;
    private final PaperAnswerRepository paperAnswerRepository;

    private final PaperTemplateService paperTemplateService;

    public Paper save(Paper paper) {
        if (paper.getPaperId() == null) {
            paper.setCreated(LocalDateTime.now());
        }
        return paperRepository.save(paper);
    }

    public List<Paper> publishPaper(Long paperTemplateId, List<Long> studentIds) {

/*
        List<Paper> papers = paperTemplateService.findById(paperTemplateId).map(
                paperTemplate ->
                        StreamSupport.stream(userRepository.findAllById(studentIds).spliterator(), false).map(
                                user -> {
                                        Paper newPaper = Paper.builder()
                                                .name(paperTemplate.getName())
                                                .paperTemplateId(paperTemplate.getPaperTemplateId())
                                                .studyUserId(user.getUserId())
                                                .state(Paper.PaperState.READY)
                                                .total(paperTemplate.getTotal())
                                                .build();
                                        return save(newPaper);
                                }).collect(Collectors.toList())).orElseThrow(() -> new IllegalArgumentException(paperTemplateId + "없는 시험지 입니다."));
        paperTemplateService.updatePublishedCount(paperTemplateId, papers.size());
        return papers;*/
        PaperTemplate paperTemplate =paperTemplateService.findById(paperTemplateId).get();
        paperTemplate.setAlreadyPublished(true);
        paperTemplate.setEnableRemAndUpd(false);
        paperTemplateService.save(paperTemplate);
        List<Paper> papers = StreamSupport.stream(userRepository.findAllById(studentIds).spliterator(), false).map(
                                user -> {

                                    Optional<Paper> paper = findAllByStudyUserId(user.getUserId()).stream().filter(p -> p.getName().equals(paperTemplate.getName())).findFirst();
                                    if (paper.isEmpty()) {
                                        Paper newPaper = Paper.builder()
                                                .name(paperTemplate.getName())
                                                .paperTemplateId(paperTemplate.getPaperTemplateId())
                                                .studyUserId(user.getUserId())
                                                .state(Paper.PaperState.READY)
                                                .total(paperTemplate.getTotal())
                                                .build();
                                        return save(newPaper);
                                    } else if (paper.get().getState().equals(Paper.PaperState.PREPARE)) {
                                        paper.get().setState(Paper.PaperState.READY);
                                    }
                                    return null;
                                }).collect(Collectors.toList());
        paperTemplateService.updatePublishedCount(paperTemplateId, papers.size());
        return papers;
    }

    public List<Paper> getPapers(Long paperTemplateId) {
        return paperRepository.findAllByPaperTemplateId(paperTemplateId);
    }

    public void removePaper(Long paperTemplateId, List<Long> userIds) {
        paperTemplateService.findById(paperTemplateId).ifPresent(
                paperTemplate -> {
                    paperRepository.deleteByPaperTemplateIdAndStudyUserIdIn(paperTemplate.getPaperTemplateId(), userIds);

                }
        );
    }

    public void answer(Long paperId, Long problemId, int num, String answer) {
        paperRepository.findById(paperId).ifPresent(
                paper -> {
                    Optional<PaperAnswer> paperAnswer = paper.getPaperAnswerList() == null ? Optional.empty() :
                            paper.getPaperAnswerList().stream().filter(a -> a.getId().getNum() == num).findFirst();

                    if (paperAnswer.isPresent()) {
                        PaperAnswer pA = paperAnswer.get();
                        pA = paperAnswer.get();
                        pA.setProblemId(problemId);
                        pA.setAnswer(answer);
                        pA.setAnswered(LocalDateTime.now());
                        paperAnswerRepository.save(pA);
                    } else {
                        PaperAnswer pA = PaperAnswer.builder()
                                .paper(paper)
                                .id(new PaperAnswer.PaperAnswerId(paper.getPaperId(), num))
                                .problemId(problemId)
                                .answer(answer)
                                .answered(LocalDateTime.now())
                                .build();
                        if (paper.getPaperAnswerList() == null) {
                            paper.setPaperAnswerList(new ArrayList<>());
                        }
                        paper.getPaperAnswerList().add(pA);
                        paper.addAnswered();
                    }

                    if (paper.getState() != Paper.PaperState.START) {

                        paper.setState(Paper.PaperState.START);
                        paper.setStartTime(LocalDateTime.now());

                    }
                    paperRepository.save(paper);

                }

        );

    }

    @Transactional
    public void submit(Long paperId) {
        Paper paper = paperRepository.findById(paperId).orElseThrow(() -> new IllegalArgumentException("없는 시험지입니다."));
        Map<Integer, String> answerSheet = paperTemplateService.getPaperAnswerSheet(paper.getPaperTemplateId());
        if (paper.getPaperAnswerList() != null) {
            paper.getPaperAnswerList().stream().forEach(
                    paperAnswer -> {
                        if (paperAnswer.getAnswer() != null && paperAnswer.getAnswer().equals(answerSheet.get(paperAnswer.getId().getNum()))) {
                            paperAnswer.setCorrect(true);
                            paper.addCorrected();
                            paperAnswerRepository.save(paperAnswer);
                        }
                    });

        }
        else{
            paper.setStartTime(LocalDateTime.now());
        }
        paper.setEndTime(LocalDateTime.now());
        paper.setState(Paper.PaperState.END);
        save(paper);
        paperTemplateService.updateCompleteCount(paper.getPaperTemplateId());

    }


    public List<Paper> findByState(Long studyUserId, List<Paper.PaperState> states) {
        return paperRepository.findByStudyUserIdAndStateIn(studyUserId,states);
    }

    public Long countPapersIng(Long userId) {
        return paperRepository.countByStudyUserIdAndStateIn(userId,List.of(Paper.PaperState.READY, Paper.PaperState.START));
    }

    public Long countPapersResult(Long userId) {
        return paperRepository.countByStudyUserIdAndStateIn(userId,List.of(Paper.PaperState.END));

    }

    public List<Paper> getPapersByUserIng(Long userId) {
        return paperRepository.findByStudyUserIdAndStateIn(userId, List.of(Paper.PaperState.READY,Paper.PaperState.START));
    }

    public Optional<Paper> findById(Long paperId) {
        return paperRepository.findById(paperId);
    }

    public Page<Paper> getPapersByUserResult(Long userId,Integer pageNum,Integer size) {
        return paperRepository.findAllByStudyUserIdAndStateIn(userId,List.of(Paper.PaperState.END), PageRequest.of(pageNum-1,size));
    }

    @PostAuthorize("returnObject.isEmpty() || returnObject.get().studyUserId == principal.userId")
    public Optional<Paper> findPaper(Long paperId) {
        return paperRepository.findById(paperId).map(paper -> {
            paper.setStudy(userRepository.getOne(paper.getStudyUserId()));
            return paper;
        });
    }

    public List<Paper> findAllByStudyUserIdAndStateEnd(Long userId) {
        return paperRepository.findAllByStudyUserIdAndStateIn(userId,List.of(Paper.PaperState.END));
    }

    public void resetPaperRemove(Long paperId,Long paperTemplateId){
        paperRepository.findByPaperIdAndPaperTemplateId(paperId,paperTemplateId).ifPresent(
                paper -> {
                    PaperTemplate paperTemplate = paperTemplateService.findById(paper.getPaperTemplateId()).get();
                    paper.setTotal(paperTemplate.getTotal());
                    reset(paper, paperTemplate);

                    paperTemplateService.save(paperTemplate);
                }
        );
    }

    private void reset(Paper paper, PaperTemplate paperTemplate) {
        paperTemplate.setPublishedCount(0);
        paperTemplate.setCompleteCount(0);
        paper.setStartTime(null);
        paper.setEndTime(null);
        if(paper.getState().equals(Paper.PaperState.END)) {
            paperTemplate.setAlreadyPublished(false);
            paperTemplate.setEnableRemAndUpd(true);
            paper.setState(Paper.PaperState.READY);

        }
        else{
            paper.setState(Paper.PaperState.PREPARE);
            paper.setPrePareCount(paper.getPrePareCount()+1);
        }
        paper.setCorrected(0);

        if(paper.getPrePareCount()==countByPaperTemplateId(paperTemplate.getPaperTemplateId())){
            paperTemplate.setAlreadyPublished(false);
            paperTemplate.setEnableRemAndUpd(true);
            paper.setPrePareCount(0);
        }
        paperRepository.save(paper);
    }

    public void resetPaper(Long paperId,Long paperTemplateId) {
        paperRepository.findByPaperIdAndPaperTemplateId(paperId,paperTemplateId).ifPresent(
                paper -> {
                    PaperTemplate paperTemplate = paperTemplateService.findById(paper.getPaperTemplateId()).get();
                    paper.setTotal(paperTemplate.getTotal());

                    if(paper.getState().equals(Paper.PaperState.END)  || (paperTemplate.isAlreadyPublished() &&
                            countByPaperTemplateIdAndStateIn(paperTemplate.getPaperTemplateId(),List.of(Paper.PaperState.START, Paper.PaperState.READY,Paper.PaperState.PREPARE)) == countByPaperTemplateId(paperTemplate.getPaperTemplateId()))) {

                        reset(paper, paperTemplate);
                    }

                    paperTemplateService.save(paperTemplate);
                }
        );
    }



    public List<Paper> findAllByStudyUserId(Long userId) {
        return paperRepository.findAllByStudyUserId(userId);
    }

    public List<Paper> findAllByPaperTemplateId(Long paperTemplateId) {
        return paperRepository.findAllByPaperTemplateId(paperTemplateId);
    }

    public int countByPaperTemplateId(Long paperTemplateId) {
        return paperRepository.countByPaperTemplateId(paperTemplateId);
    }


    public int countByPaperTemplateIdAndStateIn(Long paperTemplateId,List<Paper.PaperState> states) {
        return paperRepository.countByPaperTemplateIdAndStateIn(paperTemplateId,states);

    }

}
