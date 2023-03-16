package cherhy.soloProject.domain.follow.dto.response;

import java.io.Serializable;

public record ResponseFollowMemberDto(Long id, Long followId, String name, String email) implements Serializable {}
