package com.example.schoolmanagement.comp.paper.paper.repository;

import com.example.schoolmanagement.comp.paper.paper.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem,Long> {
}
