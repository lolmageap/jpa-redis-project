package cherhy.soloProject.application.domain.follow.dto;

import javax.validation.constraints.NotBlank;

public record FollowMemberDto(@NotBlank Long MemberId, @NotBlank Long FollowerId) {}
