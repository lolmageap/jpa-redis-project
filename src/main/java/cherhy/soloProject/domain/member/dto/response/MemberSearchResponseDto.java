package cherhy.soloProject.domain.member.dto.response;

import java.io.Serializable;

public record MemberSearchResponseDto(Long memberId, String name) implements Serializable {
}
