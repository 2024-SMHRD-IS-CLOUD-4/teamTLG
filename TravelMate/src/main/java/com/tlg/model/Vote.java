package com.tlg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class Vote {
    private int choiceIdx;
    private int trIdx;
    private int scheIdx;
    private String voteCont;
    private int voteScore;

    // 기본 생성자
    public Vote() {}

    // 필드를 초기화하는 생성자
    public Vote(int choiceIdx, int trIdx, int scheIdx, String voteCont, int voteScore) {
        this.choiceIdx = choiceIdx;
        this.trIdx = trIdx;
        this.scheIdx = scheIdx;
        this.voteCont = voteCont;
        this.voteScore = voteScore;
    }

    // Getters and setters
    public int getChoiceIdx() {
        return choiceIdx;
    }

    public void setChoiceIdx(int choiceIdx) {
        this.choiceIdx = choiceIdx;
    }

    public int getTrIdx() {
        return trIdx;
    }

    public void setTrIdx(int trIdx) {
        this.trIdx = trIdx;
    }

    public int getScheIdx() {
        return scheIdx;
    }

    public void setScheIdx(int scheIdx) {
        this.scheIdx = scheIdx;
    }

    public String getVoteCont() {
        return voteCont;
    }

    public void setVoteCont(String voteCont) {
        this.voteCont = voteCont;
    }

    public int getVoteScore() {
        return voteScore;
    }

    public void setVoteScore(int voteScore) {
        this.voteScore = voteScore;
    }
}