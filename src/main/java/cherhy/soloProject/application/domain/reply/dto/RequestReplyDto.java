package cherhy.soloProject.application.domain.reply.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record RequestReplyDto(
        @NotNull(message = "세션 만료") Long memberId,
        @NotNull(message = "게시물 선택오류") Long postId,
        String content
) {
}
