package Task.Management.System.models;

import Task.Management.System.models.contracts.Comment;
import Task.Management.System.utils.ValidationHelpers;

import static java.lang.String.format;

public class CommentImpl implements Comment {

    public static final int CONTENT_LEN_MIN = 3;
    public static final int CONTENT_LEN_MAX = 200;
    private static final String CONTENT_LEN_ERR = format(
            "Content must be between %d and %d characters long!",
            CONTENT_LEN_MIN,
            CONTENT_LEN_MAX);
    private final String author;
    private String content;

    public CommentImpl(String content, String author) {
        setContent(content);
        this.author = author;
    }

    @Override
    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        ValidationHelpers.validateIntRange(content.length(), CONTENT_LEN_MIN, CONTENT_LEN_MAX, CONTENT_LEN_ERR);
        this.content = content;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "----------" + System.lineSeparator() +
                getContent() + System.lineSeparator() +
                "User: " + getAuthor() + System.lineSeparator() +
                "----------" + System.lineSeparator();
    }
}