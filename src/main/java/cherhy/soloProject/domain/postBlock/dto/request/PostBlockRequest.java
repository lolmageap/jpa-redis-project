package cherhy.soloProject.domain.postBlock.dto.request;

import javax.validation.constraints.NotNull;

public record PostBlockRequest(
        @NotNull Long memberId,
        @NotNull Long PostId
) {}
