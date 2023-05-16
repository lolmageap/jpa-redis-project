package cherhy.soloProject.domain.follow.dto.request;

public record FollowQueryRequest(
        Long key, Long memberId, int size
) {
}
