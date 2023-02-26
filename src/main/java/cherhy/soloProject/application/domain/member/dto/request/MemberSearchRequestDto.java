package cherhy.soloProject.application.domain.member.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record MemberSearchRequestDto(
        @NotNull(message = "세션이 만기되었습니다")
        Long memberId,
        @NotBlank(message = "검색어를 입력해주세요")
        String searchName) {

}
