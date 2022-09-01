package com.example.schoolmanagement.comp.paper.paper.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="sp_paper_answer")
public class PaperAnswer {


    @JsonIgnore
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name="paperId"))
    private Paper paper;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class PaperAnswerId implements Serializable {
        private Long paperId;
        private Integer num;

    }

    @EmbeddedId
    private PaperAnswerId id;

    private Long problemId;
    private String answer;
    private boolean correct;

    private LocalDateTime answered;

    public Integer num(){
        return id.getNum();
    }
}
