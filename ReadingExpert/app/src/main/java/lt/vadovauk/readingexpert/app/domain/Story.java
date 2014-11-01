package lt.vadovauk.readingexpert.app.domain;

public class Story {
    private String title;
    private String description;
    private int difficulty; // words per minute
    private String content;

    public Story(String title, String description, int difficulty, String content) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
