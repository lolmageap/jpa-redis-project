package cherhy.soloProject.domain.memberBlock.dto.response;

import java.io.Serializable;

public record MemberBlockResponseDto(Long MemberBlockId, Long memberId, String name, String email) implements Serializable {

}
