package cherhy.soloProject.domain.post.entity;

import cherhy.soloProject.Util.BaseEntity;
import cherhy.soloProject.domain.member.entity.Member;
import cherhy.soloProject.domain.photo.entity.Photo;
import cherhy.soloProject.domain.post.dto.request.PostRequestDto;
import cherhy.soloProject.domain.reply.entity.Reply;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    private String content;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Reply> replies = new ArrayList<>();

    private Integer likeCount;

//    @Version //낙관적 락
//    private Long version;

    public static Post of(PostRequestDto postRequestDto, Member member){
        return Post.builder()
                .member(member)
                .title(postRequestDto.title())
                .content(postRequestDto.content())
                .photos(postRequestDto.photos())
                .build();
    }

    @Builder
    public Post(Member member, String title, String content, List<String> photos, Integer likeCount) {
        this.member = member;
        this.title = title;
        this.content = content;
        for (String photo : photos) {
            addPhoto(photo);
        }
        this.likeCount = likeCount == null ? 0 : likeCount;
    }

    public void addId(Long id){
        this.id = id;
    }

    public void addPhoto(String photoName){
        Photo photo = Photo.of(this, photoName);
        this.photos.add(photo);
    }

    @PrePersist
    public void prePersist() {
        this.likeCount = this.likeCount == null ? 0 : this.likeCount;
    }

    public void updatePostLikeCount(Integer likeCount){
        this.likeCount = likeCount;
    }

}
