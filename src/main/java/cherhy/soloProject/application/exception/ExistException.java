package cherhy.soloProject.application.exception;

import cherhy.soloProject.application.exception.enums.ExceptionKey;

public class ExistException extends SnsException {

    private static String MESSAGE = "이미 존재하는 %s 입니다.";

    public static String parseMessage(ExceptionKey exceptionKey){
        return String.format(MESSAGE, exceptionKey);
    }

    public ExistException(ExceptionKey exceptionKey) {
        super(parseMessage(exceptionKey));
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
