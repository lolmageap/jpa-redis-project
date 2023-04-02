package cherhy.soloProject.domain.postBlock.dto.response;

import cherhy.soloProject.domain.post.dto.PostPhotoDto;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


public record PostBlockResponseDto(
        Long id,
        PostPhotoDto post
) implements Serializable {

    public static List<PostBlockResponseDto> from(List<PostBlock> postBlocks){
        return postBlocks.stream().map(p -> new PostBlockResponseDto(p.getId(), new PostPhotoDto(p.getPost())))
                .collect(Collectors.toList());
    }

}
