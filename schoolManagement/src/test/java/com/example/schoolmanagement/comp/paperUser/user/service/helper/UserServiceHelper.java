package com.example.schoolmanagement.comp.paperUser.user.service.helper;

import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.domain.School;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserServiceHelper {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public static User makeUser(School school, String name){
        return User.builder()
                .name(name)
                .email(name+"@test.com")
                .school(school)
                .enabled(true)
                .build();
    }

    public User createUser(School school,String name){
        User user = makeUser(school,name);
        user.setPassword(passwordEncoder.encode("1234"));
        return userService.save(user);
    }

    public User createUser(School school, String name, String ...authorities){
        User user = makeUser(school,name);
        user.setPassword(passwordEncoder.encode("1234"));
        User savedUser =userService.save(user);
        Stream.of(authorities).forEach(auth-> {
            userService.addAuthority(savedUser.getUserId(), auth);
        });
        return userService.save(savedUser);
    }



    public static void assertUser(School school,User user,String name){
        assertNotNull(user.getUserId());
        assertNotNull(user.getCreated());
        assertNotNull(user.getUpdated());

        assertNull(user.getGrade());

        assertEquals(true,user.isEnabled());

        assertEquals(school.getSchoolId(),user.getSchool().getSchoolId());
        assertEquals(name,user.getName());
    }

    public static void assertUser(School school,User user,String name,String ...authorities){
        assertUser(school,user,name);
        assertTrue(user.getAuthorities().containsAll(
                Stream.of(authorities).map(auth -> new Authority(user.getUserId(),auth)).collect(Collectors.toSet())
        ));

    }

    public User createTeacher(School school,String name){
        User teacher = createUser(school,name);
        userService.addAuthority(teacher.getUserId(), Authority.ROLE_TEACHER);
        return userService.save(teacher);
    }

    public User createStudent(School school,User teacher,String name,String grade){
        User student = createUser(school,name);
        student.setTeacher(teacher);
        student.setGrade(grade);
        userService.addAuthority(student.getUserId(),Authority.ROLE_STUDENT);
        return userService.save(student);
    }

    public static void assertTeacher(School school,User teacher,String name){
        assertUser(school,teacher,name,Authority.ROLE_TEACHER);
    }


}
