package cherhy.soloProject.application.exception;

public class PostBlockNotFoundException extends SnsException {

    private static final String MESSAGE = "차단한 게시물이 존재하지 않습니다.";

    public PostBlockNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

}
