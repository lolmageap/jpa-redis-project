package cherhy.soloProject.Util;

import java.util.List;

public record PageScroll<T>(
        ScrollRequest nextScrollRequest,
        List<T> body
) {
}
