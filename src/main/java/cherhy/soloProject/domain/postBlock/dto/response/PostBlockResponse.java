package cherhy.soloProject.domain.postBlock.dto.response;

import cherhy.soloProject.domain.post.dto.response.PostPhotoResponse;
import cherhy.soloProject.domain.postBlock.entity.PostBlock;

import java.io.Serializable;


public record PostBlockResponse(
        Long id,
        PostPhotoResponse post
) implements Serializable {

    public static PostBlockResponse of(PostBlock postBlock){
        return new PostBlockResponse(
                postBlock.getId(),
                PostPhotoResponse.of(postBlock.getPost())
        );
    }

}
