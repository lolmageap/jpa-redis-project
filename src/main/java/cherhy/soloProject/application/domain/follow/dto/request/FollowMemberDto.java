package cherhy.soloProject.application.domain.follow.dto;

import javax.validation.constraints.NotNull;

public record FollowMemberDto(
        @NotNull Long MemberId,
        @NotNull Long FollowerId
) {}
