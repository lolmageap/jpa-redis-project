package cherhy.soloProject.domain.postBlock.dto.response;

import cherhy.soloProject.domain.post.dto.PostPhotoDto;

import java.io.Serializable;


public record PostBlockResponseDto(
        Long id,
        PostPhotoDto post
) implements Serializable {}
