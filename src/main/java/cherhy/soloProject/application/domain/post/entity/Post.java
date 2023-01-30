package cherhy.soloProject.application.domain.post.entity;

import cherhy.soloProject.Util.BaseEntity;
import cherhy.soloProject.application.domain.member.entity.Member;
import cherhy.soloProject.application.domain.photo.entity.Photo;
import cherhy.soloProject.application.domain.reply.entity.Reply;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
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
    @OneToMany(mappedBy = "post")
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<TimeLine> timeLines = new ArrayList<>();

    @Builder
    public Post(Member member, String title, String content, List<Photo> photos) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.photos = photos;
    }

    public void changeTitle(String changeTitle) {
        this.title = changeTitle;
    }

    public void changeContent(String changeContent) {
        this.content = changeContent;
    }

    public void changePhoto(List<Photo> changePhoto) {
        this.photos = changePhoto;
    }

}
