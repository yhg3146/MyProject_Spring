package com.example.schoolmanagement.comp.paperUser.user.repository;

import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String username);

    @Query("select u from User u, Authority a where u.school.schoolId=?1 and u.userId =a.id and a.authority =?2")
    List<User> findUserBySchoolId(Long schoolId, String authority);

    @Query("select u from User u, User b where u.teacher.userId = b.userId and b.userId=?1")
    List<User> findStudentListByTeacherUserId(Long userId);

/*    @Query("select u from User u, User b where u.teacher.userId = b.userId and b.userId=?1")
    Page<User> findStudentListByTeacherUserId(Long userId,Pageable pageable);*/

    @Query("select u from User u, Authority a where u.userId = a.id and a.authority=?1")
    List<User> findAuthorityIn(String authority);

    @Query("select count(u) from User u,Authority a where u.userId=a.id and a.authority=?1")
    Long countByAuthorities(String role);

    @Query("select count(u) from User u,Authority a where u.school.schoolId=?1 and u.userId=a.id and a.authority=?2")
    Long countAllBySchoolIdAndAuthorities(Long schoolId, String role);

    @Query("select u from User u,Authority a where u.userId = a.id and a.authority=?1")
    Page<User> findAllByAuthoritiesIn(String roleTeacher, Pageable pageable);

    @Query("select count(u) from User u where u.teacher.userId=?1")
    Long countStudentByTeacherUserId(Long userId);

}
