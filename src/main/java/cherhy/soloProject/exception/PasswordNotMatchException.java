package cherhy.soloProject.exception;

public class PasswordNotMatchException extends SnsException {

    private static final String MESSAGE = "비밀번호가 틀립니다.";

    public PasswordNotMatchException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

}
