package cherhy.soloProject.Util.scrollDto;

import java.io.Serializable;
import java.util.List;

public record ScrollResponse<T>(
        ScrollRequest nextScrollRequest,
        List<T> body
) implements Serializable {
}
