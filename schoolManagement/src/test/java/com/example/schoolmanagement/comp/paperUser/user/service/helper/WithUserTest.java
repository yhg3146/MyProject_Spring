package com.example.schoolmanagement.comp.paperUser.user.service.helper;

import com.example.schoolmanagement.comp.paperUser.user.domain.School;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.repository.SchoolRepository;
import com.example.schoolmanagement.comp.paperUser.user.repository.UserRepository;
import com.example.schoolmanagement.comp.paperUser.user.service.SchoolService;
import com.example.schoolmanagement.comp.paperUser.user.service.UserSecurityService;
import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import javax.persistence.EntityManager;

public class WithUserTest {


    @Autowired
    protected SchoolRepository schoolRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected EntityManager entityManager;

    protected SchoolService schoolService;
    protected UserService userService;
    protected UserSecurityService userDetailService;


    protected SchoolServiceHelper schoolServiceHelper;
    protected UserServiceHelper userServiceHelper;

    protected School school;
    protected User user;
    protected User teacher;
    protected User student1;
    protected User student2;

    protected void prepareTest(){
        schoolRepository.deleteAll();
        userRepository.deleteAll();

        this.schoolService = new SchoolService(schoolRepository);
        this.userService = new UserService(userRepository);
        this.userDetailService = new UserSecurityService(userRepository);

        this.schoolServiceHelper = new SchoolServiceHelper(schoolService);
        this.userServiceHelper = new UserServiceHelper(userService, NoOpPasswordEncoder.getInstance());

        this.school = schoolServiceHelper.createSchool("테스트 학교","서울");
        this.user = userServiceHelper.createUser(school,"user1");
        this.teacher = userServiceHelper.createTeacher(school,"teacher1");
        this.student1 = userServiceHelper.createStudent(school,teacher,"study1","1학년");
        this.student2 = userServiceHelper.createStudent(school,teacher,"study2","1학년");
    }

}
