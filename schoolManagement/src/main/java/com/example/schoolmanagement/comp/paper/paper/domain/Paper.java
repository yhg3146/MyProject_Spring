package com.example.schoolmanagement.comp.paper.paper.domain;

import com.example.schoolmanagement.comp.paperUser.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sp_paper")
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paperId;

    private String name;

    private Long paperTemplateId;


    private Long studyUserId;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<PaperAnswer> paperAnswerList;

    private LocalDateTime created;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Transient
    private User study;

    private PaperState state;



    public static enum PaperState {
        READY,
        START,
        END,
        PREPARE

    }

    private int total;

    private int answered;

    private int corrected;

    private int prePareCount;
    @Transient
    public double getScore(){
        if(total <1) return 0;
        return corrected*100/total;
    }

    public void addAnswered(){
        answered++;
    }

    public void addCorrected(){
        corrected++;
    }

    public Map<Integer,PaperAnswer> answerMap(){
        if(paperAnswerList == null){
            return new HashMap<>();
        }
        return paperAnswerList.stream().collect(Collectors.toMap(PaperAnswer::num,Function.identity()));
    }
}
