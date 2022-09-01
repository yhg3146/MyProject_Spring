package com.example.schoolmanagement.comp.paper.paper.service;

import com.example.schoolmanagement.comp.paper.paper.domain.Problem;
import com.example.schoolmanagement.comp.paper.paper.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ProblemService {

    private final ProblemRepository problemRepository;

    public Optional<Problem> findById(Long problemId){
        return problemRepository.findById(problemId);
    };

    public Problem save(Problem problem){
        if(problem.getProblemId() == null){
            problem.setCreated(LocalDateTime.now());
        }
        problem.setUpdated(LocalDateTime.now());
        return problemRepository.save(problem);
    }

    public void delete(Problem problem) {
        problemRepository.delete(problem);
    }

}
