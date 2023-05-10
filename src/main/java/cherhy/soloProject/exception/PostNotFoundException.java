package cherhy.soloProject.exception;

public class PostNotFoundException extends SnsException {

    private static final String MESSAGE = "존재하지 않는 게시물입니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

}
