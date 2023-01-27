package cherhy.soloProject.application.domain.follow.dto.request;

import javax.validation.constraints.NotNull;

public record FollowMemberDto(
        @NotNull Long MemberId,
        @NotNull Long FollowerId
) {}
