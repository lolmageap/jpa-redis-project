package cherhy.soloProject.domain.follow.dto.response;

import java.io.Serializable;

public record FollowMemberResponse(Long id, Long followId, String name, String email) implements Serializable {}
