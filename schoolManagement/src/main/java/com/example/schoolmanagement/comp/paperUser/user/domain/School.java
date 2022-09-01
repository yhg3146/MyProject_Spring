package com.example.schoolmanagement.comp.paperUser.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sp_school")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolId;

    private String schoolName;

    private String city;



    @Column(updatable = false)
    private LocalDateTime created;

    private LocalDateTime updated;

    @Transient
    private Long teacherCount;

    @Transient
    private Long studentCount;
}
