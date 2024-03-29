package cherhy.soloProject.domain.reply.dto.response;

import cherhy.soloProject.domain.reply.entity.Reply;

import java.util.List;
import java.util.stream.Collectors;

public record ResponseReplyDto(
        Long memberId, String memberName, Long postId, String content
) {
    public static List<ResponseReplyDto> of(Long postId, List<Reply> replies) {
        return replies.stream()
                .map(reply -> new ResponseReplyDto(
                        reply.getMember().getId(),
                        reply.getMember().getName(),
                        postId,
                        reply.getContent()))
                .collect(Collectors.toList());
    }
}
