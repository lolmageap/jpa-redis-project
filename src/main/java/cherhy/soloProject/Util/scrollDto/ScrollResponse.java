package cherhy.soloProject.Util.scrollDto;

import java.util.List;

public record ScrollResponse<T>(
        ScrollRequest nextScrollRequest,
        List<T> body
) {
}
