package cherhy.soloProject.application.domain.post.dto;


import cherhy.soloProject.application.domain.photo.entity.Photo;
import cherhy.soloProject.application.domain.post.entity.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class PostPhotoDto{
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private List<String> photos = new ArrayList<>();

    @QueryProjection
    public PostPhotoDto(Long id, Long memberId, String title, String content, List<Photo> photos) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        for (Photo photo : photos) {
            this.photos.add(photo.getPhoto());
        }
    }

    public PostPhotoDto(Post post) {
        this.id = post.getId();
        this.memberId = post.getMember().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        for (Photo photo : post.getPhotos()) {
            this.photos.add(photo.getPhoto());
        }
    }
}
