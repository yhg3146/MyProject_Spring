package com.example.schoolmanagement.comp.paperUser.user.repository;

import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
