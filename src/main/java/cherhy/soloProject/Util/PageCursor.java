package cherhy.soloProject.Util;

import java.util.List;

public record PageCursor<T>(
        CursorRequest nextCursorRequest,
        List<T> body
) {
}
