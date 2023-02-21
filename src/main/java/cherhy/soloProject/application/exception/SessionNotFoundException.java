package cherhy.soloProject.application.exception;

public class SessionNotFoundException extends SnsException {

    private static final String MESSAGE = "로그인을 해주세요.";

    public SessionNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
