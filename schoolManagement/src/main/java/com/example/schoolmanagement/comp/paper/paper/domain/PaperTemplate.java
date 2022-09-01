package com.example.schoolmanagement.comp.paper.paper.domain;

import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sp_paper_template")
public class PaperTemplate {

    @Id
    @GeneratedValue
    private Long paperTemplateId;

    private String name;

    private Long userId;

    @Transient
    private User creator;

    private int total;
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "paperTemplateId"))
    private List<Problem> problemList;

    private long publishedCount;

    private long completeCount;

    private boolean alreadyPublished;

    private boolean enableRemAndUpd;
    @Column(updatable = false)
    private LocalDateTime created;

    private LocalDateTime updated;





}
