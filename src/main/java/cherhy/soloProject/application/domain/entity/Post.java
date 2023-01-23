package cherhy.soloProject.application.domain.entity;

import cherhy.soloProject.Util.BaseEntity;
import cherhy.soloProject.application.domain.dto.MemberDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String title;
    private String content;
    @OneToMany(mappedBy = "post")
    private List<Photo> photos = new ArrayList<>();

    @Builder
    public Post(Member member, String title, String content, List<Photo> photos) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.photos = photos;
    }




    //    @OneToMany
//    @JoinColumn()
//    private List<TimeLine> timeLines = new ArrayList<>();

}
