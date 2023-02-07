package cherhy.soloProject.application.exception;

public class UserNotFoundException extends SnsException {

    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
