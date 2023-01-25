package cherhy.soloProject.application.domain.photo.entity;

import cherhy.soloProject.application.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {
    @Id @GeneratedValue
    @Column(name = "photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String photo; //사진 링크

    @Builder
    public Photo(Post post, String photo) {
        this.post = post;
        this.photo = photo;
    }
}
