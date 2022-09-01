package com.example.schoolmanagement.comp.paperUser.user.service;

import com.example.schoolmanagement.comp.paperUser.user.domain.School;
import com.example.schoolmanagement.comp.paperUser.user.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;


    public School save(School school){
        if(school.getSchoolId()==null){
            school.setCreated(LocalDateTime.now());
        }
        school.setUpdated(LocalDateTime.now());
        return schoolRepository.save(school);
    }

    public List<School> findAllByCity(String city){
        return schoolRepository.findAllByCity(city);
    }

    public void updateName(Long schoolId, String name) {
        School school = schoolRepository.findById(schoolId).get();
        school.setSchoolName(name);
        save(school);

    }

    public List<String> cities() {
        return schoolRepository.getCities();
    }

    public Long countAllBySchool(){
        return schoolRepository.count();
    };

    public Page<School> pageSchool(int pageNum, int size) {
        return schoolRepository.findAll(PageRequest.of(pageNum-1,size));
    }


    public Optional<School> findById(Long schoolId) {
        return schoolRepository.findById(schoolId);
    }


}
