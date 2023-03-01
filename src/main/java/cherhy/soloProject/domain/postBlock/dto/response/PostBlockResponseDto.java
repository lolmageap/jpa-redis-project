package cherhy.soloProject.domain.postBlock.dto.response;

import cherhy.soloProject.domain.post.dto.PostPhotoDto;


public record PostBlockResponseDto(
        Long id,
        PostPhotoDto post
) {}
