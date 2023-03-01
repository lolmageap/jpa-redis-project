package cherhy.soloProject.domain.reply.dto.response;

public record ResponseReplyDto(
        Long memberId, String memberName, Long postId, String content
) {
}
