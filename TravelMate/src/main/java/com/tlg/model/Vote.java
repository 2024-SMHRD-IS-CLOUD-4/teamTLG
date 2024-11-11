package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
public class Vote {
    private int choiceIdx;
    private int trIdx;
    private int scheIdx;
    private String voteCont;
    private int voteScore;

}