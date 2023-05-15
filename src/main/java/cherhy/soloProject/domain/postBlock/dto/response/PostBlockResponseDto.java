package cherhy.soloProject.domain.postBlock.dto.response;

import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


public record PostBlockResponseDto(
        Long id,
        PostPhotoDto post
) implements Serializable {

    public static PostBlockResponseDto of(PostBlock postBlock){
        return new PostBlockResponseDto(
                postBlock.getId(),
                PostPhotoDto.of(postBlock.getPost())
        );
    }

}
