package cherhy.soloProject.domain.photo.entity;

import cherhy.soloProject.domain.post.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;
    private String photo;

    public static Photo of(Post post, String photo){
        return new Photo(post, photo);
    }

    public Photo(Post post, String photo) {
        this.post = post;
        this.photo = photo;
    }

}
