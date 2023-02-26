package cherhy.soloProject.application.domain.member.entity;

import cherhy.soloProject.Util.BaseTimeEntity;
import cherhy.soloProject.application.domain.follow.entity.Follow;
import cherhy.soloProject.application.domain.post.entity.Post;
import cherhy.soloProject.application.domain.postBlock.entity.PostBlock;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "MEMBER",
        indexes = @Index(name = "user_id__index__member", columnList = "user_id"))

public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    @Column(unique = true, name = "user_id")
    private String userId;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    //member -> follow 단방향 참조
    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.REMOVE)
    private List<Follow> follows = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<PostBlock> postBlocks = new ArrayList<>();

    @Builder //회원가입
    public Member(String userId, String name, String email, String password, LocalDate birthday) {
        this.userId = requireNonNull(userId);
        this.name = requireNonNull(name);
        this.password = requireNonNull(password);
        this.email = requireNonNull(email);
    }

    public void changeName(String changeName) {
        this.name = changeName;
    }

    public void changeUserId(String changeUserId) {
        this.userId = changeUserId;
    }

    public void changePassword(String changePassword) {
        this.password = changePassword;
    }

}
