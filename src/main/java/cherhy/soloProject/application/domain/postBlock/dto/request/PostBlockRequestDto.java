package cherhy.soloProject.application.domain.postBlock.dto.request;

import javax.validation.constraints.NotNull;

public record PostBlockRequestDto(
        @NotNull Long memberId,
        @NotNull Long PostId
) {}
