package cherhy.soloProject.exception;

public class SameMemberException extends SnsException {

    private static final String MESSAGE = "자기자신을 대상으로 할 수 없습니다.";

    public SameMemberException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 500;
    }

}
