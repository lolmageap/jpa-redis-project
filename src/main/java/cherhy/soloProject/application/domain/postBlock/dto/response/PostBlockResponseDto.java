package cherhy.soloProject.application.domain.postBlock.dto.response;

import cherhy.soloProject.application.domain.post.dto.PostPhotoDto;


public record PostBlockResponseDto(
        Long id,
        PostPhotoDto post
) {}
