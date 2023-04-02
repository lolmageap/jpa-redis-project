package cherhy.soloProject.domain.reply.dto.response;

import cherhy.soloProject.domain.reply.entity.Reply;

import java.util.List;
import java.util.stream.Collectors;

public record ResponseReplyDto(
        Long memberId, String memberName, Long postId, String content
) {
    public static List<ResponseReplyDto> from(Long postId, List<Reply> replies) {
        return replies.stream().map(r -> new ResponseReplyDto(
                r.getMember().getId(), r.getMember().getName(), postId, r.getContent())
                ).collect(Collectors.toList());
    }
}
