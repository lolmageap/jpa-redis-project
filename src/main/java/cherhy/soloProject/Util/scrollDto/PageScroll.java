package cherhy.soloProject.Util.scrollDto;

import java.util.List;

public record PageScroll<T>(
        ScrollRequest nextScrollRequest,
        List<T> body
) {
}
