package cherhy.soloProject.domain.reply.dto;

import javax.validation.constraints.NotNull;

public record RequestReplyDto(
        @NotNull(message = "게시물 선택오류") Long postId,
        String content
) {
}
