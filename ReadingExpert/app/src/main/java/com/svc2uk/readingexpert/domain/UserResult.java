package com.svc2uk.readingexpert.domain;

public class UserResult {
    public int storyId;
    public boolean isDone;
    public float bestResult;

    public UserResult(int storyId, boolean isDone, float bestResult) {
        this.storyId = storyId;
        this.isDone = isDone;
        this.bestResult = bestResult;
    }
}
