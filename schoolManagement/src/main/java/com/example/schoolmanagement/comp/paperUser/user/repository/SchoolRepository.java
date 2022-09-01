package com.example.schoolmanagement.comp.paperUser.user.repository;

import com.example.schoolmanagement.comp.paperUser.user.domain.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School,Long> {

    List<School> findAllByCity(String city);

    @Query("select distinct city from School")
    List<String> getCities();


    Page<School> findAll(Pageable pageable);
}
