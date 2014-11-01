package lt.vadovauk.readingexpert.app.data;

public class DbContract {

    class Story {

        public static final String TABLE_NAME = "Stories";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_ID = "id";

        public static final String COLUMN_DIFF = "difficulty";

        public static final String COLUMN_IMG = "imagesource";

        public static final String COLUMN_DONE = "done";

        public static final String COLUMN_CONTENT = "content";

    }

    class Question {

        public static final String TABLE_NAME = "Questions";

        public static final String COLUMN_ID = "Qid";

        public static final String COLUMN_STORYID = "StoryID";

        public static final String COLUMN_CONTENT = "Qcontent";

        public static final String COLUMN_CORRECT = "correct answer";

        public static final String COLUMN_OTHER = "Other answers";
    }
}
