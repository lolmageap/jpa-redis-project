package cherhy.soloProject.domain.post.dto.response;


import cherhy.soloProject.domain.photo.entity.Photo;
import cherhy.soloProject.domain.post.entity.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class PostPhotoResponse implements Serializable {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Integer likeCount;
    private List<String> photos = new ArrayList<>();

    public static PostPhotoResponse of(Post post) {
        return new PostPhotoResponse(post);
    }

    @QueryProjection
    public PostPhotoResponse(Long id, Long memberId, String title, String content, Integer likeCount, List<Photo> photos) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.likeCount = likeCount;
        this.content = content;
        for (Photo photo : photos) {
            this.photos.add(photo.getPhoto());
        }
    }

    private PostPhotoResponse(Post post) {
        this.id = post.getId();
        this.memberId = post.getMember().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        for (Photo photo : post.getPhotos()) {
            this.photos.add(photo.getPhoto());
        }
    }


}
