package cherhy.soloProject.application.domain.reply.dto;

import cherhy.soloProject.application.domain.member.entity.Member;

import javax.validation.constraints.NotNull;

public record ResponseReplyDto(
        Long memberId, String memberName, Long postId, String content
) {
}
