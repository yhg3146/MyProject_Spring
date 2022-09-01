package com.example.schoolmanagement.comp.paperUser.user.service;

import com.example.schoolmanagement.comp.paperUser.user.domain.Authority;
import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import com.example.schoolmanagement.comp.paperUser.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user){
        if(user.getUserId()==null){
            user.setCreated(LocalDateTime.now());
        }
        user.setUpdated(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void updateUserName(Long userId, String name) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("없는 유저입니다."));
        user.setName(name);
        save(user);
    }

    public void addAuthority(Long userId, String role) {
        userRepository.findById(userId).ifPresent(user->{

            Authority newRole = new Authority(user.getUserId(),role);
            if(user.getAuthorities() == null){
                user.setAuthorities(new HashSet<>());
                user.getAuthorities().add(newRole);
                save(user);
            }
            else if(!user.getAuthorities().contains(newRole)){
                user.getAuthorities().add(newRole);
                save(user);
            }

        });
    }

    public void removeAuthority(Long userId, String role) {
    userRepository.findById(userId).ifPresent(user->{

        if(user.getAuthorities() == null){
            return;
        }
        Authority targetAuthority = new Authority(userId,role);
        if(user.getAuthorities().contains(targetAuthority)){
            user.getAuthorities().remove(targetAuthority);
        }
        save(user);
    });
    }

    public List<User> findTeacherListBySchoolId(Long schoolId) {
        return userRepository.findUserBySchoolId(schoolId,Authority.ROLE_TEACHER);
    }

    public List<User> findStudentListByTeacherUserId(Long userId) {
        return userRepository.findStudentListByTeacherUserId(userId);
    }

    public List<User> findAllByTeacherAuthority() {
        return userRepository.findAuthorityIn(Authority.ROLE_TEACHER);
    }

    public List<User> findAllByStudentAuthority() {
        return userRepository.findAuthorityIn(Authority.ROLE_STUDENT);
    }

    public List<User> findStudentListBySchoolId(Long schoolId) {
        return userRepository.findUserBySchoolId(schoolId,Authority.ROLE_STUDENT);
    }

    public Long countAllByAuthorities(String role) {
        return userRepository.countByAuthorities(role);
    }

    public Long countAllBySchoolIdAndAuthorities(Long schoolId,String role) {
        return userRepository.countAllBySchoolIdAndAuthorities(schoolId,role);
    }

    public Page<User> findAllTeacherList(Integer pageNum, Integer size) {
        return userRepository.findAllByAuthoritiesIn(Authority.ROLE_TEACHER, PageRequest.of(pageNum-1,size));
    }

    public Long countStudentByTeacherUserId(Long userId) {
        return userRepository.countStudentByTeacherUserId(userId);
    }

    public Page<User> findAllStudentList(Integer pageNum,Integer size) {
        return userRepository.findAllByAuthoritiesIn(Authority.ROLE_STUDENT,PageRequest.of(pageNum-1,size));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long userId){
        return userRepository.findById(userId);
    }

    public Map<Long, User> getUsers(List<Long> userIds) {

        return StreamSupport.stream(userRepository.findAllById(userIds).spliterator(),false)
                .collect(Collectors.toMap(User::getUserId,Function.identity()));

    }


  /*  public Page<User> studyListPage(Long userId, Integer pageNum, Integer size) {
        return userRepository.findStudentListByTeacherUserId(userId, PageRequest.of(pageNum-1,size));
    }*/
}
