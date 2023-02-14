package cherhy.soloProject.application.exception;

public class ExistException extends SnsException {

    private static String MESSAGE = "이미 존재하는 %s 입니다.";

    public static String parseMessage(String message){
        return String.format(MESSAGE, message);
    }

    public ExistException(String message) {
        super(parseMessage(message));
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
