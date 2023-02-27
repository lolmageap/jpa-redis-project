package cherhy.soloProject.application.exception;

public class MemberBlockException extends SnsException {

    private static final String MESSAGE = "차단되었습니다.";

    public MemberBlockException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

}
