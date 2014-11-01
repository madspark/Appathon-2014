package lt.vadovauk.readingexpert.app.domain;

import java.util.ArrayList;

public class Question {
    private int id;
    private int storyId;
    private String question;

    public ArrayList<String> getOtherAnswers() {
        return otherAnswers;
    }

    public void setOtherAnswers(ArrayList<String> otherAnswers) {
        this.otherAnswers = otherAnswers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String answer;
    ArrayList<String> otherAnswers;

    public Question(int id, int storyId, String question, String answer, ArrayList<String> otherAnswers) {
        this.id = id;
        this.storyId = storyId;
        this.question = question;
        this.answer = answer;
        this.otherAnswers = otherAnswers;
    }
}
