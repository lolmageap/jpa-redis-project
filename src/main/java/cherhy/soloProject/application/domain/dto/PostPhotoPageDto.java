package cherhy.soloProject.application.domain.dto;

import cherhy.soloProject.application.domain.entity.Photo;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostPhotoPageDto {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private List<String> photos = new ArrayList<>();

    @QueryProjection
    public PostPhotoPageDto(Long id, Long memberId, String title, String content, List<Photo> photos) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        for (Photo photo : photos) {
            this.photos.add(photo.getPhoto());
        }
    }
}
