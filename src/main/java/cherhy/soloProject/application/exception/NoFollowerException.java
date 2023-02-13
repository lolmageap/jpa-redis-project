package cherhy.soloProject.application.exception;

public class NoFollowerException extends SnsException {

    private static String MESSAGE = "팔로워가 아닙니다";

    public NoFollowerException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
