package cherhy.soloProject.application.domain.follow.dto.request;

public record FollowQueryDto(
        Long key, Long memberId, int size
) {
}
